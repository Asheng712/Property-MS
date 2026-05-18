<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useRepairStore } from '@/stores/repair'
import type { RepairRecord } from '@/types'

const router = useRouter()
const route = useRoute()
const repairStore = useRepairStore()

const repair = ref<RepairRecord | null>(null)

const statusSteps = [
  { text: '已提交' },
  { text: '已派单' },
  { text: '处理中' },
  { text: '已完成' },
]

const statusMap: Record<number, string> = { 0: '待处理', 1: '处理中', 2: '已完成' }
const statusTagMap: Record<number, string> = { 0: 'warning', 1: 'primary', 2: 'success' }
const priorityMap: Record<number, string> = { 1: '普通', 2: '紧急' }

onMounted(async () => {
  const id = Number(route.params.id)
  await repairStore.fetchRepairs()
  repair.value = repairStore.myRepairs.find((r) => r.id === id) || null
})
</script>

<template>
  <div class="page-container--with-nav">
    <van-nav-bar title="报修详情" left-arrow @click-left="router.back()" />

    <div v-if="repair">
      <van-cell-group title="工单信息" style="margin-top: 8px">
        <van-cell title="工单编号" :value="repair.repairNo" />
        <van-cell title="房屋" :value="repair.houseName || `#${repair.houseId}`" />
        <van-cell title="报修内容" :value="repair.content" />
        <van-cell title="优先级">
          <template #value>
            <van-tag :type="repair.priority === 2 ? 'danger' : 'default'" size="small">
              {{ priorityMap[repair.priority] }}
            </van-tag>
          </template>
        </van-cell>
        <van-cell title="状态">
          <template #value>
            <van-tag :type="statusTagMap[repair.status] as any" size="small">
              {{ statusMap[repair.status] }}
            </van-tag>
          </template>
        </van-cell>
        <van-cell title="报修人" :value="repair.reporter" />
        <van-cell title="维修师傅" :value="repair.workerName || '待分配'" />
        <van-cell title="创建时间" :value="repair.createTime || '-'" />
        <van-cell v-if="repair.finishTime" title="完成时间" :value="repair.finishTime" />
      </van-cell-group>

      <van-cell-group title="进度跟踪" style="margin-top: 12px">
        <div style="padding: 24px 16px">
          <van-steps direction="vertical" :active="repair.status + 1">
            <van-step v-for="step in statusSteps" :key="step.text">
              {{ step.text }}
              <template v-if="step.text === '已完成' && repair.status < 2">
                <span style="color: var(--van-text-color-weak); font-size: 12px">（等待中）</span>
              </template>
            </van-step>
          </van-steps>
        </div>
      </van-cell-group>
    </div>

    <EmptyState v-else description="未找到工单信息" />
  </div>
</template>
