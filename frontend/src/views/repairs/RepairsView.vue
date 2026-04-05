<template>
  <PageContainer title="报修工单调度" description="完整的接单、派单、维修与回访闭环流程，支持人工录单和状态推进。">
    <template #actions>
      <el-button type="primary" class="btn-primary-gradient" @click="ticketDialogVisible = true">录入人工工单</el-button>
    </template>

    <div class="kanban-grid">
      <PanelCard v-for="column in columns" :key="column.key" class="kanban-column">
        <template #header>
          <div class="kanban-column__header">
            <div>
              <span class="kanban-column__dot" :style="{ background: column.color }"></span>
              <strong>{{ column.title }}</strong>
            </div>
            <span class="kanban-column__count">{{ column.items.length }}</span>
          </div>
        </template>

        <article v-for="ticket in column.items" :key="ticket.id" class="kanban-ticket glass-card">
          <div class="kanban-ticket__top">
            <span class="ticket-id">{{ ticket.id }}</span>
            <span class="ticket-age" :class="{ warning: column.key === 'todo' }">{{ ticket.ageLabel }}</span>
          </div>
          <h3>{{ ticket.title }}</h3>
          <p>位置: {{ ticket.location }}</p>
          <footer>
            <span>跟进人: {{ ticket.assignee ?? ticket.reporter }}</span>
            <el-button v-if="column.key === 'todo'" plain size="small" @click="openAssignDialog(ticket.id)">立即派单</el-button>
            <el-button v-else-if="column.key === 'doing'" link type="primary" @click="completeTicket(ticket.id)">完成维修</el-button>
            <el-button v-else link type="info" @click="openDetail(ticket.id)">回访记录</el-button>
          </footer>
        </article>
      </PanelCard>
    </div>

    <el-dialog v-model="ticketDialogVisible" title="录入人工工单" width="520px">
      <el-form label-position="top" class="dialog-form">
        <el-form-item label="报修主题">
          <el-input v-model="ticketDraft.title" />
        </el-form-item>
        <el-form-item label="报修位置">
          <el-input v-model="ticketDraft.location" />
        </el-form-item>
        <el-form-item label="报修人">
          <el-input v-model="ticketDraft.reporter" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="ticketDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="createTicket">提交</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="assignDialogVisible" title="派单给维修人员" width="420px">
      <el-form label-position="top" class="dialog-form">
        <el-form-item label="维修人员">
          <el-input v-model="assignDraft.assignee" placeholder="例如：赵工" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="assignDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="assignTicket">确认派单</el-button>
      </template>
    </el-dialog>

    <el-drawer v-model="detailVisible" title="工单回访记录" size="420px">
      <InfoList v-if="activeTicket" :items="detailItems" />
    </el-drawer>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import InfoList from '@/components/InfoList.vue'
import PageContainer from '@/components/PageContainer.vue'
import PanelCard from '@/components/PanelCard.vue'
import { repairTickets } from '@/mock/data'
import type { RepairTicket } from '@/types'
import { createLocalId } from '@/utils/format'

const ticketDialogVisible = ref(false)
const assignDialogVisible = ref(false)
const detailVisible = ref(false)
const selectedId = ref('')
const tickets = ref<RepairTicket[]>(repairTickets.map((item) => ({ ...item })))

const ticketDraft = reactive({
  title: '',
  location: '',
  reporter: '',
})

const assignDraft = reactive({
  assignee: '赵工',
})

const columns = computed(() => [
  { key: 'todo', title: '待处理 (接单)', color: '#f97316', items: tickets.value.filter((item) => item.stage === 'todo') },
  { key: 'doing', title: '处理中 (派单维修)', color: '#3b82f6', items: tickets.value.filter((item) => item.stage === 'doing') },
  { key: 'done', title: '已办结 (回访评价)', color: '#10b981', items: tickets.value.filter((item) => item.stage === 'done') },
])

const activeTicket = computed(() => tickets.value.find((item) => item.id === selectedId.value) ?? null)

const detailItems = computed(() =>
  activeTicket.value
    ? [
        { label: '工单编号', value: activeTicket.value.id },
        { label: '报修主题', value: activeTicket.value.title },
        { label: '报修位置', value: activeTicket.value.location },
        { label: '报修人', value: activeTicket.value.reporter },
        { label: '维修人', value: activeTicket.value.assignee ?? '-' },
        { label: '当前阶段', value: activeTicket.value.stage === 'done' ? '已办结' : '处理中' },
      ]
    : [],
)

function openAssignDialog(ticketId: string) {
  selectedId.value = ticketId
  assignDraft.assignee = '赵工'
  assignDialogVisible.value = true
}

function assignTicket() {
  tickets.value = tickets.value.map((item) =>
    item.id === selectedId.value
      ? { ...item, assignee: assignDraft.assignee.trim() || '赵工', stage: 'doing', ageLabel: '已派单' }
      : item,
  )
  assignDialogVisible.value = false
  ElMessage.success('工单已派发给维修工程部')
}

function completeTicket(ticketId: string) {
  tickets.value = tickets.value.map((item) =>
    item.id === ticketId ? { ...item, stage: 'done', ageLabel: '已办结' } : item,
  )
  ElMessage.success('工单已更新为完成待回访')
}

function openDetail(ticketId: string) {
  selectedId.value = ticketId
  detailVisible.value = true
}

function createTicket() {
  if (!ticketDraft.title.trim() || !ticketDraft.location.trim()) {
    ElMessage.warning('请补充报修主题和位置')
    return
  }

  tickets.value = [
    {
      id: createLocalId('REP'),
      title: ticketDraft.title,
      location: ticketDraft.location,
      reporter: ticketDraft.reporter || '前台客服',
      ageLabel: '刚刚创建',
      stage: 'todo',
    },
    ...tickets.value,
  ]

  ticketDialogVisible.value = false
  ticketDraft.title = ''
  ticketDraft.location = ''
  ticketDraft.reporter = ''
  ElMessage.success('人工工单已录入')
}
</script>

<style scoped>
.kanban-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 20px;
}

.kanban-column {
  min-height: 640px;
}

.kanban-column__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.kanban-column__header > div {
  display: flex;
  align-items: center;
  gap: 10px;
}

.kanban-column__dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
}

.kanban-column__count {
  display: grid;
  place-items: center;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  color: #5c6c84;
  background: #f3f7fc;
}

.kanban-ticket {
  padding: 16px;
  margin-bottom: 16px;
}

.kanban-ticket__top,
.kanban-ticket footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.kanban-ticket h3 {
  margin: 14px 0 10px;
  font-size: 24px;
  color: #22304a;
}

.kanban-ticket p {
  margin: 0 0 16px;
  color: #8192ab;
}

.ticket-id {
  padding: 8px 12px;
  border-radius: 12px;
  background: #eef4ff;
  color: #6b87b4;
}

.ticket-age {
  color: #8ea0b8;
}

.ticket-age.warning {
  color: #f97316;
}

@media (max-width: 1200px) {
  .kanban-grid {
    grid-template-columns: 1fr;
  }
}
</style>
