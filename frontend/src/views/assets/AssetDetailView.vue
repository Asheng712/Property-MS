<template>
  <PageContainer title="资产详情" description="集中展示资产基础信息、业主信息和运营状态。">
    <template #actions>
      <el-button @click="router.back()">返回</el-button>
      <el-button type="primary" plain @click="router.push('/assets')">资产台账</el-button>
    </template>

    <el-alert
      v-if="errorMessage"
      class="detail-alert"
      :title="errorMessage"
      type="warning"
      show-icon
      :closable="false"
    />

    <div v-loading="loading" class="detail-grid">
      <PanelCard title="基础资料" description="数据来自资产分页接口。">
        <InfoList v-if="asset" :items="baseItems" />
        <el-empty v-else description="暂无资产数据" />
      </PanelCard>

      <PanelCard title="运营概览">
        <div v-if="asset" class="metric-grid">
          <div class="metric-item">
            <span>资产状态</span>
            <StatusBadge :label="getStatusText(asset.status)" :tone="getStatusTone(asset.status)" />
          </div>
          <div class="metric-item">
            <span>建筑面积</span>
            <strong>{{ formatArea(Number(asset.area)) }}</strong>
          </div>
          <div class="metric-item">
            <span>业主姓名</span>
            <strong>{{ asset.ownerName || '-' }}</strong>
          </div>
          <div class="metric-item">
            <span>联系方式</span>
            <strong>{{ asset.ownerPhone || '-' }}</strong>
          </div>
        </div>
        <el-empty v-else description="请选择资产查看详情" />
      </PanelCard>
    </div>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRoute, useRouter } from 'vue-router'
import InfoList from '@/components/InfoList.vue'
import PageContainer from '@/components/PageContainer.vue'
import PanelCard from '@/components/PanelCard.vue'
import StatusBadge from '@/components/StatusBadge.vue'
import { assetApi } from '@/services/api'
import { formatArea } from '@/utils/format'
import type { AssetRecord } from '@/types'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const errorMessage = ref('')
const asset = ref<AssetRecord | null>(null)

const baseItems = computed(() =>
  asset.value
    ? [
        { label: '资产 ID', value: asset.value.id },
        { label: '上级资产 ID', value: asset.value.parentId ?? '-' },
        { label: '资产名称', value: asset.value.name },
        { label: '资产类型', value: getTypeText(asset.value.type) },
        { label: '创建时间', value: asset.value.createTime || '-' },
      ]
    : [],
)

onMounted(() => {
  void loadDetail()
})

async function loadDetail() {
  loading.value = true
  errorMessage.value = ''

  try {
    const id = Number(route.params.id)
    const result = await assetApi.getList({ page: 1, pageSize: 1000 })
    asset.value = result.records.find((item) => item.id === id) ?? null

    if (!asset.value) {
      errorMessage.value = '未找到对应资产，请返回资产台账重新选择'
    }
  } catch (error) {
    const message = error instanceof Error ? error.message : '加载资产详情失败'
    errorMessage.value = message
    ElMessage.error(message)
  } finally {
    loading.value = false
  }
}

function getTypeText(value: string) {
  const normalized = value.trim().toUpperCase()
  const mapping: Record<string, string> = {
    BUILDING: '楼栋',
    UNIT: '单元',
    RESIDENTIAL: '住宅',
    SHOP: '商铺',
  }

  return mapping[normalized] || value
}

function getStatusText(value: string) {
  const normalized = value.trim().toLowerCase()
  const mapping: Record<string, string> = {
    occupied: '已入住',
    vacant: '空置',
    sold: '已售',
  }

  return mapping[normalized] || value
}

function getStatusTone(value: string) {
  const normalized = value.toLowerCase()
  const mapping: Record<string, 'success' | 'warning' | 'info'> = {
    occupied: 'success',
    vacant: 'warning',
    sold: 'info',
  }

  return mapping[normalized] || 'info'
}
</script>

<style scoped>
.detail-alert {
  margin-bottom: 16px;
}

.detail-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(320px, 420px);
  gap: 20px;
  min-height: 360px;
}

.metric-grid {
  display: grid;
  gap: 14px;
}

.metric-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 16px;
  border: 1px solid #edf1f7;
  border-radius: 16px;
  background: #f8fbff;
}

.metric-item span {
  color: #8ea0b8;
}

.metric-item strong {
  color: #23324a;
}

@media (max-width: 1024px) {
  .detail-grid {
    grid-template-columns: 1fr;
  }
}
</style>
