<template>
  <PageContainer title="缴费核销" description="查看缴费记录，核销或撤销支付。">
    <DataToolbar
      v-model:keyword="keyword"
      v-model:status="status"
      placeholder="搜索支付记录编号"
      select-placeholder="筛选状态"
      :filters="filters"
    >
      <el-button @click="handleReset">重置</el-button>
      <el-button type="primary" @click="handleSearch">查询</el-button>
    </DataToolbar>

    <PanelCard title="支付记录">
      <el-table v-loading="loading" :data="payments">
        <el-table-column prop="paymentNo" label="支付编号" min-width="160" />
        <el-table-column prop="userName" label="缴费人" min-width="100" />
        <el-table-column label="金额" min-width="140">
          <template #default="{ row }">{{ formatCurrency(Number(row.amount)) }}</template>
        </el-table-column>
        <el-table-column label="支付方式" min-width="100">
          <template #default="{ row }">{{ row.payMethodText || '-' }}</template>
        </el-table-column>
        <el-table-column prop="payTime" label="支付时间" min-width="160" />
        <el-table-column label="状态" min-width="100">
          <template #default="{ row }">
            <StatusBadge :label="row.statusText || getStatusText(row.status)" :tone="getStatusTone(row.status)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="160">
          <template #default="{ row }">
            <el-button link type="primary" @click="inspectRecord(row)">详情</el-button>
            <el-button v-if="row.status === 0" link type="primary" @click="verifyPayment(row.id)">核销</el-button>
            <el-button v-if="row.status === 1" link type="danger" @click="showCancelDialog(row)">撤销</el-button>
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

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" title="支付详情" width="550px">
      <template v-if="activePayment">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="支付编号">{{ activePayment.paymentNo }}</el-descriptions-item>
          <el-descriptions-item label="缴费人">{{ activePayment.userName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="支付金额">{{ formatCurrency(activePayment.amount) }}</el-descriptions-item>
          <el-descriptions-item label="支付方式">{{ activePayment.payMethodText || '-' }}</el-descriptions-item>
          <el-descriptions-item label="支付时间">{{ activePayment.payTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ activePayment.statusText || '-' }}</el-descriptions-item>
          <el-descriptions-item label="凭证" :span="2">
            <a v-if="activePayment.voucherUrl" :href="activePayment.voucherUrl" target="_blank">查看凭证</a>
            <span v-else>-</span>
          </el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ activePayment.remark || '-' }}</el-descriptions-item>
          <el-descriptions-item v-if="activePayment.verifyUserName" label="核销人">{{ activePayment.verifyUserName }}</el-descriptions-item>
          <el-descriptions-item v-if="activePayment.verifyTime" label="核销时间">{{ activePayment.verifyTime }}</el-descriptions-item>
          <el-descriptions-item v-if="activePayment.cancelUserName" label="撤销人">{{ activePayment.cancelUserName }}</el-descriptions-item>
          <el-descriptions-item v-if="activePayment.cancelReason" label="撤销原因">{{ activePayment.cancelReason }}</el-descriptions-item>
        </el-descriptions>

        <h4 style="margin-top: 16px; margin-bottom: 8px">关联账单</h4>
        <el-table :data="activePayment.bills || []" size="small">
          <el-table-column prop="billNo" label="账单号" min-width="160" />
          <el-table-column prop="houseName" label="资产" min-width="120" />
          <el-table-column prop="feeTypeText" label="费用类型" min-width="80" />
          <el-table-column prop="billMonth" label="月份" min-width="80" />
          <el-table-column label="金额">
            <template #default="{ row }">{{ formatCurrency(row.amount) }}</template>
          </el-table-column>
        </el-table>
      </template>
    </el-dialog>

    <!-- 撤销弹窗 -->
    <el-dialog v-model="cancelVisible" title="撤销核销" width="400px">
      <el-form>
        <el-form-item label="撤销原因" required>
          <el-input v-model="cancelReason" type="textarea" :rows="3" placeholder="请填写撤销原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="cancelVisible = false">取消</el-button>
        <el-button type="danger" :loading="cancelling" @click="confirmCancel">确认撤销</el-button>
      </template>
    </el-dialog>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import DataToolbar from '@/components/DataToolbar.vue'
import PageContainer from '@/components/PageContainer.vue'
import PanelCard from '@/components/PanelCard.vue'
import StatusBadge from '@/components/StatusBadge.vue'
import { financeApi } from '@/services/api'
import { formatCurrency } from '@/utils/format'
import type { PaymentRecordVO } from '@/types'

const loading = ref(false)
const keyword = ref('')
const status = ref('')
const total = ref(0)
const detailVisible = ref(false)
const cancelVisible = ref(false)
const cancelling = ref(false)
const cancelReason = ref('')
const payments = ref<PaymentRecordVO[]>([])
const activePayment = ref<PaymentRecordVO | null>(null)
const cancelTargetId = ref<number | null>(null)

const query = reactive({ page: 1, pageSize: 20 })

const filters = [
  { label: '待核销', value: '0' },
  { label: '已核销', value: '1' },
  { label: '已驳回', value: '2' },
  { label: '已撤销', value: '3' },
]

onMounted(() => { void loadPayments() })

async function loadPayments() {
  loading.value = true
  try {
    const result = await financeApi.getPayments({
      page: query.page,
      pageSize: query.pageSize,
      paymentNo: keyword.value.trim() || undefined,
      status: status.value ? Number(status.value) : undefined,
    })
    payments.value = result.records
    total.value = result.total
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '加载失败')
  } finally { loading.value = false }
}

function handleSearch() { query.page = 1; void loadPayments() }
function handleReset() { keyword.value = ''; status.value = ''; query.page = 1; void loadPayments() }
function handlePageChange(page: number) { query.page = page; void loadPayments() }
function handleSizeChange(size: number) { query.page = 1; query.pageSize = size; void loadPayments() }

async function inspectRecord(payment: PaymentRecordVO) {
  try {
    activePayment.value = await financeApi.getPaymentDetail(payment.id)
  } catch {
    activePayment.value = payment
  }
  detailVisible.value = true
}

async function verifyPayment(id: number) {
  try {
    await financeApi.verifyPayment(id)
    ElMessage.success('核销成功')
    await loadPayments()
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '核销失败')
  }
}

function showCancelDialog(row: PaymentRecordVO) {
  cancelTargetId.value = row.id
  cancelReason.value = ''
  cancelVisible.value = true
}

async function confirmCancel() {
  if (!cancelReason.value.trim()) { ElMessage.warning('请填写撤销原因'); return }
  if (!cancelTargetId.value) return
  cancelling.value = true
  try {
    await financeApi.cancelPayment(cancelTargetId.value, { cancelReason: cancelReason.value.trim() })
    ElMessage.success('已撤销核销')
    cancelVisible.value = false
    await loadPayments()
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '撤销失败')
  } finally { cancelling.value = false }
}

function getStatusText(status: number) {
  const map: Record<number, string> = { 0: '待核销', 1: '已核销', 2: '已驳回', 3: '已撤销' }
  return map[status] || '未知'
}
function getStatusTone(status: number) {
  const map: Record<number, string> = { 0: 'warning', 1: 'success', 2: 'danger', 3: 'info' }
  return map[status] || 'info'
}
</script>

<style scoped>
.pagination-wrap { display: flex; justify-content: flex-end; margin-top: 24px; }
</style>
