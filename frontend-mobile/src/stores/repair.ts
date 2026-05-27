import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { repairApi } from '@/services/api'
import type { RepairRecord, RepairPayload } from '@/types'

export const useRepairStore = defineStore('repair', () => {
  const allRepairs = ref<RepairRecord[]>([])
  const loading = ref(false)

  const myRepairs = computed(() => allRepairs.value)

  const pendingRepairs = computed(() => myRepairs.value.filter((r) => r.status === 0))
  const processingRepairs = computed(() => myRepairs.value.filter((r) => r.status === 1))
  const completedRepairs = computed(() => myRepairs.value.filter((r) => r.status === 2))

  async function fetchRepairs() {
    loading.value = true
    try {
      const kanban = await repairApi.getKanban()
      allRepairs.value = [...kanban.pending, ...kanban.processing, ...kanban.completed]
    } finally {
      loading.value = false
    }
  }

  async function createRepair(payload: RepairPayload) {
    await repairApi.create(payload)
    await fetchRepairs()
  }

  return {
    allRepairs,
    myRepairs,
    pendingRepairs,
    processingRepairs,
    completedRepairs,
    loading,
    fetchRepairs,
    createRepair,
  }
})
