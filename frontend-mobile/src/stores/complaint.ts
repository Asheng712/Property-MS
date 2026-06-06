import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { complaintApi } from '@/services/api'
import type { ComplaintRecord, ComplaintCreatePayload } from '@/types'

export const useComplaintStore = defineStore('complaint', () => {
  const complaints = ref<ComplaintRecord[]>([])
  const loading = ref(false)

  const pendingComplaints = computed(() => complaints.value.filter((c) => c.status === 0))
  const completedComplaints = computed(() => complaints.value.filter((c) => c.status === 2))

  async function fetchComplaints() {
    loading.value = true
    try {
      const result = await complaintApi.getList({ page: 1, pageSize: 100 })
      complaints.value = result.records
    } finally {
      loading.value = false
    }
  }

  async function createComplaint(payload: ComplaintCreatePayload) {
    await complaintApi.create(payload)
    await fetchComplaints()
  }

  return {
    complaints,
    pendingComplaints,
    completedComplaints,
    loading,
    fetchComplaints,
    createComplaint,
  }
})
