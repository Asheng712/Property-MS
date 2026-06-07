<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { purchaseApi } from '@/services/api'
import type { PurchaseApplicationRecord } from '@/types'

const router = useRouter()

const applications = ref<PurchaseApplicationRecord[]>([])
const loading = ref(true)
const activeTab = ref(0)

const statusMap: Record<number, string> = { 0: '待审批', 1: '已通过', 2: '已拒绝' }
const statusTagMap: Record<number, string> = { 0: 'warning', 1: 'success', 2: 'danger' }

const typeLabelMap: Record<string, string> = {
  PURCHASE: '购买',
  RENTAL: '租赁',
}

const tabStatusMapping: Record<number, number | undefined> = {
  0: undefined,
  1: 0,
  2: 1,
  3: 2,
}

onMounted(async () => {
  await loadApplications()
})

async function loadApplications() {
  loading.value = true
  try {
    const statusFilter = tabStatusMapping[activeTab.value]
    const result = await purchaseApi.getList({
      page: 1,
      pageSize: 100,
      status: statusFilter,
    })
    applications.value = result.records
  } catch {
    /* ignore */
  } finally {
    loading.value = false
  }
}

async function onTabChange(tab: number) {
  activeTab.value = tab
  await loadApplications()
}
</script>

<template>
  <div class="page-container--with-nav">
    <van-nav-bar title="资产申请记录" left-arrow @click-left="router.back()" />

    <van-tabs v-model:active="activeTab" sticky @change="onTabChange">
      <van-tab title="全部" />
      <van-tab title="待审批" />
      <van-tab title="已通过" />
      <van-tab title="已拒绝" />
    </van-tabs>

    <PageSkeleton v-if="loading" />

    <EmptyState
      v-else-if="applications.length === 0"
      description="暂无资产申请记录"
    />

    <div
      v-for="item in applications"
      :key="item.id"
      class="card-wrapper"
      style="margin-bottom: 10px"
    >
      <div style="padding: 14px 16px">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px">
          <span style="font-weight: 600; font-size: 15px">{{ item.applicationNo }}</span>
          <van-tag :type="statusTagMap[item.status] as any" size="small">
            {{ statusMap[item.status] || '未知' }}
          </van-tag>
        </div>
        <div style="display: flex; gap: 6px; margin-bottom: 6px">
          <van-tag type="primary" size="small" plain>
            {{ typeLabelMap[item.type || ''] || '购买' }}
          </van-tag>
          <span style="font-size: 13px; color: var(--van-text-color)">{{ item.houseName || `资产ID: ${item.houseId}` }}</span>
        </div>
        <div v-if="item.proposedPrice" style="font-size: 13px; color: var(--van-text-color); margin-bottom: 4px">
          {{ (item.type === 'RENTAL' || !item.startDate) ? '价格: ' : '售价: ' }}
          <span style="color: var(--van-price-color)">{{ item.proposedPrice.toLocaleString() }} 元{{ item.type === 'RENTAL' ? '/月' : '' }}</span>
        </div>
        <div v-if="item.startDate && item.endDate" style="font-size: 12px; color: var(--van-text-color-weak); margin-bottom: 4px">
          合同期限: {{ item.startDate }} ~ {{ item.endDate }}
        </div>
        <div v-if="item.remark" style="font-size: 12px; color: var(--van-text-color-weak); margin-bottom: 4px">
          备注: {{ item.remark }}
        </div>
        <div style="font-size: 12px; color: var(--van-text-color-weak); text-align: right">
          {{ item.createTime || '-' }}
        </div>
      </div>
    </div>
  </div>
</template>
