<template>
  <PageContainer title="报修工单调度" description="已接入报修看板、新建工单、派单和完工接口。">
    <template #actions>
      <el-button type="primary" class="btn-primary-gradient" @click="ticketDialogVisible = true">录入人工工单</el-button>
    </template>

    <div class="kanban-grid" v-loading="loading">
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
            <span class="ticket-id">{{ ticket.repairNo }}</span>
            <span class="ticket-age">{{ ticket.priorityText || `优先级 ${ticket.priority}` }}</span>
          </div>
          <h3>{{ ticket.content }}</h3>
          <p>位置: {{ ticket.houseName || `房屋ID: ${ticket.houseId}` }}</p>
          <footer>
            <span>跟进人: {{ ticket.workerName || ticket.reporter }}</span>
            <el-button v-if="column.key === 'pending'" plain size="small" @click="openAssignDialog(ticket.id)">立即派单</el-button>
            <el-button v-else-if="column.key === 'processing'" link type="primary" @click="completeTicket(ticket.id)">完成维修</el-button>
            <el-button v-else link type="info" @click="openDetail(ticket)">回访记录</el-button>
          </footer>
        </article>
      </PanelCard>
    </div>

    <el-dialog v-model="ticketDialogVisible" title="录入人工工单" width="520px">
      <el-form label-position="top" class="dialog-form">
        <el-form-item label="房屋 ID">
          <el-input-number v-model="ticketDraft.houseId" :min="1" :precision="0" class="full-width" />
        </el-form-item>
        <el-form-item label="报修内容">
          <el-input v-model="ticketDraft.content" type="textarea" rows="4" />
        </el-form-item>
        <el-form-item label="报修人">
          <el-input v-model="ticketDraft.reporter" />
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="ticketDraft.priority">
            <el-option label="普通" :value="1" />
            <el-option label="紧急" :value="2" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="ticketDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="createTicket">提交</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="assignDialogVisible" title="派单给维修人员" width="420px">
      <el-form label-position="top" class="dialog-form">
        <el-form-item label="维修人员 ID">
          <el-input-number v-model="assignDraft.workerId" :min="1" :precision="0" class="full-width" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="assignDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="assignTicket">确认派单</el-button>
      </template>
    </el-dialog>

    <el-drawer v-model="detailVisible" title="工单详情" size="420px">
      <InfoList v-if="activeTicket" :items="detailItems" />
    </el-drawer>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import InfoList from '@/components/InfoList.vue'
import PageContainer from '@/components/PageContainer.vue'
import PanelCard from '@/components/PanelCard.vue'
import { repairApi } from '@/services/api'
import type { RepairRecord } from '@/types'

const loading = ref(false)
const submitting = ref(false)
const ticketDialogVisible = ref(false)
const assignDialogVisible = ref(false)
const detailVisible = ref(false)
const selectedId = ref<number | null>(null)
const kanban = reactive({
  pending: [] as RepairRecord[],
  processing: [] as RepairRecord[],
  completed: [] as RepairRecord[],
})
const activeTicket = ref<RepairRecord | null>(null)

const ticketDraft = reactive({
  houseId: 1,
  content: '',
  reporter: '',
  priority: 1,
})

const assignDraft = reactive({
  workerId: 1,
})

const columns = computed(() => [
  { key: 'pending', title: '待处理', color: '#f97316', items: kanban.pending },
  { key: 'processing', title: '处理中', color: '#3b82f6', items: kanban.processing },
  { key: 'completed', title: '已完成', color: '#10b981', items: kanban.completed },
])

const detailItems = computed(() =>
  activeTicket.value
    ? [
        { label: '工单编号', value: activeTicket.value.repairNo },
        { label: '房屋 ID', value: String(activeTicket.value.houseId) },
        { label: '资产名称', value: activeTicket.value.houseName || '-' },
        { label: '报修内容', value: activeTicket.value.content },
        { label: '报修人', value: activeTicket.value.reporter },
        { label: '维修人', value: activeTicket.value.workerName || '-' },
        { label: '状态', value: activeTicket.value.statusText || String(activeTicket.value.status) },
        { label: '创建时间', value: activeTicket.value.createTime || '-' },
        { label: '完成时间', value: activeTicket.value.finishTime || '-' },
      ]
    : [],
)

onMounted(() => {
  void loadKanban()
})

async function loadKanban() {
  loading.value = true
  try {
    const result = await repairApi.getKanban()
    kanban.pending = result.pending
    kanban.processing = result.processing
    kanban.completed = result.completed
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '加载报修看板失败')
  } finally {
    loading.value = false
  }
}

function openAssignDialog(id: number) {
  selectedId.value = id
  assignDraft.workerId = 1
  assignDialogVisible.value = true
}

async function assignTicket() {
  if (!selectedId.value) {
    return
  }

  try {
    await repairApi.dispatch({
      id: selectedId.value,
      workerId: assignDraft.workerId,
    })
    ElMessage.success('工单已派发')
    assignDialogVisible.value = false
    await loadKanban()
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '派单失败')
  }
}

async function completeTicket(id: number) {
  try {
    await repairApi.updateStatus({
      id,
      status: 2,
    })
    ElMessage.success('工单已更新为完成')
    await loadKanban()
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '更新状态失败')
  }
}

function openDetail(ticket: RepairRecord) {
  activeTicket.value = ticket
  detailVisible.value = true
}

async function createTicket() {
  if (!ticketDraft.content.trim()) {
    ElMessage.warning('请输入报修内容')
    return
  }

  submitting.value = true
  try {
    await repairApi.create({
      houseId: ticketDraft.houseId,
      content: ticketDraft.content.trim(),
      reporter: ticketDraft.reporter.trim() || '前台客服',
      priority: ticketDraft.priority,
    })
    ElMessage.success('人工工单已录入')
    ticketDialogVisible.value = false
    ticketDraft.houseId = 1
    ticketDraft.content = ''
    ticketDraft.reporter = ''
    ticketDraft.priority = 1
    await loadKanban()
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '录入工单失败')
  } finally {
    submitting.value = false
  }
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

.full-width {
  width: 100%;
}

@media (max-width: 1200px) {
  .kanban-grid {
    grid-template-columns: 1fr;
  }
}
</style>
