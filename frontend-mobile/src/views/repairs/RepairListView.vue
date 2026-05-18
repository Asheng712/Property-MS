<script setup lang="ts">
import { ref, onActivated } from 'vue'
import { useRouter } from 'vue-router'
import { useRepairStore } from '@/stores/repair'
import type { RepairRecord } from '@/types'

const router = useRouter()
const repairStore = useRepairStore()

const activeTab = ref(0)

const statusMap: Record<number, string> = { 0: '待处理', 1: '处理中', 2: '已完成' }
const statusTagMap: Record<number, string> = { 0: 'warning', 1: 'primary', 2: 'success' }
const priorityMap: Record<number, string> = { 1: '普通', 2: '紧急' }

function filteredList(): RepairRecord[] {
  if (activeTab.value === 0) return repairStore.myRepairs
  return repairStore.myRepairs.filter((r) => r.status === activeTab.value - 1)
}

onActivated(() => {
  repairStore.fetchRepairs()
})
</script>

<template>
  <div class="page-container">
    <van-nav-bar title="我的报修" fixed placeholder>
      <template #right>
        <van-icon name="plus" size="20" @click="router.push('/repairs/create')" />
      </template>
    </van-nav-bar>

    <van-tabs v-model:active="activeTab" sticky offset-top="46">
      <van-tab title="全部" />
      <van-tab title="待处理" />
      <van-tab title="处理中" />
      <van-tab title="已完成" />
    </van-tabs>

    <EmptyState
      v-if="!repairStore.loading && repairStore.myRepairs.length === 0"
      description="暂无报修记录"
      action-text="提交报修"
      @action="router.push('/repairs/create')"
    />

    <div v-for="item in filteredList()" :key="item.id" class="card-wrapper" @click="router.push(`/repairs/${item.id}`)">
      <div style="padding: 14px 16px">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px">
          <span style="font-weight: 600; font-size: 15px">{{ item.repairNo }}</span>
          <div style="display: flex; gap: 6px">
            <van-tag :type="priorityMap[item.priority] === '紧急' ? 'danger' : 'default'" size="small">
              {{ priorityMap[item.priority] }}
            </van-tag>
            <van-tag :type="statusTagMap[item.status] as any" size="small">
              {{ statusMap[item.status] }}
            </van-tag>
          </div>
        </div>
        <p style="font-size: 14px; margin-bottom: 6px; color: var(--van-text-color); line-height: 1.5">
          {{ item.content }}
        </p>
        <div style="display: flex; justify-content: space-between; font-size: 12px; color: var(--van-text-color-weak)">
          <span>{{ item.houseName || `房屋 #${item.houseId}` }}</span>
          <span>{{ item.createTime || '-' }}</span>
        </div>
      </div>
    </div>
  </div>
</template>
