<script setup lang="ts">
import { onActivated } from 'vue'
import { useRouter } from 'vue-router'
import { useBillStore } from '@/stores/bill'
import { formatCurrency } from '@/utils/format'

const router = useRouter()
const billStore = useBillStore()

const tabs = [
  { title: '待缴费', status: 0 },
  { title: '已缴费', status: 1 },
]

function onTabChange(idx: number) {
  billStore.activeTab = idx
  billStore.resetPagination()
  billStore.fetchBills()
}

function goPay(billId: number) {
  router.push(`/bills/pay/${billId}`)
}

onActivated(() => {
  if (billStore.bills.length === 0) {
    billStore.fetchBills()
  }
})
</script>

<template>
  <div class="page-container">
    <van-nav-bar title="我的账单" fixed placeholder />

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
        <div v-for="bill in billStore.bills" :key="bill.id" class="card-wrapper">
          <van-swipe-cell>
            <div style="padding: 14px 16px">
              <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px">
                <span style="font-weight: 600; font-size: 15px">{{ bill.houseName || `房屋 #${bill.houseId}` }}</span>
                <van-tag :type="bill.payStatus === 0 ? 'warning' : 'success'" size="medium">
                  {{ bill.payStatus === 0 ? '待缴费' : '已缴费' }}
                </van-tag>
              </div>
              <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px">
                <span style="color: var(--van-text-color-weak); font-size: 13px">账单编号: {{ bill.billNo }}</span>
                <span class="amount-text">{{ formatCurrency(bill.amount) }}</span>
              </div>
              <div style="display: flex; justify-content: space-between; font-size: 12px; color: var(--van-text-color-weak)">
                <span>{{ bill.type }} | 截止: {{ bill.deadline || '-' }}</span>
              </div>
            </div>
            <template v-if="bill.payStatus === 0" #right>
              <van-button
                type="primary"
                text="去缴费"
                style="height: 100%; border-radius: 0"
                @click="goPay(bill.id)"
              />
            </template>
          </van-swipe-cell>
        </div>

        <EmptyState v-if="!billStore.loading && billStore.bills.length === 0" description="暂无账单" />
      </van-list>
    </van-pull-refresh>
  </div>
</template>
