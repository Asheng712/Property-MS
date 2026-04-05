<template>
  <PageContainer title="报表导出与导入" description="统一管理财务导出、资产导入和周报推送任务，支持操作留痕与任务状态更新。">
    <div class="reports-grid">
      <PanelCard title="任务进度">
        <article v-for="card in cards" :key="card.id" class="report-task">
          <div class="report-task__text">
            <strong>{{ card.title }}</strong>
            <span>{{ card.subtitle }}</span>
          </div>
          <div class="report-task__meta">
            <span>{{ card.metric }}</span>
            <el-progress :percentage="card.progress" />
          </div>
        </article>
      </PanelCard>

      <PanelCard title="快捷操作">
        <div class="quick-actions">
          <button v-for="action in reportActions" :key="action.id" type="button" class="quick-actions__item" @click="runAction(action.title)">
            <el-icon :size="24"><component :is="action.icon" /></el-icon>
            <span>{{ action.title }}</span>
          </button>
        </div>
      </PanelCard>
    </div>

    <PanelCard title="最近执行记录">
      <el-table :data="history">
        <el-table-column prop="time" label="执行时间" min-width="160" />
        <el-table-column prop="title" label="任务名称" min-width="160" />
        <el-table-column prop="operator" label="执行人" min-width="120" />
        <el-table-column label="状态" min-width="120">
          <template #default="{ row }">
            <StatusBadge :label="row.status" :tone="row.status === '已完成' ? 'success' : 'info'" />
          </template>
        </el-table-column>
      </el-table>
    </PanelCard>
  </PageContainer>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import PageContainer from '@/components/PageContainer.vue'
import PanelCard from '@/components/PanelCard.vue'
import StatusBadge from '@/components/StatusBadge.vue'
import { reportActions, reportCards } from '@/mock/data'
import { nowText } from '@/utils/format'

const cards = reactive(reportCards.map((item) => ({ ...item })))
const history = ref([
  { time: '2026-04-05 08:00', title: '收费日报', operator: '管理员', status: '已完成' },
  { time: '2026-04-05 08:10', title: '资产台账导入', operator: '财务经理', status: '执行中' },
])

function runAction(title: string) {
  history.value = [{ time: nowText(), title, operator: '管理员', status: '已完成' }, ...history.value]
  const target = cards.find((item) => title.includes(item.title.replace('日报', '')) || title.includes(item.title))
  if (target) {
    target.progress = Math.min(100, target.progress + 6)
  }
  ElMessage.success(`${title} 已开始执行`)
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
  padding: 18px 0;
  border-bottom: 1px solid #edf1f7;
}

.report-task:last-child {
  border-bottom: none;
}

.report-task__text {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-bottom: 12px;
}

.report-task__text strong {
  color: #20304b;
}

.report-task__text span,
.report-task__meta span {
  color: #8ea0b8;
}

.quick-actions {
  display: grid;
  gap: 14px;
}

.quick-actions__item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 18px;
  border: 1px solid #e8eef6;
  border-radius: 18px;
  background: linear-gradient(180deg, #fbfdff, #f5f9ff);
  color: #234;
  cursor: pointer;
}

@media (max-width: 1024px) {
  .reports-grid {
    grid-template-columns: 1fr;
  }
}
</style>
