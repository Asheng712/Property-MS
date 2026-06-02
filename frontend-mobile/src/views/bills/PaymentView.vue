<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useBillStore } from '@/stores/bill'
import { billApi, paymentApi } from '@/services/api'
import { formatCurrency } from '@/utils/format'
import { showToast } from 'vant'
import type { BillRecord } from '@/types'

const router = useRouter()
const route = useRoute()
const billStore = useBillStore()

const bill = ref<BillRecord | null>(null)
const payType = ref('微信支付')
const loading = ref(false)
const submitting = ref(false)

const payMethods = [
  { label: '微信支付', value: 'WECHAT' },
  { label: '支付宝', value: 'ALIPAY' },
  { label: '银行卡', value: 'TRANSFER' },
]

async function loadBill() {
  const id = Number(route.params.id)
  if (!id) return

  // 先从当前 store 数据里找（BillListView 已加载）
  const fromStore = billStore.bills.find((b) => b.id === id)
  if (fromStore) {
    bill.value = fromStore
    return
  }

  // 兜底：通过 API 按账单号查
  loading.value = true
  try {
    const result = await billApi.getList({ page: 1, pageSize: 100 })
    bill.value = result.records.find((b) => b.id === id) ?? null
  } catch {
    showToast('加载账单失败')
  } finally {
    loading.value = false
  }
}

async function confirmPay() {
  if (!bill.value) return
  if (bill.value.payStatus === 1) {
    showToast('该账单已缴费')
    return
  }

  submitting.value = true
  try {
    await paymentApi.create({
      billId: bill.value.id,
      payType: payType.value,
      payAmount: bill.value.amount,
    })
    showToast('缴费成功')
    // 刷新 store 中的账单列表
    billStore.resetPagination()
    await billStore.fetchBills()
    router.back()
  } catch (err: any) {
    showToast(err?.message || '缴费失败，请重试')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadBill()
})
</script>

<template>
  <div class="page-container--with-nav">
    <van-nav-bar title="确认缴费" left-arrow @click-left="router.back()" />

    <div v-if="!bill && loading" style="padding: 48px; text-align: center">
      <van-loading size="24" />
      <p style="margin-top: 12px; color: var(--van-text-color-weak)">加载中...</p>
    </div>

    <template v-else-if="bill">
      <van-cell-group title="账单详情" style="margin-top: 8px">
        <van-cell title="房屋" :value="bill.houseName || `#${bill.houseId}`" />
        <van-cell title="账单编号" :value="bill.billNo" />
        <van-cell title="费用类型" :value="bill.type || '-'" />
        <van-cell title="截止日期" :value="bill.deadline || '-'" />
        <van-cell title="应缴金额">
          <template #value>
            <span style="color: #ee0a24; font-weight: 700; font-size: 20px">{{ formatCurrency(bill.amount) }}</span>
          </template>
        </van-cell>
      </van-cell-group>

      <van-cell-group title="支付方式" style="margin-top: 12px">
        <van-radio-group v-model="payType">
          <van-cell
            v-for="method in payMethods"
            :key="method.value"
            :title="method.label"
            clickable
            @click="payType = method.value"
          >
            <template #right-icon>
              <van-radio :name="method.value" />
            </template>
          </van-cell>
        </van-radio-group>
      </van-cell-group>

      <div style="margin: 32px 16px">
        <van-button
          type="primary"
          block
          round
          size="large"
          :loading="submitting"
          loading-text="支付中..."
          @click="confirmPay"
        >
          确认支付 {{ formatCurrency(bill.amount) }}
        </van-button>
      </div>
    </template>

    <EmptyState v-else description="未找到账单信息" />
  </div>
</template>
