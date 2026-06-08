<script setup lang="ts">
import { computed, ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useBillStore } from '@/stores/bill'
import { billApi, paymentApi } from '@/services/api'
import { formatCurrency } from '@/utils/format'
import { showToast } from 'vant'
import type { BillRecord } from '@/types'

const router = useRouter()
const route = useRoute()
const billStore = useBillStore()

const selectedBills = ref<BillRecord[]>([])
const payMethod = ref<number>(1)
const voucherUrl = ref('')
const remark = ref('')
const loading = ref(false)
const submitting = ref(false)

const feeTypeLabels: Record<number, string> = { 1: '租金', 2: '买房', 3: '押金', 4: '物业费' }

const payMethods = [
  { label: '微信支付', value: 1 },
  { label: '支付宝', value: 2 },
  { label: '银行卡', value: 3 },
  { label: '现金', value: 4 },
]

const totalAmount = computed(() =>
  selectedBills.value.reduce((sum, b) => sum + (b.amount || 0), 0)
)

async function loadBills() {
  const idsParam = String(route.params.id)
  const ids = idsParam.split(',').map(Number).filter(n => !isNaN(n))
  if (ids.length === 0) return

  loading.value = true
  try {
    const result = await billApi.getList({ page: 1, pageSize: 100 })
    selectedBills.value = result.records.filter(b => ids.includes(b.id) && b.status === 0)
    if (selectedBills.value.length === 0) {
      showToast('没有可缴费的账单')
    }
  } catch {
    showToast('加载账单失败')
  } finally {
    loading.value = false
  }
}

async function confirmPay() {
  if (selectedBills.value.length === 0) return

  submitting.value = true
  try {
    await paymentApi.submit({
      billIds: selectedBills.value.map(b => b.id),
      payMethod: payMethod.value,
      voucherUrl: voucherUrl.value.trim() || undefined,
      remark: remark.value.trim() || undefined,
    })
    showToast('缴费提交成功，等待管理员核销')
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
  loadBills()
})
</script>

<template>
  <div class="page-container--with-nav">
    <van-nav-bar title="确认缴费" left-arrow @click-left="router.back()" />

    <div v-if="loading" style="padding: 48px; text-align: center">
      <van-loading size="24" />
      <p style="margin-top: 12px; color: var(--van-text-color-weak)">加载中...</p>
    </div>

    <template v-else-if="selectedBills.length > 0">
      <!-- 账单明细 -->
      <van-cell-group title="账单明细">
        <van-cell
          v-for="bill in selectedBills"
          :key="bill.id"
          :title="bill.houseName || `#${bill.houseId}`"
          :label="`${feeTypeLabels[bill.feeType] || '-'} · ${bill.billMonth || '-'}`"
        >
          <template #value>
            <span style="color: var(--van-text-color)">{{ formatCurrency(bill.amount) }}</span>
          </template>
        </van-cell>
        <van-cell title="合计金额">
          <template #value>
            <span style="color: #ee0a24; font-weight: 700; font-size: 20px">{{ formatCurrency(totalAmount) }}</span>
          </template>
        </van-cell>
      </van-cell-group>

      <!-- 支付方式 -->
      <van-cell-group title="支付方式" style="margin-top: 12px">
        <van-radio-group v-model="payMethod">
          <van-cell
            v-for="method in payMethods"
            :key="method.value"
            :title="method.label"
            clickable
            @click="payMethod = method.value"
          >
            <template #right-icon>
              <van-radio :name="method.value" />
            </template>
          </van-cell>
        </van-radio-group>
      </van-cell-group>

      <!-- 支付凭证 -->
      <van-cell-group title="支付凭证（可选）" style="margin-top: 12px">
        <van-field
          v-model="voucherUrl"
          placeholder="输入支付截图/凭证链接"
          rows="1"
        />
        <van-field
          v-model="remark"
          placeholder="备注信息"
          rows="1"
        />
      </van-cell-group>

      <div style="margin: 32px 16px">
        <van-button
          type="primary"
          block
          round
          size="large"
          :loading="submitting"
          loading-text="提交中..."
          @click="confirmPay"
        >
          确认缴费 {{ formatCurrency(totalAmount) }}
        </van-button>
        <p style="text-align: center; margin-top: 12px; font-size: 12px; color: var(--van-text-color-weak)">
          提交后需管理员核销确认
        </p>
      </div>
    </template>

    <EmptyState v-else description="无可缴费账单" />
  </div>
</template>
