<template>
  <PageContainer title="账单管理" description="按费用类型生成账单，查看账单明细。">
    <template #actions>
      <el-button type="primary" class="btn-primary-gradient" @click="dialogVisible = true">生成账单</el-button>
      <el-button @click="showConfigDialog">物业费配置</el-button>
    </template>

    <div class="billing-summary">
      <article class="surface-card billing-summary__card">
        <div class="billing-summary__icon">账</div>
        <div>
          <strong>{{ totalCount.toLocaleString() }}</strong>
          <span>账单总数</span>
        </div>
      </article>
      <article class="surface-card billing-summary__card">
        <div class="billing-summary__icon">¥</div>
        <div>
          <strong>{{ formatCurrency(totalAmount) }}</strong>
          <span>物业费单价</span>
        </div>
      </article>
    </div>

    <PanelCard title="账单明细">
      <DataToolbar
        v-model:status="billQuery.feeType"
        placeholder="搜索账单号"
        select-placeholder="筛选费用类型"
        :filters="feeTypeFilters"
      >
        <el-button @click="handleBillReset">重置</el-button>
        <el-button type="primary" @click="handleBillSearch">查询</el-button>
      </DataToolbar>
      <el-table v-loading="loadingBills" :data="bills">
        <el-table-column prop="billNo" label="账单号" min-width="180" />
        <el-table-column prop="houseName" label="资产名称" min-width="160" />
        <el-table-column label="费用类型" min-width="100">
          <template #default="{ row }">{{ row.feeTypeText || '-' }}</template>
        </el-table-column>
        <el-table-column prop="billMonth" label="账单月份" min-width="110" />
        <el-table-column label="金额" min-width="130">
          <template #default="{ row }">{{ formatCurrency(Number(row.amount)) }}</template>
        </el-table-column>
        <el-table-column prop="dueDate" label="截止日期" min-width="120" />
        <el-table-column label="状态" min-width="100">
          <template #default="{ row }">
            <StatusBadge :label="row.statusText || getStatusText(row.status)" :tone="getStatusTone(row.status)" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="160" />
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next"
          :current-page="billQuery.page"
          :page-size="billQuery.pageSize"
          :page-sizes="[10, 20, 50]"
          :total="billTotal"
          @current-change="handleBillPageChange"
          @size-change="handleBillSizeChange"
        />
      </div>
    </PanelCard>

    <!-- 生成账单弹窗 -->
    <el-dialog v-model="dialogVisible" title="生成账单" width="500px">
      <el-form label-position="top" class="dialog-form">
        <el-form-item label="费用类型" required>
          <el-select v-model="generateDraft.feeType" class="full-width">
            <el-option label="租金" :value="1" />
            <el-option label="买房金额" :value="2" />
            <el-option label="押金" :value="3" />
            <el-option label="物业费" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="账单月份">
          <el-input v-model="generateDraft.billMonth" placeholder="如 2026-07，留空则自动计算" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="generateDraft.remark" placeholder="可选备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="generating" @click="generateBills">确认生成</el-button>
      </template>
    </el-dialog>

    <!-- 物业费配置弹窗 -->
    <el-dialog v-model="configDialogVisible" title="物业费单价设置" width="400px">
      <el-form label-position="top" class="dialog-form">
        <el-form-item label="当前单价">
          <span style="font-size: 24px; font-weight: 700; color: var(--el-color-primary)">
            ¥{{ propertyFeeConfig?.unitPrice || '2.00' }}
          </span>
          <span style="color: var(--el-text-color-secondary); font-size: 13px; margin-left: 8px">/㎡/月</span>
        </el-form-item>
        <el-form-item label="新单价（下月生效）" required>
          <el-input-number v-model="configDraft.unitPrice" :min="0.01" :precision="2" class="full-width" />
          <span style="font-size: 12px; color: var(--el-text-color-secondary)">
            生效月份: {{ nextMonth }}
          </span>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="configDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="savingConfig" @click="savePropertyFeeConfig">保存</el-button>
      </template>
    </el-dialog>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import PageContainer from '@/components/PageContainer.vue'
import PanelCard from '@/components/PanelCard.vue'
import StatusBadge from '@/components/StatusBadge.vue'
import DataToolbar from '@/components/DataToolbar.vue'
import { financeApi } from '@/services/api'
import { formatCurrency } from '@/utils/format'
import type { BillRecord, PropertyFeeConfigVO } from '@/types'

const generating = ref(false)
const loadingBills = ref(false)
const dialogVisible = ref(false)
const configDialogVisible = ref(false)
const savingConfig = ref(false)
const billTotal = ref(0)
const bills = ref<BillRecord[]>([])
const propertyFeeConfig = ref<PropertyFeeConfigVO | null>(null)

const nextMonth = computed(() => {
  const d = new Date()
  d.setMonth(d.getMonth() + 1)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}`
})

const billQuery = reactive({
  page: 1,
  pageSize: 20,
  feeType: undefined as number | undefined,
})

const generateDraft = reactive({
  feeType: 4 as number,
  billMonth: '',
  remark: '',
})

const configDraft = reactive({
  unitPrice: 0,
})

const feeTypeFilters = [
  { label: '租金', value: '1' },
  { label: '买房金额', value: '2' },
  { label: '押金', value: '3' },
  { label: '物业费', value: '4' },
]

const totalCount = computed(() => billTotal.value.toLocaleString('zh-CN'))
const totalAmount = computed(() => {
  if (propertyFeeConfig.value) return propertyFeeConfig.value.unitPrice
  return 2.00
})

onMounted(() => {
  void loadBills()
  void loadPropertyFeeConfig()
})

async function loadBills() {
  loadingBills.value = true
  try {
    const result = await financeApi.getBills({
      page: billQuery.page,
      pageSize: billQuery.pageSize,
      feeType: billQuery.feeType,
    })
    bills.value = result.records
    billTotal.value = result.total
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '加载账单失败')
  } finally {
    loadingBills.value = false
  }
}

async function loadPropertyFeeConfig() {
  try {
    propertyFeeConfig.value = await financeApi.getPropertyFeeConfig()
  } catch { /* ignore */ }
}

function handleBillPageChange(page: number) { billQuery.page = page; void loadBills() }
function handleBillSizeChange(size: number) { billQuery.page = 1; billQuery.pageSize = size; void loadBills() }
function handleBillSearch() { billQuery.page = 1; void loadBills() }
function handleBillReset() { billQuery.feeType = undefined; billQuery.page = 1; void loadBills() }

async function generateBills() {
  if (!generateDraft.feeType) { ElMessage.warning('请选择费用类型'); return }
  generating.value = true
  try {
    await financeApi.generateBills({
      feeType: generateDraft.feeType,
      billMonth: generateDraft.billMonth.trim() || undefined,
      remark: generateDraft.remark.trim() || undefined,
    })
    ElMessage.success('账单生成成功')
    dialogVisible.value = false
    await loadBills()
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '生成账单失败')
  } finally {
    generating.value = false
  }
}

function showConfigDialog() {
  configDraft.unitPrice = propertyFeeConfig.value?.unitPrice || 2.00
  configDialogVisible.value = true
}

async function savePropertyFeeConfig() {
  if (!configDraft.unitPrice || configDraft.unitPrice <= 0) {
    ElMessage.warning('请输入有效单价')
    return
  }
  savingConfig.value = true
  try {
    await financeApi.setPropertyFeeConfig({ unitPrice: configDraft.unitPrice })
    ElMessage.success(`物业费单价已设为 ¥${configDraft.unitPrice}，${nextMonth.value} 起生效`)
    configDialogVisible.value = false
    await loadPropertyFeeConfig()
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '设置失败')
  } finally {
    savingConfig.value = false
  }
}

function getStatusText(status: number) {
  const map: Record<number, string> = { 0: '待缴费', 1: '待核销', 2: '已缴费', 3: '已撤销', 4: '已作废' }
  return map[status] || '未知'
}

function getStatusTone(status: number) {
  const map: Record<number, string> = { 0: 'warning', 1: 'info', 2: 'success', 3: 'danger', 4: 'info' }
  return map[status] || 'info'
}
</script>

<style scoped>
.billing-summary {
  display: flex;
  gap: 16px;
  margin-bottom: 32px;
}
.billing-summary__card {
  display: flex;
  align-items: center;
  gap: 20px;
  min-width: 280px;
}
.billing-summary__card strong {
  display: block;
  font-size: 40px;
  font-weight: 700;
  color: var(--text-heading);
}
.billing-summary__card span {
  color: var(--text-subtle);
  font-size: 14px;
}
.billing-summary__icon {
  display: grid;
  place-items: center;
  width: 56px;
  height: 56px;
  border-radius: 14px;
  background: #f5f5f5;
  color: #1a1a1a;
  font-size: 24px;
  font-weight: 600;
}
.pagination-wrap {
  display: flex;
  justify-content: flex-end;
  margin-top: 24px;
}
.full-width { width: 100%; }
</style>
