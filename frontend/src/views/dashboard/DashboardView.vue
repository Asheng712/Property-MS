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

      <div class="section-header">
        <h3>各类型资产租售分布</h3>
        <span class="section-sub">单位：%</span>
      </div>

      <div v-if="rentalCharts.length === 0" class="empty-chart">
        暂无资产数据，请先在「资产管理」中添加资产
      </div>

      <div class="chart-grid" :style="{ gridTemplateColumns: `repeat(${Math.min(rentalCharts.length, 2)}, minmax(0, 1fr))` }">
        <PieLegendCard
          v-for="chart in rentalCharts"
          :key="chart.type"
          :title="chart.typeName + '租售情况'"
          :center-label="'共 ' + chart.totalCount + ' 项'"
          :segments="chart.segments"
        />
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
import { dashboardApi } from '@/services/api'
import type { AssetRentalStatus, ChartSegment, DashboardData, QuickStat } from '@/types'

const loading = ref(false)
const errorMessage = ref('')
const dashboard = ref<DashboardData>({
  pendingChargeCount: 0,
  pendingChargeAmount: 0,
  overdueCount: 0,
  overdueAmount: 0,
  todayComplaintCount: 0,
  pendingComplaintCount: 0,
  todayRepairCount: 0,
  pendingRepairCount: 0,
  assetRentalList: [],
})

const stats = computed<QuickStat[]>(() => {
  return [
    {
      id: 'unpaid-count',
      label: '待收费数据',
      value: dashboard.value.pendingChargeCount,
      unit: '条',
      color: '#a855f7',
      accent: '#f3e8ff',
      icon: 'bell',
    },
    {
      id: 'unpaid-amount',
      label: '待收费金额',
      value: formatMoney(dashboard.value.pendingChargeAmount),
      unit: '元',
      color: '#2563eb',
      accent: '#dbeafe',
      icon: 'money',
    },
    {
      id: 'overdue-count',
      label: '欠费数据',
      value: dashboard.value.overdueCount,
      unit: '条',
      color: '#f97316',
      accent: '#ffedd5',
      icon: 'warning',
    },
    {
      id: 'overdue-amount',
      label: '欠费金额',
      value: formatMoney(dashboard.value.overdueAmount),
      unit: '元',
      color: '#ef4444',
      accent: '#fee2e2',
      icon: 'document',
    },
    {
      id: 'today-complaints',
      label: '今日投诉',
      value: dashboard.value.todayComplaintCount,
      unit: '件',
      color: '#16a34a',
      accent: '#dcfce7',
      icon: 'service',
    },
    {
      id: 'pending-complaints',
      label: '投诉待办',
      value: dashboard.value.pendingComplaintCount,
      unit: '件',
      color: '#ec4899',
      accent: '#fce7f3',
      icon: 'clock',
    },
    {
      id: 'today-repairs',
      label: '今日报修',
      value: dashboard.value.todayRepairCount,
      unit: '件',
      color: '#e11d48',
      accent: '#ffe4e6',
      icon: 'tool',
    },
    {
      id: 'pending-repairs',
      label: '报修待办',
      value: dashboard.value.pendingRepairCount,
      unit: '件',
      color: '#14b8a6',
      accent: '#ccfbf1',
      icon: 'repair',
    },
  ]
})

interface RentalChartData {
  type: string
  typeName: string
  totalCount: number
  segments: ChartSegment[]
}

const rentalCharts = computed<RentalChartData[]>(() => {
  return (dashboard.value.assetRentalList || [])
    .filter(item => item.totalCount > 0)
    .map(item => ({
      type: item.type,
      typeName: item.typeName,
      totalCount: item.totalCount,
      segments: buildRentalSegments(item),
    }))
})

onMounted(() => {
  void loadDashboard()
})

async function loadDashboard() {
  loading.value = true
  errorMessage.value = ''

  try {
    dashboard.value = await dashboardApi.getData()
  } catch (error) {
    const message = error instanceof Error ? error.message : '加载管理员看板数据失败'
    errorMessage.value = message
    ElMessage.error(message)
  } finally {
    loading.value = false
  }
}

function buildRentalSegments(status: AssetRentalStatus): ChartSegment[] {
  return [
    { label: '已入住', value: Math.round(status.occupiedRate || 0), color: '#2563eb' },
    { label: '已出租', value: Math.round(status.rentedRate || 0), color: '#10b981' },
    { label: '已出售', value: Math.round(status.soldRate || 0), color: '#f59e0b' },
    { label: '空置',   value: Math.round(status.vacantRate || 0), color: '#94a3b8' },
  ].filter(s => s.value > 0)
}

function formatMoney(value?: number) {
  return Number(value || 0).toLocaleString('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  })
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

.section-header {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  margin: 28px 0 18px;
}

.section-header h3 {
  margin: 0;
  font-size: 18px;
  color: var(--text-heading);
}

.section-sub {
  color: var(--text-subtle);
  font-size: 14px;
}

.empty-chart {
  padding: 48px 0;
  text-align: center;
  color: var(--text-subtle);
}

.chart-grid {
  display: grid;
  gap: 20px;
}

@media (max-width: 1024px) {
  .dashboard-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 768px) {
  .dashboard-grid {
    grid-template-columns: 1fr;
  }
}
</style>
