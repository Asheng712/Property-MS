<template>
  <PageContainer title="投诉建议闭环" description="统一追踪投诉来源、优先级、处理进度和回访结果，避免问题在跨部门之间丢失。">
    <template #actions>
      <el-button type="primary" class="btn-primary-gradient" @click="feedbackDrawer = true">快速回访</el-button>
    </template>

    <DataToolbar v-model:keyword="keyword" v-model:status="status" placeholder="搜索投诉人、主题..." :filters="filters" />

    <section class="surface-card">
      <el-table :data="filteredComplaints">
        <el-table-column prop="id" label="编号" min-width="120" />
        <el-table-column prop="resident" label="投诉人" min-width="120" />
        <el-table-column prop="topic" label="投诉主题" min-width="220" />
        <el-table-column prop="createdAt" label="提交时间" min-width="160" />
        <el-table-column label="优先级" min-width="110">
          <template #default="{ row }">
            <span class="status-pill" :class="getPriorityClass(row.priority)">{{ getPriorityText(row.priority) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" min-width="120">
          <template #default="{ row }">
            <span class="status-pill" :class="getStatusClass(row.status)">{{ getStatusText(row.status) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button link type="primary" @click="openComplaint(row.id)">处理</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>

    <el-drawer v-model="feedbackDrawer" title="投诉回访记录" size="420px">
      <el-form label-position="top" class="dialog-form">
        <el-form-item label="回访结果">
          <el-select v-model="feedback.status">
            <el-option label="已解决" value="resolved" />
            <el-option label="待跟进" value="pending" />
          </el-select>
        </el-form-item>
        <el-form-item label="回访说明">
          <el-input v-model="feedback.note" type="textarea" rows="6" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="drawer-footer">
          <el-button @click="feedbackDrawer = false">取消</el-button>
          <el-button type="primary" @click="submitFeedback">保存回访</el-button>
        </div>
      </template>
    </el-drawer>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import DataToolbar from '@/components/DataToolbar.vue'
import PageContainer from '@/components/PageContainer.vue'
import { complaintRecords } from '@/mock/data'

const keyword = ref('')
const status = ref('')
const feedbackDrawer = ref(false)

const filters = [
  { label: '新建', value: 'new' },
  { label: '跟进中', value: 'following' },
  { label: '已关闭', value: 'closed' },
]

const statusText = {
  new: '新建',
  following: '跟进中',
  closed: '已关闭',
}

const statusClass = {
  new: 'warning',
  following: 'info',
  closed: 'success',
}

const priorityText = {
  high: '高优先级',
  medium: '中优先级',
  low: '低优先级',
}

const priorityClass = {
  high: 'danger',
  medium: 'warning',
  low: 'info',
}

const feedback = reactive({
  status: 'resolved',
  note: '',
})

const filteredComplaints = computed(() =>
  complaintRecords.filter((item) => {
    const matchesKeyword = !keyword.value || `${item.resident}${item.topic}`.includes(keyword.value)
    const matchesStatus = !status.value || item.status === status.value
    return matchesKeyword && matchesStatus
  }),
)

function openComplaint(id: string) {
  feedbackDrawer.value = true
  ElMessage.info(`正在处理投诉 ${id}`)
}

function submitFeedback() {
  feedbackDrawer.value = false
  ElMessage.success('投诉回访记录已保存')
}

function getStatusText(statusValue: 'new' | 'following' | 'closed') {
  return statusText[statusValue]
}

function getStatusClass(statusValue: 'new' | 'following' | 'closed') {
  return statusClass[statusValue]
}

function getPriorityText(priorityValue: 'high' | 'medium' | 'low') {
  return priorityText[priorityValue]
}

function getPriorityClass(priorityValue: 'high' | 'medium' | 'low') {
  return priorityClass[priorityValue]
}
</script>

<style scoped>
.drawer-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
