<template>
  <PageContainer title="合同管理" description="管理租户合同、租期、租金和合同状态。">
    <template #actions>
      <el-button type="warning" class="btn-warning-gradient" @click="openApproval">资产申请审批</el-button>
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
        <el-table-column prop="createTime" label="创建时间" min-width="160" />
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

    <!-- 资产申请审批弹窗 -->
    <el-dialog v-model="approvalVisible" title="资产申请审批（购买/租赁）" width="900px" @closed="loadContracts">
      <div style="margin-bottom: 12px; display: flex; gap: 8px; align-items: center">
        <span style="font-size: 13px; color: var(--el-text-color-secondary)">申请类型：</span>
        <el-select v-model="approvalQuery.type" placeholder="全部" clearable style="width: 120px" @change="handleApprovalSearch">
          <el-option label="购买" value="PURCHASE" />
          <el-option label="租赁" value="RENTAL" />
        </el-select>
        <el-button @click="handleApprovalSearch">查询</el-button>
      </div>
      <el-table v-loading="approvalLoading" :data="applications">
        <el-table-column prop="applicationNo" label="申请编号" width="180" />
        <el-table-column label="申请类型" width="90">
          <template #default="{ row }">
            <StatusBadge :label="getApplicationTypeText(row.type)" :tone="row.type === 'PURCHASE' ? 'info' : 'warning'" />
          </template>
        </el-table-column>
        <el-table-column prop="applicantName" label="申请人" min-width="100" />
        <el-table-column prop="houseName" label="资产名称" min-width="140">
          <template #default="{ row }">{{ row.houseName || `资产ID: ${row.houseId}` }}</template>
        </el-table-column>
        <el-table-column prop="applicantPhone" label="联系电话" min-width="120" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <StatusBadge :label="getApplicationStatusText(row.status)" :tone="getApplicationStatusTone(row.status)" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="申请时间" width="160" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button v-if="row.status === 0" link type="primary" @click="openApproveForm(row)">审批</el-button>
            <span v-else style="color: var(--el-text-color-secondary); font-size: 13px">已处理</span>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          background
          layout="total, prev, pager, next"
          :current-page="approvalQuery.page"
          :page-size="approvalQuery.pageSize"
          :total="approvalTotal"
          @current-change="handleApprovalPageChange"
        />
      </div>
    </el-dialog>

    <!-- 审批表单弹窗 -->
    <el-dialog v-model="approveFormVisible" :title="`审批申请 - ${approvingApp?.applicationNo || ''}`" width="500px">
      <el-form label-position="top" class="dialog-form">
        <div v-if="approvingApp" style="margin-bottom: 16px; padding: 8px 12px; background: var(--el-color-info-light-9); border-radius: 6px; font-size: 13px">
          申请类型：<strong>{{ getApplicationTypeText(approvingApp.type) }}</strong>
        </div>
        <el-form-item label="审批结果">
          <el-radio-group v-model="approveDraft.approved">
            <el-radio :value="true">通过</el-radio>
            <el-radio :value="false">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>

        <template v-if="approveDraft.approved">
          <!-- 购买申请：只需定价 -->
          <template v-if="approvingApp?.type === 'PURCHASE'">
            <el-form-item label="出售价格（元）" required>
              <el-input-number v-model="approveDraft.proposedPrice" :min="0" :precision="2" class="full-width" />
            </el-form-item>
          </template>
          <!-- 租赁申请：需要租金、日期等 -->
          <template v-else>
            <el-form-item label="租金（元/月）" required>
              <el-input-number v-model="approveDraft.proposedPrice" :min="0" :precision="2" class="full-width" />
            </el-form-item>
            <el-form-item label="合同开始日期" required>
              <el-date-picker v-model="approveDraft.startDate" type="date" value-format="YYYY-MM-DD" class="full-width" />
            </el-form-item>
            <el-form-item label="合同结束日期" required>
              <el-date-picker v-model="approveDraft.endDate" type="date" value-format="YYYY-MM-DD" class="full-width" />
            </el-form-item>
            <el-form-item label="押金">
              <el-input-number v-model="approveDraft.deposit" :min="0" :precision="2" class="full-width" />
            </el-form-item>
            <el-form-item label="递增比例（%）">
              <el-input-number v-model="approveDraft.increaseRate" :min="0" :precision="2" class="full-width" />
            </el-form-item>
          </template>
        </template>

        <template v-else>
          <el-form-item label="拒绝理由" required>
            <el-input v-model="approveDraft.remark" type="textarea" :rows="3" placeholder="请填写拒绝理由" />
          </el-form-item>
        </template>
      </el-form>
      <template #footer>
        <el-button @click="approveFormVisible = false">取消</el-button>
        <el-button type="primary" :loading="approveSubmitting" @click="submitApproval">确认审批</el-button>
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
import { contractApi, purchaseApi } from '@/services/api'
import { formatCurrency, formatDateRange } from '@/utils/format'
import type { ContractRecord, PurchaseApplicationRecord, PurchaseApprovalPayload } from '@/types'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const detailVisible = ref(false)
const keyword = ref('')
const status = ref('')
const total = ref(0)
const contracts = ref<ContractRecord[]>([])
const activeContract = ref<ContractRecord | null>(null)

// 购买申请审批相关状态
const approvalVisible = ref(false)
const approvalLoading = ref(false)
const approveFormVisible = ref(false)
const approveSubmitting = ref(false)
const applications = ref<PurchaseApplicationRecord[]>([])
const approvalTotal = ref(0)
const approvingApp = ref<PurchaseApplicationRecord | null>(null)

const approvalQuery = reactive({
  page: 1,
  pageSize: 10,
  type: '' as string,
})

const approveDraft = reactive({
  id: undefined as number | undefined,
  approved: true as boolean,
  proposedPrice: 0,
  startDate: '',
  endDate: '',
  deposit: 0,
  increaseRate: 0,
  remark: '',
})

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

// ---- 购买申请审批 ----
async function openApproval() {
  approvalVisible.value = true
  approvalQuery.page = 1
  await loadApplications()
}

async function loadApplications() {
  approvalLoading.value = true
  try {
    const result = await purchaseApi.getList({
      page: approvalQuery.page,
      pageSize: approvalQuery.pageSize,
      type: approvalQuery.type || undefined,
    })
    applications.value = result.records
    approvalTotal.value = result.total
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '加载申请列表失败')
  } finally {
    approvalLoading.value = false
  }
}

function handleApprovalSearch() {
  approvalQuery.page = 1
  void loadApplications()
}

function handleApprovalPageChange(page: number) {
  approvalQuery.page = page
  void loadApplications()
}

function openApproveForm(app: PurchaseApplicationRecord) {
  approvingApp.value = app
  approveDraft.id = app.id
  approveDraft.approved = true
  approveDraft.proposedPrice = 0
  approveDraft.startDate = ''
  approveDraft.endDate = ''
  approveDraft.deposit = 0
  approveDraft.increaseRate = 0
  approveDraft.remark = ''
  approveFormVisible.value = true
}

async function submitApproval() {
  if (!approveDraft.id) return

  const isPurchase = approvingApp.value?.type === 'PURCHASE'

  if (approveDraft.approved) {
    if (!approveDraft.proposedPrice || approveDraft.proposedPrice <= 0) {
      ElMessage.warning(isPurchase ? '请输入出售价格' : '请输入租金')
      return
    }
    if (!isPurchase) {
      if (!approveDraft.startDate || !approveDraft.endDate) {
        ElMessage.warning('请选择合同起止日期')
        return
      }
    }
  } else {
    if (!approveDraft.remark.trim()) {
      ElMessage.warning('请填写拒绝理由')
      return
    }
  }

  approveSubmitting.value = true
  try {
    const payload: PurchaseApprovalPayload = {
      id: approveDraft.id,
      approved: approveDraft.approved,
    }
    if (approveDraft.approved) {
      payload.proposedPrice = approveDraft.proposedPrice
      if (!isPurchase) {
        payload.startDate = approveDraft.startDate
        payload.endDate = approveDraft.endDate
        payload.deposit = approveDraft.deposit || undefined
        payload.increaseRate = approveDraft.increaseRate || undefined
      }
    } else {
      payload.remark = approveDraft.remark.trim()
    }
    await purchaseApi.approve(payload)
    ElMessage.success(approveDraft.approved
      ? (isPurchase ? '审批通过，资产所有权已转移' : '审批通过，合同已生成')
      : '已拒绝申请')
    approveFormVisible.value = false
    await loadApplications()
    await loadContracts()
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '审批操作失败')
  } finally {
    approveSubmitting.value = false
  }
}

function getApplicationStatusText(status: number) {
  if (status === 0) return '待审批'
  if (status === 1) return '已通过'
  if (status === 2) return '已拒绝'
  return '未知'
}

function getApplicationStatusTone(status: number) {
  if (status === 0) return 'warning'
  if (status === 1) return 'success'
  if (status === 2) return 'danger'
  return 'info'
}

function getApplicationTypeText(type?: string | null) {
  if (type === 'PURCHASE') return '购买'
  if (type === 'RENTAL') return '租赁'
  return '购买'
}
</script>

<style scoped>
.pagination-wrap {
  display: flex;
  justify-content: flex-end;
  margin-top: 24px;
}

.full-width {
  width: 100%;
}
</style>
