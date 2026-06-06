<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { assetApi } from '@/services/api'
import { formatArea } from '@/utils/format'
import type { AssetRecord } from '@/types'

const router = useRouter()

const assets = ref<AssetRecord[]>([])
const loading = ref(true)

const typeLabelMap: Record<string, string> = {
  BUILDING: '楼栋',
  UNIT: '单元',
  RESIDENTIAL: '住宅',
  SHOP: '商铺',
}

const statusTagMap: Record<string, string> = {
  VACANT: 'primary',
  SOLD: 'danger',
  RENTING: 'warning',
  DECORATING: 'primary',
  OCCUPIED: 'success',
}

const statusLabelMap: Record<string, string> = {
  VACANT: '空置',
  SOLD: '已售',
  RENTING: '招租中',
  DECORATING: '装修中',
  OCCUPIED: '已入住',
}

onMounted(async () => {
  loading.value = true
  try {
    const result = await assetApi.getList({ page: 1, pageSize: 100 })
    // 过滤掉楼栋（BUILDING）以及已入住/已售资产，仅展示可购买/租赁的叶子节点
    assets.value = result.records.filter(a =>
      a.type !== 'BUILDING' &&
      a.status !== 'OCCUPIED' &&
      a.status !== 'SOLD'
    )
  } catch {
    /* ignore */
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="page-container--with-nav">
    <van-nav-bar title="可购资产" left-arrow @click-left="router.back()" />

    <PageSkeleton v-if="loading" />

    <template v-else>
      <div
        v-for="item in assets"
        :key="item.id"
        class="card-wrapper"
        style="margin-bottom: 10px"
        @click="router.push(`/assets/${item.id}`)"
      >
        <div style="padding: 14px 16px">
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px">
            <span style="font-weight: 600; font-size: 15px">{{ item.name }}</span>
            <van-tag :type="(statusTagMap[item.status] || 'default') as any" size="small">
              {{ statusLabelMap[item.status] || item.status || '未知' }}
            </van-tag>
          </div>
          <div style="display: flex; justify-content: space-between; font-size: 13px; color: var(--van-text-color-weak)">
            <span>类型: {{ typeLabelMap[item.type] || item.type }}</span>
            <span>面积: {{ item.area ? formatArea(item.area) : '-' }}</span>
          </div>
        </div>
      </div>

      <EmptyState v-if="!loading && assets.length === 0" description="暂无可用资产" />
    </template>
  </div>
</template>
