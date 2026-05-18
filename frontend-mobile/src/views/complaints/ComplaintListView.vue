<script setup lang="ts">
import { ref, onActivated } from 'vue'
import { useRouter } from 'vue-router'
import { useComplaintStore } from '@/stores/complaint'
import type { ComplaintRecord } from '@/types'

const router = useRouter()
const complaintStore = useComplaintStore()

const activeTab = ref(0)

const statusMap: Record<number, string> = { 0: '待处理', 1: '已处理' }
const statusTagMap: Record<number, string> = { 0: 'warning', 1: 'success' }

function filteredList(): ComplaintRecord[] {
  if (activeTab.value === 0) return complaintStore.complaints
  return complaintStore.complaints.filter((c) => c.status === activeTab.value - 1)
}

onActivated(() => {
  complaintStore.fetchComplaints()
})
</script>

<template>
  <div class="page-container">
    <van-nav-bar title="我的投诉" fixed placeholder>
      <template #right>
        <van-icon name="plus" size="20" @click="router.push('/complaints/create')" />
      </template>
    </van-nav-bar>

    <van-tabs v-model:active="activeTab" sticky offset-top="46">
      <van-tab title="全部" />
      <van-tab title="待处理" />
      <van-tab title="已处理" />
    </van-tabs>

    <EmptyState
      v-if="!complaintStore.loading && complaintStore.complaints.length === 0"
      description="暂无投诉记录"
      action-text="提交投诉"
      @action="router.push('/complaints/create')"
    />

    <div v-for="item in filteredList()" :key="item.id" class="card-wrapper" @click="router.push(`/complaints/${item.id}`)">
      <div style="padding: 14px 16px">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px">
          <span style="font-weight: 600; font-size: 15px">{{ item.complaintNo }}</span>
          <van-tag :type="statusTagMap[item.status] as any" size="small">
            {{ statusMap[item.status] }}
          </van-tag>
        </div>
        <div style="display: flex; gap: 6px; margin-bottom: 6px">
          <van-tag type="primary" size="small" plain>{{ item.category }}</van-tag>
        </div>
        <p style="font-size: 14px; margin-bottom: 4px; color: var(--van-text-color); line-height: 1.5; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden">
          {{ item.content }}
        </p>
        <div style="font-size: 12px; color: var(--van-text-color-weak); text-align: right">
          {{ item.createTime || '-' }}
        </div>
      </div>
    </div>
  </div>
</template>
