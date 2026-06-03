<template>
  <PageContainer title="系统监控面板" description="实时查看后端健康状态、API 性能指标和系统运行情况。">
    <template #actions>
      <el-button plain :loading="loading" @click="refreshAll">刷新数据</el-button>
    </template>

    <div v-loading="loading" class="monitor-content">
      <div class="dashboard-grid">
        <StatCard
          v-for="stat in stats"
          :key="stat.id"
          :stat="stat"
        />
      </div>

      <div class="panels-row">
        <PanelCard title="后端服务健康状态" description="来自 /health 端点">
          <el-tag
            :type="healthTagType"
            size="large"
            effect="dark"
            round
          >
            {{ healthDisplay.status }}
          </el-tag>
          <div class="health-meta">
            <div v-if="healthDisplay.timestamp" class="meta-item">
              <span class="meta-label">时间戳</span>
              <span class="meta-value">{{ healthDisplay.timestamp }}</span>
            </div>
            <div v-if="healthDisplay.version" class="meta-item">
              <span class="meta-label">版本</span>
              <span class="meta-value">{{ healthDisplay.version }}</span>
            </div>
          </div>
          <div v-if="healthError" class="health-error">
            {{ healthError }}
          </div>
        </PanelCard>

        <PanelCard title="前端指标详情" description="API 请求计数、错误率、平均响应时间">
          <el-table :data="metricTableData" size="small" stripe>
            <el-table-column prop="label" label="指标" width="160" />
            <el-table-column prop="value" label="当前值" />
          </el-table>
        </PanelCard>
      </div>

      <div class="panels-row">
        <PanelCard title="系统综合评估" description="基于前后端指标自动判断">
          <div class="assessment">
            <div class="assessment-row">
              <span>API 请求总数</span>
              <span>{{ monitor.getRequestCount() }}</span>
            </div>
            <div class="assessment-row">
              <span>错误率</span>
              <span :class="{ 'text-danger': monitor.getErrorRate() > 5 }">
                {{ monitor.getErrorRate().toFixed(1) }}%
              </span>
            </div>
            <div class="assessment-row">
              <span>平均响应时间</span>
              <span :class="{ 'text-warning': monitor.getAverageResponseTime() > 2000 }">
                {{ monitor.getAverageResponseTime().toFixed(0) }}ms
              </span>
            </div>
            <el-divider />
            <div class="assessment-row">
              <span>综合状态</span>
              <el-tag :type="overallTagType" effect="dark" round size="small">
                {{ overallHealth.status }}
              </el-tag>
            </div>
            <div v-if="overallHealth.issues.length > 0" class="health-issues">
              <div v-for="issue in overallHealth.issues" :key="issue" class="issue-item">
                <el-icon color="#e6a23c"><WarningFilled /></el-icon>
                {{ issue }}
              </div>
            </div>
          </div>
        </PanelCard>
      </div>
    </div>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { WarningFilled } from '@element-plus/icons-vue'
import PageContainer from '@/components/PageContainer.vue'
import PanelCard from '@/components/PanelCard.vue'
import StatCard from '@/components/StatCard.vue'
import { monitorApi, type HealthStatus } from '@/services/api'
import { monitor, getSystemHealth } from '@/utils/monitor'
import type { QuickStat } from '@/types'

const loading = ref(false)
const healthError = ref('')
const healthData = ref<HealthStatus | null>(null)

const healthDisplay = computed(() => {
  if (healthData.value) {
    return {
      status: healthData.value.status === 'healthy' ? '服务正常' : '服务异常',
      timestamp: healthData.value.timestamp,
      version: healthData.value.version,
    }
  }
  return { status: '检测中...', timestamp: '', version: '' }
})

const healthTagType = computed(() => {
  if (!healthData.value) return 'info'
  return healthData.value.status === 'healthy' ? 'success' : 'danger'
})

const overallHealth = computed(() => getSystemHealth())

const overallTagType = computed(() => {
  if (overallHealth.value.status === 'healthy') return 'success'
  return 'warning'
})

const stats = computed<QuickStat[]>(() => [
  {
    id: 'requests',
    label: 'API 请求总数',
    value: monitor.getRequestCount(),
    unit: '次',
    color: '#409eff',
    accent: '#ecf5ff',
    icon: 'document',
  },
  {
    id: 'error',
    label: '错误率',
    value: `${monitor.getErrorRate().toFixed(1)}%`,
    unit: '',
    color: monitor.getErrorRate() > 5 ? '#f56c6c' : '#67c23a',
    accent: monitor.getErrorRate() > 5 ? '#fef0f0' : '#f0f9eb',
    icon: monitor.getErrorRate() > 5 ? 'warning' : 'bell',
  },
  {
    id: 'latency',
    label: '平均响应时间',
    value: `${monitor.getAverageResponseTime().toFixed(0)}ms`,
    unit: '',
    color: monitor.getAverageResponseTime() > 2000 ? '#e6a23c' : '#409eff',
    accent: monitor.getAverageResponseTime() > 2000 ? '#fdf6ec' : '#ecf5ff',
    icon: 'clock',
  },
  {
    id: 'health',
    label: '后端服务',
    value: healthDisplay.value.status,
    unit: '',
    color: healthData.value?.status === 'healthy' ? '#67c23a' : '#909399',
    accent: healthData.value?.status === 'healthy' ? '#f0f9eb' : '#f4f4f5',
    icon: 'repair',
  },
])

const metricTableData = computed(() => [
  { label: '请求总次数', value: monitor.getRequestCount() },
  { label: '成功次数', value: monitor.getMetrics().requests.success },
  { label: '失败次数', value: monitor.getMetrics().requests.error },
  { label: '错误率', value: `${monitor.getErrorRate().toFixed(1)}%` },
  { label: '平均响应时间', value: `${monitor.getAverageResponseTime().toFixed(0)}ms` },
])

async function loadHealth() {
  healthError.value = ''
  try {
    healthData.value = await monitorApi.getHealth()
  } catch (e) {
    healthError.value = `健康检查失败: ${e instanceof Error ? e.message : '未知错误'}`
    healthData.value = null
  }
}

async function refreshAll() {
  loading.value = true
  try {
    await loadHealth()
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  refreshAll()
})
</script>

<style scoped>
.monitor-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 20px;
}

.panels-row {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 20px;
}

.health-meta {
  margin-top: 16px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.meta-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 4px 0;
}

.meta-label {
  color: var(--text-subtle);
  font-size: 14px;
}

.meta-value {
  font-size: 14px;
  color: var(--text-heading);
  font-family: monospace;
}

.health-error {
  margin-top: 12px;
  color: var(--el-color-danger);
  font-size: 14px;
}

.assessment {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.assessment-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
}

.text-danger {
  color: var(--el-color-danger);
  font-weight: 600;
}

.text-warning {
  color: var(--el-color-warning);
  font-weight: 600;
}

.health-issues {
  margin-top: 4px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.issue-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #e6a23c;
}

@media (max-width: 768px) {
  .dashboard-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .panels-row {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 480px) {
  .dashboard-grid {
    grid-template-columns: 1fr;
  }
}
</style>
