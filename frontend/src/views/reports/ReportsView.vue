<template>
  <PageContainer title="报表导出与导入" description="统一管理财务导出、资产导入和周报推送任务，减少重复人工整理工作。">
    <div class="reports-grid">
      <section class="surface-card">
        <div class="section-title">
          <h3>任务进度</h3>
        </div>
        <article v-for="card in reportCards" :key="card.id" class="report-task">
          <div class="report-task__text">
            <strong>{{ card.title }}</strong>
            <span>{{ card.subtitle }}</span>
          </div>
          <div class="report-task__meta">
            <span>{{ card.metric }}</span>
            <el-progress :percentage="card.progress" />
          </div>
        </article>
      </section>

      <section class="surface-card">
        <div class="section-title">
          <h3>快捷操作</h3>
        </div>
        <div class="quick-actions">
          <button v-for="action in reportActions" :key="action.id" type="button" class="quick-actions__item" @click="runAction(action.title)">
            <el-icon :size="24"><component :is="action.icon" /></el-icon>
            <span>{{ action.title }}</span>
          </button>
        </div>
      </section>
    </div>
  </PageContainer>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus'
import PageContainer from '@/components/PageContainer.vue'
import { reportActions, reportCards } from '@/mock/data'

function runAction(title: string) {
  ElMessage.success(`${title} 已开始执行`)
}
</script>

<style scoped>
.reports-grid {
  display: grid;
  grid-template-columns: 1.2fr 0.8fr;
  gap: 20px;
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
