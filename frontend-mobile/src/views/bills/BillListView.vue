<script setup lang="ts">
import { onActivated, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useBillStore } from '@/stores/bill'
import { formatCurrency } from '@/utils/format'
import type { BillRecord } from '@/types'

const router = useRouter()
const billStore = useBillStore()

const selectedBills = ref<number[]>([])
const selectMode = ref(false)

const feeTypeLabels: Record<number, string> = { 1: '租金', 2: '买房', 3: '押金', 4: '物业费' }

const tabs = [
  { title: '待缴费', status: 0 },
  { title: '已缴费', status: 2 },
]

function onTabChange(idx: number) {
  billStore.activeTab = idx
  billStore.resetPagination()
  billStore.fetchBills()
}

function goPay() {
  if (selectedBills.value.length === 0) return
  router.push(`/bills/pay/${selectedBills.value.join(',')}`)
}

function toggleBill(id: number) {
  const idx = selectedBills.value.indexOf(id)
  if (idx >= 0) {
    selectedBills.value.splice(idx, 1)
  } else {
    selectedBills.value.push(id)
  }
}

function cancelSelect() {
  selectMode.value = false
  selectedBills.value = []
}

onActivated(() => {
  billStore.resetPagination()
  billStore.fetchBills()
})
</script>

<template>
  <div class="page-container">
    <van-nav-bar title="我的账单" fixed placeholder>
      <template #right>
        <span v-if="!selectMode && billStore.activeTab === 0" style="color: var(--van-primary-color); font-size: 14px" @click="selectMode = true">合并缴费</span>
        <span v-else-if="selectMode" style="color: var(--van-danger-color); font-size: 14px" @click="cancelSelect">取消</span>
      </template>
    </van-nav-bar>

    <van-tabs
      v-model:active="billStore.activeTab"
      sticky
      offset-top="46"
      @change="onTabChange"
    >
      <van-tab v-for="tab in tabs" :key="tab.status" :title="tab.title" />
    </van-tabs>

    <van-pull-refresh
      v-model="billStore.refreshing"
      @refresh="billStore.onRefresh"
    >
      <van-list
        v-model:loading="billStore.loading"
        :finished="billStore.finished"
        finished-text="没有更多了"
        @load="billStore.onLoad"
      >
        <van-cell
          v-for="bill in billStore.bills"
          :key="bill.id"
          :title="bill.houseName || `房屋 #${bill.houseId}`"
          :label="`${bill.billNo}\n${feeTypeLabels[bill.feeType] || '费用'} · ${bill.billMonth || '-'} · 截止 ${bill.dueDate || '-'}`"
          title-style="font-weight: 600; font-size: 15px"
          label-style="margin-top: 6px; line-height: 1.6; white-space: pre-line"
          :clickable="bill.status === 0"
          @click="bill.status === 0 ? (selectMode ? toggleBill(bill.id) : goPay(bill.id)) : undefined"
        >
          <template #icon v-if="selectMode">
            <van-checkbox
              :model-value="selectedBills.includes(bill.id)"
              style="margin-right: 8px; align-self: center"
              @click.stop="toggleBill(bill.id)"
            />
          </template>
          <template #value>
            <span class="amount-text" style="font-size: 17px">{{ formatCurrency(bill.amount) }}</span>
          </template>
          <template #extra>
            <van-tag :type="bill.status === 0 ? 'warning' : 'success'" size="medium">
              {{ bill.statusText || (bill.status === 0 ? '待缴费' : '已缴费') }}
            </van-tag>
          </template>
        </van-cell>

        <EmptyState v-if="!billStore.loading && billStore.bills.length === 0" description="暂无账单" />
      </van-list>
    </van-pull-refresh>

    <!-- 合并缴费按钮 -->
    <div v-if="selectMode" style="position: fixed; bottom: 50px; left: 12px; right: 12px; z-index: 100">
      <van-button type="primary" block round size="large" :disabled="selectedBills.length === 0" @click="goPay">
        合并缴费（已选 {{ selectedBills.length }} 笔）
      </van-button>
    </div>
  </div>
</template>
