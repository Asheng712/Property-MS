<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useComplaintStore } from '@/stores/complaint'
import type { ComplaintRecord } from '@/types'

const router = useRouter()
const route = useRoute()
const complaintStore = useComplaintStore()

const complaint = ref<ComplaintRecord | null>(null)

const statusMap: Record<number, string> = { 0: '待处理', 1: '处理中', 2: '已办结' }
const statusTagMap: Record<number, string> = { 0: 'warning', 1: 'warning', 2: 'success' }

onMounted(async () => {
  const id = Number(route.params.id)
  await complaintStore.fetchComplaints()
  complaint.value = complaintStore.complaints.find((c) => c.id === id) || null
})
</script>

<template>
  <div class="page-container--with-nav">
    <van-nav-bar title="投诉详情" left-arrow @click-left="router.back()" />

    <div v-if="complaint">
      <van-cell-group title="投诉信息" style="margin-top: 8px">
        <van-cell title="投诉编号" :value="complaint.complaintNo" />
        <van-cell title="分类">
          <template #value>
            <van-tag type="primary" size="small" plain>{{ complaint.category }}</van-tag>
          </template>
        </van-cell>
        <van-cell title="状态">
          <template #value>
            <van-tag :type="statusTagMap[complaint.status] as any" size="small">
              {{ statusMap[complaint.status] }}
            </van-tag>
          </template>
        </van-cell>
        <van-cell title="提交时间" :value="complaint.createTime || '-'" />
      </van-cell-group>

      <van-cell-group title="投诉内容" style="margin-top: 12px">
        <div style="padding: 12px 16px; font-size: 14px; line-height: 1.6; white-space: pre-wrap">
          {{ complaint.content }}
        </div>
      </van-cell-group>

      <van-cell-group v-if="complaint.handleResult" title="处理结果" style="margin-top: 12px">
        <div style="padding: 12px 16px; font-size: 14px; line-height: 1.6; white-space: pre-wrap; color: var(--van-success-color)">
          {{ complaint.handleResult }}
        </div>
      </van-cell-group>
    </div>

    <EmptyState v-else description="未找到投诉信息" />
  </div>
</template>
