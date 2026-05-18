import { ref } from 'vue'
import { defineStore } from 'pinia'
import { noticeApi } from '@/services/api'
import type { NoticeRecord } from '@/types'

export const useNoticeStore = defineStore('notice', () => {
  const notices = ref<NoticeRecord[]>([])
  const loading = ref(false)
  const refreshing = ref(false)
  const finished = ref(false)
  const page = ref(1)
  const pageSize = 10

  async function fetchNotices() {
    loading.value = true
    try {
      const result = await noticeApi.getList({
        page: page.value,
        pageSize,
        status: 'PUBLISHED',
      })
      if (page.value === 1) {
        notices.value = result.records
      } else {
        notices.value.push(...result.records)
      }
      finished.value = result.records.length < pageSize
    } finally {
      loading.value = false
      refreshing.value = false
    }
  }

  function resetPagination() {
    page.value = 1
    finished.value = false
    notices.value = []
  }

  function onRefresh() {
    refreshing.value = true
    resetPagination()
    fetchNotices()
  }

  function onLoad() {
    page.value++
    fetchNotices()
  }

  return {
    notices,
    loading,
    refreshing,
    finished,
    fetchNotices,
    resetPagination,
    onRefresh,
    onLoad,
  }
})
