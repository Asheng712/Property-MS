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
        <van-swipe-cell
          v-for="bill in billStore.bills"
          :key="bill.id"
          :right-width="65"
        >
          <van-cell
            :title="bill.houseName || `房屋 #${bill.houseId}`"
            :label="`${bill.billNo}\n${bill.type} · 截止 ${bill.deadline || '-'}`"
            title-style="font-weight: 600; font-size: 15px"
            label-style="margin-top: 6px; line-height: 1.6; white-space: pre-line"
            clickable
            @click="bill.payStatus === 0 ? goPay(bill.id) : undefined"
          >
            <template #value>
              <span class="amount-text" style="font-size: 17px">{{ formatCurrency(bill.amount) }}</span>
            </template>
            <template #extra>
              <van-tag :type="bill.payStatus === 0 ? 'warning' : 'success'" size="medium">
                {{ bill.payStatus === 0 ? '待缴费' : '已缴费' }}
              </van-tag>
            </template>
          </van-cell>

          <template #right>
            <van-button
              square
              type="primary"
              text="缴费"
              style="height: 100%"
              @click="goPay(bill.id)"
            />
          </template>
        </van-swipe-cell>

        <EmptyState v-if="!billStore.loading && billStore.bills.length === 0" description="暂无账单" />
      </van-list>
    </van-pull-refresh>
  </div>
</template>
