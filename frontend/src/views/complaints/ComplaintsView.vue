<template>
  <PageContainer title="投诉建议闭环" description="已接入投诉分页查询与处理反馈接口。">
    <template #actions>
      <el-button type="primary" class="btn-primary-gradient" @click="openFeedback()">快速处理</el-button>
    </template>

    <DataToolbar
      v-model:keyword="keyword"
      v-model:status="status"
      placeholder="搜索投诉分类"
      select-placeholder="筛选处理状态"
      :filters="filters"
    >
      <el-button @click="handleReset">重置</el-button>
      <el-button type="primary" @click="handleSearch">查询</el-button>
    </DataToolbar>

    <PanelCard title="投诉列表">
      <el-table v-loading="loading" :data="complaints">
        <el-table-column prop="complaintNo" label="投诉编号" min-width="150" />
        <el-table-column prop="category" label="投诉分类" min-width="140" />
        <el-table-column prop="content" label="投诉内容" min-width="240" show-overflow-tooltip />
        <el-table-column prop="source" label="来源" min-width="120" />
        <el-table-column prop="createTime" label="提交时间" min-width="170" />
        <el-table-column label="状态" min-width="120">
          <template #default="{ row }">
            <StatusBadge :label="row.statusText || getStatusText(row.status)" :tone="getStatusTone(row.status)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button link type="primary" @click="openDetail(row)">详情</el-button>
            <el-button link type="primary" @click="openFeedback(row)">处理</el-button>
          </template>
        </el-table-column>
      </el-table>
    </PanelCard>

    <el-drawer v-model="feedbackDrawer" title="投诉处理" size="420px">
      <el-form label-position="top" class="dialog-form">
        <el-form-item label="处理结果">
          <el-select v-model="feedback.status">
            <el-option label="处理中" :value="1" />
            <el-option label="已办结" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理说明">
          <el-input v-model="feedback.note" type="textarea" rows="6" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="drawer-footer">
          <el-button @click="feedbackDrawer = false">取消</el-button>
          <el-button type="primary" :loading="submitting" @click="submitFeedback">保存处理结果</el-button>
        </div>
      </template>
    </el-drawer>

    <el-drawer v-model="detailVisible" title="投诉详情" size="420px">
      <InfoList v-if="activeComplaint" :items="detailItems" />
    </el-drawer>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import DataToolbar from '@/components/DataToolbar.vue'
import InfoList from '@/components/InfoList.vue'
import PageContainer from '@/components/PageContainer.vue'
import PanelCard from '@/components/PanelCard.vue'
import StatusBadge from '@/components/StatusBadge.vue'
import { complaintApi } from '@/services/api'
import type { ComplaintRecord } from '@/types'

const loading = ref(false)
const submitting = ref(false)
const keyword = ref('')
const status = ref('')
const feedbackDrawer = ref(false)
const detailVisible = ref(false)
const complaints = ref<ComplaintRecord[]>([])
const activeComplaint = ref<ComplaintRecord | null>(null)

const filters = [
  { label: '待处理', value: '0' },
  { label: '处理中', value: '1' },
  { label: '已办结', value: '2' },
]

const feedback = reactive({
  status: 1,
  note: '',
})

const detailItems = computed(() =>
  activeComplaint.value
    ? [
        { label: '投诉编号', value: activeComplaint.value.complaintNo },
        { label: '投诉分类', value: activeComplaint.value.category },
        { label: '投诉来源', value: activeComplaint.value.source },
        { label: '投诉内容', value: activeComplaint.value.content },
        { label: '处理结果', value: activeComplaint.value.handleResult || '-' },
        { label: '状态', value: activeComplaint.value.statusText || getStatusText(activeComplaint.value.status) },
        { label: '提交时间', value: activeComplaint.value.createTime || '-' },
      ]
    : [],
)

onMounted(() => {
  void loadComplaints()
})

async function loadComplaints() {
  loading.value = true
  try {
    const result = await complaintApi.getList({
      page: 1,
      pageSize: 20,
      category: keyword.value.trim() || undefined,
      status: status.value ? Number(status.value) : undefined,
    })
    complaints.value = result.records
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '加载投诉数据失败')
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  void loadComplaints()
}

function handleReset() {
  keyword.value = ''
  status.value = ''
  void loadComplaints()
}

function openFeedback(complaint?: ComplaintRecord) {
  activeComplaint.value = complaint || complaints.value[0] || null
  feedback.status = activeComplaint.value?.status === 2 ? 2 : 1
  feedback.note = activeComplaint.value?.handleResult || ''
  feedbackDrawer.value = true
}

function openDetail(complaint: ComplaintRecord) {
  activeComplaint.value = complaint
  detailVisible.value = true
}

async function submitFeedback() {
  if (!activeComplaint.value) {
    return
  }

  submitting.value = true
  try {
    await complaintApi.handle({
      id: activeComplaint.value.id,
      status: feedback.status,
      handleResult: feedback.note.trim() || '已处理',
    })
    ElMessage.success('投诉处理结果已保存')
    feedbackDrawer.value = false
    await loadComplaints()
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '处理投诉失败')
  } finally {
    submitting.value = false
  }
}

function getStatusText(value: number) {
  if (value === 2) {
    return '已办结'
  }

  if (value === 1) {
    return '处理中'
  }

  return '待处理'
}

function getStatusTone(value: number) {
  if (value === 2) {
    return 'success'
  }

  if (value === 1) {
    return 'info'
  }

  return 'warning'
}
</script>

<style scoped>
.drawer-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
