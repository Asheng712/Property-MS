import { ref } from 'vue'
import { defineStore } from 'pinia'
import { billApi, paymentApi } from '@/services/api'
import type { BillRecord, BillQuery, PaymentRecord, PaymentQuery } from '@/types'

export const useBillStore = defineStore('bill', () => {
  const bills = ref<BillRecord[]>([])
  const payments = ref<PaymentRecord[]>([])
  const loading = ref(false)
  const refreshing = ref(false)
  const finished = ref(false)
  const page = ref(1)
  const pageSize = 10
  const activeTab = ref(0)

  async function fetchBills(params?: Partial<BillQuery>) {
    loading.value = true
    try {
      const query: BillQuery = {
        page: page.value,
        pageSize,
        ...params,
      }
      if (activeTab.value === 0) {
        query.payStatus = 0
      } else if (activeTab.value === 1) {
        query.payStatus = 1
      }
      const result = await billApi.getList(query)
      if (page.value === 1) {
        bills.value = result.records
      } else {
        bills.value.push(...result.records)
      }
      finished.value = result.records.length < pageSize
    } finally {
      loading.value = false
      refreshing.value = false
    }
  }

  async function fetchPayments(params?: Partial<PaymentQuery>) {
    const query: PaymentQuery = {
      page: 1,
      pageSize: 100,
      ...params,
    }
    const result = await paymentApi.getList(query)
    payments.value = result.records
  }

  function resetPagination() {
    page.value = 1
    finished.value = false
    bills.value = []
  }

  function onRefresh() {
    refreshing.value = true
    resetPagination()
    fetchBills()
  }

  function onLoad() {
    page.value++
    fetchBills()
  }

  return {
    bills,
    payments,
    loading,
    refreshing,
    finished,
    activeTab,
    fetchBills,
    fetchPayments,
    resetPagination,
    onRefresh,
    onLoad,
  }
})
