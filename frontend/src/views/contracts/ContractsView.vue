<template>
  <PageContainer title="合同管理" description="已接入最新后端合同分页查询与新增/编辑接口。">
    <template #actions>
      <el-button type="primary" class="btn-primary-gradient" @click="openCreate">新增合同</el-button>
    </template>

    <DataToolbar
      v-model:keyword="keyword"
      v-model:status="status"
      placeholder="搜索租户名称"
      select-placeholder="筛选合同状态"
      :filters="filters"
    >
      <el-button @click="handleReset">重置</el-button>
      <el-button type="primary" @click="handleSearch">查询</el-button>
    </DataToolbar>

    <PanelCard title="合同列表">
      <el-table v-loading="loading" :data="contracts">
        <el-table-column prop="id" label="ID" width="90" />
        <el-table-column prop="tenantName" label="租户名称" min-width="160" />
        <el-table-column prop="houseName" label="关联资产" min-width="160">
          <template #default="{ row }">{{ row.houseName || `房屋ID: ${row.houseId}` }}</template>
        </el-table-column>
        <el-table-column label="合同周期" min-width="220">
          <template #default="{ row }">{{ formatDateRange(row.startDate, row.endDate) }}</template>
        </el-table-column>
        <el-table-column label="合同金额" min-width="150">
          <template #default="{ row }">{{ formatCurrency(Number(row.rentAmount)) }}</template>
        </el-table-column>
        <el-table-column label="状态" min-width="120">
          <template #default="{ row }">
            <StatusBadge :label="row.contractStatusText || getStatusText(row.contractStatus)" :tone="getStatusTone(row.contractStatus)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140">
          <template #default="{ row }">
            <el-button link type="primary" @click="openDetail(row)">详情</el-button>
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
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

    <el-dialog v-model="dialogVisible" :title="draft.id ? '编辑合同' : '新增合同'" width="560px">
      <el-form label-position="top" class="dialog-form">
        <el-form-item label="房屋 ID">
          <el-input-number v-model="draft.houseId" :min="1" :precision="0" class="full-width" />
        </el-form-item>
        <el-form-item label="租户名称">
          <el-input v-model="draft.tenantName" />
        </el-form-item>
        <el-form-item label="租金金额">
          <el-input-number v-model="draft.rentAmount" :min="0" :precision="2" class="full-width" />
        </el-form-item>
        <el-form-item label="开始日期">
          <el-date-picker v-model="draft.startDate" type="date" value-format="YYYY-MM-DD" class="full-width" />
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker v-model="draft.endDate" type="date" value-format="YYYY-MM-DD" class="full-width" />
        </el-form-item>
        <el-form-item label="递增比例">
          <el-input-number v-model="draft.increaseRate" :min="0" :precision="2" class="full-width" />
        </el-form-item>
        <el-form-item label="押金">
          <el-input-number v-model="draft.deposit" :min="0" :precision="2" class="full-width" />
        </el-form-item>
        <el-form-item label="合同状态">
          <el-select v-model="draft.contractStatus">
            <el-option label="有效" :value="1" />
            <el-option label="过期/终止" :value="0" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="saveContract">保存</el-button>
      </template>
    </el-dialog>

    <el-drawer v-model="detailVisible" title="合同详情" size="420px">
      <InfoList v-if="activeContract" :items="detailItems" />
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
import { contractApi } from '@/services/api'
import { formatCurrency, formatDateRange } from '@/utils/format'
import type { ContractRecord } from '@/types'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const detailVisible = ref(false)
const keyword = ref('')
const status = ref('')
const total = ref(0)
const contracts = ref<ContractRecord[]>([])
const activeContract = ref<ContractRecord | null>(null)

const query = reactive({
  page: 1,
  pageSize: 10,
})

const draft = reactive({
  id: undefined as number | undefined,
  houseId: 1,
  tenantName: '',
  rentAmount: 0,
  startDate: '',
  endDate: '',
  increaseRate: 0,
  deposit: 0,
  contractStatus: 1,
})

const filters = [
  { label: '有效', value: '1' },
  { label: '过期/终止', value: '0' },
]

const detailItems = computed(() =>
  activeContract.value
    ? [
        { label: '合同 ID', value: String(activeContract.value.id) },
        { label: '房屋 ID', value: String(activeContract.value.houseId) },
        { label: '租户名称', value: activeContract.value.tenantName },
        { label: '关联资产', value: activeContract.value.houseName || '-' },
        { label: '合同周期', value: formatDateRange(activeContract.value.startDate, activeContract.value.endDate) },
        { label: '租金金额', value: formatCurrency(Number(activeContract.value.rentAmount)) },
        { label: '押金', value: formatCurrency(Number(activeContract.value.deposit || 0)) },
        { label: '状态', value: activeContract.value.contractStatusText || getStatusText(activeContract.value.contractStatus) },
      ]
    : [],
)

onMounted(() => {
  void loadContracts()
})

async function loadContracts() {
  loading.value = true
  try {
    const result = await contractApi.getList({
      page: query.page,
      pageSize: query.pageSize,
      tenantName: keyword.value.trim() || undefined,
      contractStatus: status.value ? Number(status.value) : undefined,
    })

    contracts.value = result.records
    total.value = result.total
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '加载合同失败')
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  query.page = 1
  void loadContracts()
}

function handleReset() {
  keyword.value = ''
  status.value = ''
  query.page = 1
  void loadContracts()
}

function handlePageChange(page: number) {
  query.page = page
  void loadContracts()
}

function handleSizeChange(size: number) {
  query.page = 1
  query.pageSize = size
  void loadContracts()
}

function openCreate() {
  resetDraft()
  dialogVisible.value = true
}

function openEdit(contract: ContractRecord) {
  draft.id = contract.id
  draft.houseId = contract.houseId
  draft.tenantName = contract.tenantName
  draft.rentAmount = Number(contract.rentAmount)
  draft.startDate = contract.startDate
  draft.endDate = contract.endDate
  draft.increaseRate = Number(contract.increaseRate || 0)
  draft.deposit = Number(contract.deposit || 0)
  draft.contractStatus = contract.contractStatus
  dialogVisible.value = true
}

function openDetail(contract: ContractRecord) {
  activeContract.value = contract
  detailVisible.value = true
}

async function saveContract() {
  if (!draft.tenantName.trim()) {
    ElMessage.warning('请输入租户名称')
    return
  }

  submitting.value = true
  try {
    await contractApi.save({
      id: draft.id,
      houseId: draft.houseId,
      tenantName: draft.tenantName.trim(),
      rentAmount: draft.rentAmount,
      startDate: draft.startDate,
      endDate: draft.endDate,
      increaseRate: draft.increaseRate,
      deposit: draft.deposit,
      contractStatus: draft.contractStatus,
    })
    ElMessage.success(draft.id ? '合同更新成功' : '合同新增成功')
    dialogVisible.value = false
    resetDraft()
    await loadContracts()
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '保存合同失败')
  } finally {
    submitting.value = false
  }
}

function resetDraft() {
  draft.id = undefined
  draft.houseId = 1
  draft.tenantName = ''
  draft.rentAmount = 0
  draft.startDate = ''
  draft.endDate = ''
  draft.increaseRate = 0
  draft.deposit = 0
  draft.contractStatus = 1
}

function getStatusText(value: number) {
  return value === 1 ? '有效' : '过期/终止'
}

function getStatusTone(value: number) {
  return value === 1 ? 'success' : 'warning'
}
</script>

<style scoped>
.pagination-wrap {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

.full-width {
  width: 100%;
}
</style>
