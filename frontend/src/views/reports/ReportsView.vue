<template>
  <PageContainer title="报表导出与导入" description="已接入文件任务列表、财务导出和资产导入接口。">
    <div class="reports-grid">
      <PanelCard title="快捷操作">
        <div class="quick-actions">
          <button type="button" class="quick-actions__item" @click="exportFinanceReport">
            <span>导出财务报表</span>
            <small>调用 /api/v1/system/export/finance</small>
          </button>
          <label class="quick-actions__item quick-actions__item--file">
            <span>导入资产数据</span>
            <small>调用 /api/v1/system/import/assets</small>
            <input type="file" accept=".xlsx,.xls,.csv" class="hidden-input" @change="handleImport" />
          </label>
        </div>
      </PanelCard>

      <PanelCard title="任务说明">
        <div class="report-task">
          <strong>当前页面已从本地 mock 切换为真实任务列表。</strong>
          <span>导出或导入后，会刷新系统任务记录。</span>
        </div>
      </PanelCard>
    </div>

    <PanelCard title="最近执行记录">
      <el-table v-loading="loading" :data="tasks">
        <el-table-column prop="createTime" label="执行时间" min-width="170" />
        <el-table-column prop="taskType" label="任务类型" min-width="120" />
        <el-table-column prop="fileName" label="文件名" min-width="220" />
        <el-table-column prop="operator" label="操作人" min-width="120" />
        <el-table-column prop="dataCount" label="数据量" min-width="100">
          <template #default="{ row }">{{ row.dataCount ?? '-' }}</template>
        </el-table-column>
        <el-table-column label="状态" min-width="120">
          <template #default="{ row }">
            <StatusBadge :label="row.status" :tone="row.status === 'SUCCESS' ? 'success' : 'info'" />
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next"
          :current-page="query.page"
          :page-size="query.pageSize"
          :page-sizes="[10, 20, 50]"
          :total="total"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
        />
      </div>
    </PanelCard>
  </PageContainer>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import PageContainer from '@/components/PageContainer.vue'
import PanelCard from '@/components/PanelCard.vue'
import StatusBadge from '@/components/StatusBadge.vue'
import { systemApi } from '@/services/api'
import type { FileTaskRecord } from '@/types'

const loading = ref(false)
const total = ref(0)
const tasks = ref<FileTaskRecord[]>([])

const query = reactive({
  page: 1,
  pageSize: 20,
})

onMounted(() => {
  void loadTasks()
})

async function loadTasks() {
  loading.value = true
  try {
    const result = await systemApi.getTasks({
      page: query.page,
      pageSize: query.pageSize,
    })
    tasks.value = result.records
    total.value = result.total
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '加载任务记录失败')
  } finally {
    loading.value = false
  }
}

function handlePageChange(page: number) {
  query.page = page
  void loadTasks()
}

function handleSizeChange(size: number) {
  query.page = 1
  query.pageSize = size
  void loadTasks()
}

async function exportFinanceReport() {
  try {
    await systemApi.exportFinance({
      startDate: '2026-01-01',
      endDate: '2026-12-31',
      reportType: 'finance',
    })
    ElMessage.success('财务导出任务已提交')
    await loadTasks()
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '导出财务报表失败')
  }
}

async function handleImport(event: Event) {
  const input = event.target as HTMLInputElement
  const file = input.files?.[0]
  if (!file) {
    return
  }

  try {
    await systemApi.importAssets(file)
    ElMessage.success('资产导入任务已提交')
    await loadTasks()
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '导入资产失败')
  } finally {
    input.value = ''
  }
}
</script>

<style scoped>
.reports-grid {
  display: grid;
  grid-template-columns: 1.2fr 0.8fr;
  gap: 20px;
  margin-bottom: 20px;
}

.report-task {
  display: grid;
  gap: 10px;
  color: #8ea0b8;
}

.quick-actions {
  display: grid;
  gap: 14px;
}

.quick-actions__item {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 18px;
  border: 1px solid #e8eef6;
  border-radius: 18px;
  background: linear-gradient(180deg, #fbfdff, #f5f9ff);
  color: #234;
  cursor: pointer;
  text-align: left;
}

.quick-actions__item--file {
  position: relative;
}

.hidden-input {
  position: absolute;
  inset: 0;
  opacity: 0;
  cursor: pointer;
}

.pagination-wrap {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

@media (max-width: 1024px) {
  .reports-grid {
    grid-template-columns: 1fr;
  }
}
</style>
