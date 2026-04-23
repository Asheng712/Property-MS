<template>
  <PageContainer title="管理员运营看板" description="实时聚合收费、报修、投诉和资产经营数据，帮助管理人员快速了解园区运行状态。">
    <template #actions>
      <el-button plain :loading="loading" @click="loadDashboard">刷新数据</el-button>
    </template>

    <el-alert
      v-if="errorMessage"
      class="dashboard-alert"
      :title="errorMessage"
      type="warning"
      show-icon
      :closable="false"
    />

    <div v-loading="loading" class="dashboard-content">
      <div class="dashboard-grid">
        <StatCard v-for="stat in stats" :key="stat.id" :stat="stat" />
      </div>

      <div class="chart-grid">
        <PieLegendCard title="商铺租售情况" center-label="商铺租售比率" :segments="rentalChart" />
        <PieLegendCard title="车位租售情况" center-label="车位租售比率" :segments="parkingChart" />
      </div>
    </div>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import PageContainer from '@/components/PageContainer.vue'
import PieLegendCard from '@/components/PieLegendCard.vue'
import StatCard from '@/components/StatCard.vue'
import { assetApi, complaintApi, financeApi, repairApi } from '@/services/api'
import type { AssetRecord, BillRecord, ChartSegment, ComplaintRecord, QuickStat, RepairKanban } from '@/types'

const PAGE_SIZE = 1000

const loading = ref(false)
const errorMessage = ref('')
const bills = ref<BillRecord[]>([])
const assets = ref<AssetRecord[]>([])
const complaints = ref<ComplaintRecord[]>([])
const repairKanban = ref<RepairKanban>({
  pending: [],
  processing: [],
  completed: [],
})

const stats = computed<QuickStat[]>(() => {
  const unpaidBills = bills.value.filter((bill) => bill.payStatus === 0)
  const overdueBills = unpaidBills.filter((bill) => isBeforeToday(bill.deadline))
  const todayComplaints = complaints.value.filter((complaint) => isToday(complaint.createTime))
  const pendingComplaints = complaints.value.filter((complaint) => complaint.status !== 2)
  const repairs = [
    ...repairKanban.value.pending,
    ...repairKanban.value.processing,
    ...repairKanban.value.completed,
  ]
  const todayRepairs = repairs.filter((repair) => isToday(repair.createTime))
  const pendingRepairs = [...repairKanban.value.pending, ...repairKanban.value.processing]

  return [
    {
      id: 'unpaid-count',
      label: '待收费数据',
      value: unpaidBills.length,
      unit: '条',
      color: '#a855f7',
      accent: '#f3e8ff',
      icon: 'bell',
    },
    {
      id: 'unpaid-amount',
      label: '待收费金额',
      value: sumBillAmount(unpaidBills),
      unit: '元',
      color: '#2563eb',
      accent: '#dbeafe',
      icon: 'money',
    },
    {
      id: 'overdue-count',
      label: '欠费数据',
      value: overdueBills.length,
      unit: '条',
      color: '#f97316',
      accent: '#ffedd5',
      icon: 'warning',
    },
    {
      id: 'overdue-amount',
      label: '欠费金额',
      value: sumBillAmount(overdueBills),
      unit: '元',
      color: '#ef4444',
      accent: '#fee2e2',
      icon: 'document',
    },
    {
      id: 'today-complaints',
      label: '今日投诉',
      value: todayComplaints.length,
      unit: '件',
      color: '#16a34a',
      accent: '#dcfce7',
      icon: 'service',
    },
    {
      id: 'pending-complaints',
      label: '投诉待办',
      value: pendingComplaints.length,
      unit: '件',
      color: '#ec4899',
      accent: '#fce7f3',
      icon: 'clock',
    },
    {
      id: 'today-repairs',
      label: '今日报修',
      value: todayRepairs.length,
      unit: '件',
      color: '#e11d48',
      accent: '#ffe4e6',
      icon: 'tool',
    },
    {
      id: 'pending-repairs',
      label: '报修待办',
      value: pendingRepairs.length,
      unit: '件',
      color: '#14b8a6',
      accent: '#ccfbf1',
      icon: 'repair',
    },
  ]
})

const rentalChart = computed<ChartSegment[]>(() => buildAssetSegments(isShopAsset))
const parkingChart = computed<ChartSegment[]>(() => buildAssetSegments(isParkingAsset))

onMounted(() => {
  void loadDashboard()
})

async function loadDashboard() {
  loading.value = true
  errorMessage.value = ''

  try {
    const [billResult, assetResult, complaintResult, repairResult] = await Promise.all([
      financeApi.getBills({ page: 1, pageSize: PAGE_SIZE }),
      assetApi.getList({ page: 1, pageSize: PAGE_SIZE }),
      complaintApi.getList({ page: 1, pageSize: PAGE_SIZE }),
      repairApi.getKanban(),
    ])

    bills.value = billResult.records
    assets.value = assetResult.records
    complaints.value = complaintResult.records
    repairKanban.value = repairResult
  } catch (error) {
    const message = error instanceof Error ? error.message : '加载管理员看板数据失败'
    errorMessage.value = message
    ElMessage.error(message)
  } finally {
    loading.value = false
  }
}

function sumBillAmount(records: BillRecord[]) {
  return records
    .reduce((sum, bill) => sum + Number(bill.amount || 0), 0)
    .toLocaleString('zh-CN', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    })
}

function isToday(value?: string | null) {
  if (!value) {
    return false
  }

  const date = new Date(value)
  if (Number.isNaN(date.getTime())) {
    return false
  }

  const now = new Date()
  return date.getFullYear() === now.getFullYear() && date.getMonth() === now.getMonth() && date.getDate() === now.getDate()
}

function isBeforeToday(value?: string | null) {
  if (!value) {
    return false
  }

  const date = new Date(value)
  if (Number.isNaN(date.getTime())) {
    return false
  }

  const today = new Date()
  today.setHours(0, 0, 0, 0)
  date.setHours(0, 0, 0, 0)
  return date < today
}

function buildAssetSegments(predicate: (asset: AssetRecord) => boolean): ChartSegment[] {
  const scopedAssets = assets.value.filter(predicate)
  const total = scopedAssets.length
  const vacant = scopedAssets.filter((asset) => normalizeStatus(asset.status) === 'vacant').length
  const rented = scopedAssets.filter((asset) => normalizeStatus(asset.status) === 'occupied').length
  const sold = scopedAssets.filter((asset) => normalizeStatus(asset.status) === 'sold').length

  return [
    { label: '空置', value: toPercent(vacant, total), color: '#3b82f6' },
    { label: '已出租', value: toPercent(rented, total), color: '#10b981' },
    { label: '已出售', value: toPercent(sold, total), color: '#f59e0b' },
  ]
}

function toPercent(value: number, total: number) {
  if (total === 0) {
    return 0
  }

  return Math.round((value / total) * 100)
}

function isShopAsset(asset: AssetRecord) {
  return normalizeType(asset.type) === 'shop'
}

function isParkingAsset(asset: AssetRecord) {
  return ['parking', 'parking_space', 'garage', 'carport'].includes(normalizeType(asset.type))
}

function normalizeType(value: string) {
  return value.trim().toLowerCase()
}

function normalizeStatus(value: string) {
  const normalized = value.trim().toLowerCase()
  const mapping: Record<string, 'occupied' | 'vacant' | 'sold'> = {
    rented: 'occupied',
    rent: 'occupied',
    leased: 'occupied',
    occupied: 'occupied',
    vacant: 'vacant',
    empty: 'vacant',
    idle: 'vacant',
    sold: 'sold',
    sale: 'sold',
  }

  return mapping[normalized] ?? normalized
}
</script>

<style scoped>
.dashboard-alert {
  margin-bottom: 16px;
}

.dashboard-content {
  min-height: 420px;
}

.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 20px;
}

.chart-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 20px;
  margin-top: 20px;
}

@media (max-width: 1200px) {
  .dashboard-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .chart-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .dashboard-grid {
    grid-template-columns: 1fr;
  }
}
</style>
