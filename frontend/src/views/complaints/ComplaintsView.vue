<template>
  <PageContainer title="投诉建议闭环" description="统一追踪投诉来源、优先级、处理进度和回访结果，支持处理动作真实落表。">
    <template #actions>
      <el-button type="primary" class="btn-primary-gradient" @click="openFeedback()">快速回访</el-button>
    </template>

    <DataToolbar v-model:keyword="keyword" v-model:status="status" placeholder="搜索投诉人、主题..." :filters="filters" />

    <PanelCard title="投诉列表">
      <el-table :data="filteredComplaints">
        <el-table-column prop="id" label="编号" min-width="120" />
        <el-table-column prop="resident" label="投诉人" min-width="120" />
        <el-table-column prop="topic" label="投诉主题" min-width="220" />
        <el-table-column prop="createdAt" label="提交时间" min-width="160" />
        <el-table-column label="优先级" min-width="110">
          <template #default="{ row }">
            <StatusBadge :label="getPriorityText(row.priority)" :tone="getPriorityTone(row.priority)" />
          </template>
        </el-table-column>
        <el-table-column label="状态" min-width="120">
          <template #default="{ row }">
            <StatusBadge :label="getStatusText(row.status)" :tone="getStatusTone(row.status)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140">
          <template #default="{ row }">
            <el-button link type="primary" @click="openDetail(row.id)">详情</el-button>
            <el-button link type="primary" @click="openFeedback(row.id)">处理</el-button>
          </template>
        </el-table-column>
      </el-table>
    </PanelCard>

    <el-drawer v-model="feedbackDrawer" title="投诉回访记录" size="420px">
      <el-form label-position="top" class="dialog-form">
        <el-form-item label="处理结果">
          <el-select v-model="feedback.status">
            <el-option label="已解决" value="closed" />
            <el-option label="跟进中" value="following" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级调整">
          <el-select v-model="feedback.priority">
            <el-option label="高优先级" value="high" />
            <el-option label="中优先级" value="medium" />
            <el-option label="低优先级" value="low" />
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

    <el-drawer v-model="detailVisible" title="投诉详情" size="420px">
      <InfoList v-if="activeComplaint" :items="detailItems" />
    </el-drawer>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import DataToolbar from '@/components/DataToolbar.vue'
import InfoList from '@/components/InfoList.vue'
import PageContainer from '@/components/PageContainer.vue'
import PanelCard from '@/components/PanelCard.vue'
import StatusBadge from '@/components/StatusBadge.vue'
import { complaintRecords } from '@/mock/data'
import type { ComplaintRecord } from '@/types'

type ComplaintStatus = ComplaintRecord['status']
type ComplaintPriority = ComplaintRecord['priority']

const keyword = ref('')
const status = ref('')
const feedbackDrawer = ref(false)
const detailVisible = ref(false)
const activeId = ref('')
const complaints = ref<ComplaintRecord[]>(complaintRecords.map((item) => ({ ...item })))

const filters = [
  { label: '新建', value: 'new' },
  { label: '跟进中', value: 'following' },
  { label: '已关闭', value: 'closed' },
]

const statusText: Record<ComplaintStatus, string> = {
  new: '新建',
  following: '跟进中',
  closed: '已关闭',
}

const statusTone: Record<ComplaintStatus, 'warning' | 'info' | 'success'> = {
  new: 'warning',
  following: 'info',
  closed: 'success',
}

const priorityText: Record<ComplaintPriority, string> = {
  high: '高优先级',
  medium: '中优先级',
  low: '低优先级',
}

const priorityTone: Record<ComplaintPriority, 'danger' | 'warning' | 'info'> = {
  high: 'danger',
  medium: 'warning',
  low: 'info',
}

const feedback = reactive({
  status: 'closed' as ComplaintStatus,
  priority: 'medium' as ComplaintPriority,
  note: '',
})

const filteredComplaints = computed(() =>
  complaints.value.filter((item) => {
    const matchesKeyword = !keyword.value || `${item.resident}${item.topic}`.includes(keyword.value)
    const matchesStatus = !status.value || item.status === status.value
    return matchesKeyword && matchesStatus
  }),
)

const activeComplaint = computed(() => complaints.value.find((item) => item.id === activeId.value) ?? null)
const detailItems = computed(() =>
  activeComplaint.value
    ? [
        { label: '投诉编号', value: activeComplaint.value.id },
        { label: '投诉人', value: activeComplaint.value.resident },
        { label: '主题', value: activeComplaint.value.topic },
        { label: '提交时间', value: activeComplaint.value.createdAt },
        { label: '优先级', value: getPriorityText(activeComplaint.value.priority) },
        { label: '状态', value: getStatusText(activeComplaint.value.status) },
      ]
    : [],
)

function openComplaint(complaintId?: string) {
  activeId.value = complaintId ?? complaints.value[0]?.id ?? ''
}

function openFeedback(complaintId?: string) {
  openComplaint(complaintId)
  if (activeComplaint.value) {
    feedback.status = activeComplaint.value.status === 'new' ? 'following' : activeComplaint.value.status
    feedback.priority = activeComplaint.value.priority
  }
  feedback.note = ''
  feedbackDrawer.value = true
}

function openDetail(complaintId: string) {
  openComplaint(complaintId)
  detailVisible.value = true
}

function submitFeedback() {
  complaints.value = complaints.value.map((item) =>
    item.id === activeId.value
      ? { ...item, status: feedback.status, priority: feedback.priority }
      : item,
  )
  feedbackDrawer.value = false
  ElMessage.success('投诉回访记录已保存')
}

function getStatusText(statusValue: ComplaintStatus) {
  return statusText[statusValue]
}

function getStatusTone(statusValue: ComplaintStatus) {
  return statusTone[statusValue]
}

function getPriorityText(priorityValue: ComplaintPriority) {
  return priorityText[priorityValue]
}

function getPriorityTone(priorityValue: ComplaintPriority) {
  return priorityTone[priorityValue]
}
</script>

<style scoped>
.drawer-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
