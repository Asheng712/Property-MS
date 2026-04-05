<template>
  <PageContainer title="商业合同管理" description="统一维护租赁合同、续签进度与到期提醒，支持草稿编辑与详情查看。">
    <template #actions>
      <el-button type="primary" class="btn-primary-gradient" @click="openCreate">新增合同</el-button>
    </template>

    <DataToolbar v-model:keyword="keyword" v-model:status="status" placeholder="搜索合同编号、租户..." :filters="filters" />

    <PanelCard title="合同列表">
      <el-table :data="filteredContracts">
        <el-table-column prop="contractNo" label="合同编号" min-width="150" />
        <el-table-column prop="tenant" label="租户名称" min-width="140" />
        <el-table-column prop="asset" label="关联资产" min-width="120" />
        <el-table-column prop="period" label="合同周期" min-width="220" />
        <el-table-column label="合同金额" min-width="140">
          <template #default="{ row }">{{ formatCurrency(row.amount) }}</template>
        </el-table-column>
        <el-table-column label="状态" min-width="120">
          <template #default="{ row }">
            <StatusBadge :label="getStatusText(row.status)" :tone="getStatusTone(row.status)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-button link type="primary" @click="openDetail(row.id)">详情</el-button>
            <el-button link type="primary" @click="renewContract(row.id)">续签</el-button>
          </template>
        </el-table-column>
      </el-table>
    </PanelCard>

    <el-dialog v-model="dialogVisible" :title="editingId ? '编辑合同' : '新增商业合同'" width="520px">
      <el-form label-position="top" class="dialog-form">
        <el-form-item label="合同编号">
          <el-input v-model="draft.contractNo" />
        </el-form-item>
        <el-form-item label="租户名称">
          <el-input v-model="draft.tenant" />
        </el-form-item>
        <el-form-item label="关联资产">
          <el-input v-model="draft.asset" />
        </el-form-item>
        <el-form-item label="合同金额">
          <el-input v-model.number="draft.amount" />
        </el-form-item>
        <el-form-item label="合同状态">
          <el-select v-model="draft.status">
            <el-option label="生效中" value="active" />
            <el-option label="即将到期" value="expiring" />
            <el-option label="草稿" value="draft" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveContract">保存</el-button>
      </template>
    </el-dialog>

    <el-drawer v-model="detailVisible" title="合同详情" size="420px">
      <InfoList v-if="activeContract" :items="detailItems" />
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
import { contractRecords } from '@/mock/data'
import type { ContractRecord } from '@/types'
import { createLocalId, formatCurrency } from '@/utils/format'

type ContractStatus = ContractRecord['status']

const keyword = ref('')
const status = ref('')
const dialogVisible = ref(false)
const detailVisible = ref(false)
const editingId = ref('')
const activeId = ref('')
const contracts = ref<ContractRecord[]>(contractRecords.map((item) => ({ ...item })))

const filters = [
  { label: '生效中', value: 'active' },
  { label: '即将到期', value: 'expiring' },
  { label: '草稿', value: 'draft' },
]

const statusText: Record<ContractStatus, string> = {
  active: '生效中',
  expiring: '即将到期',
  draft: '草稿',
}

const statusTone: Record<ContractStatus, 'success' | 'warning' | 'info'> = {
  active: 'success',
  expiring: 'warning',
  draft: 'info',
}

const draft = reactive({
  contractNo: '',
  tenant: '',
  asset: '',
  amount: 0,
  status: 'draft' as ContractStatus,
})

const filteredContracts = computed(() =>
  contracts.value.filter((item) => {
    const matchesKeyword = !keyword.value || `${item.contractNo}${item.tenant}${item.asset}`.includes(keyword.value)
    const matchesStatus = !status.value || item.status === status.value
    return matchesKeyword && matchesStatus
  }),
)

const activeContract = computed(() => contracts.value.find((item) => item.id === activeId.value) ?? null)

const detailItems = computed(() =>
  activeContract.value
    ? [
        { label: '合同编号', value: activeContract.value.contractNo },
        { label: '租户名称', value: activeContract.value.tenant },
        { label: '关联资产', value: activeContract.value.asset },
        { label: '合同周期', value: activeContract.value.period },
        { label: '合同金额', value: formatCurrency(activeContract.value.amount) },
        { label: '当前状态', value: getStatusText(activeContract.value.status) },
      ]
    : [],
)

function openCreate() {
  editingId.value = ''
  resetDraft()
  dialogVisible.value = true
}

function openDetail(contractId: string) {
  activeId.value = contractId
  detailVisible.value = true
}

function getStatusText(statusValue: ContractStatus) {
  return statusText[statusValue]
}

function getStatusTone(statusValue: ContractStatus) {
  return statusTone[statusValue]
}

function renewContract(contractId: string) {
  contracts.value = contracts.value.map((item) =>
    item.id === contractId ? { ...item, status: 'active', period: '2026-01-01 至 2027-12-31' } : item,
  )
  ElMessage.success('合同已续签并更新周期')
}

function saveContract() {
  if (!draft.contractNo.trim() || !draft.tenant.trim()) {
    ElMessage.warning('请先填写合同编号和租户名称')
    return
  }

  const record: ContractRecord = {
    id: editingId.value || createLocalId('contract'),
    contractNo: draft.contractNo,
    tenant: draft.tenant,
    asset: draft.asset,
    period: draft.status === 'draft' ? '待审批' : '2026-01-01 至 2026-12-31',
    amount: draft.amount,
    status: draft.status,
  }

  contracts.value = editingId.value
    ? contracts.value.map((item) => (item.id === editingId.value ? record : item))
    : [record, ...contracts.value]

  dialogVisible.value = false
  resetDraft()
  ElMessage.success('合同信息已保存')
}

function resetDraft() {
  draft.contractNo = ''
  draft.tenant = ''
  draft.asset = ''
  draft.amount = 0
  draft.status = 'draft'
}
</script>
