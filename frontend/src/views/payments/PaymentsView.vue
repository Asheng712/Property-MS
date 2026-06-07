<template>
  <PageContainer title="缴费流水与核销" description="查看缴费流水并处理待核销记录。">
    <DataToolbar
      v-model:keyword="keyword"
      v-model:status="status"
      placeholder="搜索交易流水号"
      select-placeholder="筛选核销状态"
      :filters="filters"
    >
      <el-button @click="handleReset">重置</el-button>
      <el-button type="primary" @click="handleSearch">查询</el-button>
    </DataToolbar>

    <PanelCard title="缴费流水">
      <el-table v-loading="loading" :data="payments">
        <el-table-column prop="trxNo" label="交易流水号" min-width="180" />
        <el-table-column prop="billNo" label="账单号" min-width="160" />
        <el-table-column prop="houseName" label="资产名称" min-width="150">
          <template #default="{ row }">{{ row.houseName || `房屋ID: ${row.houseId}` }}</template>
        </el-table-column>
        <el-table-column label="缴费金额" min-width="140">
          <template #default="{ row }">{{ formatCurrency(Number(row.payAmount)) }}</template>
        </el-table-column>
        <el-table-column label="支付方式" min-width="120">
          <template #default="{ row }">{{ getPayTypeText(row.payType) }}</template>
        </el-table-column>
        <el-table-column prop="payTime" label="支付时间" min-width="170" />
        <el-table-column label="状态" min-width="120">
          <template #default="{ row }">
            <StatusBadge :label="row.statusText || getStatusText(row.status)" :tone="getStatusTone(row.status)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="170">
          <template #default="{ row }">
            <el-button link type="primary" @click="inspectRecord(row)">详情</el-button>
            <el-button v-if="row.status === 0" link type="primary" @click="confirmPayment(row.id)">确认</el-button>
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

    <el-drawer v-model="detailVisible" title="流水详情" size="420px">
      <InfoList v-if="activePayment" :items="detailItems" />
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
import { financeApi } from '@/services/api'
import { formatCurrency } from '@/utils/format'
import { useAppStore } from '@/stores/app'
import type { PaymentRecord } from '@/types'

const loading = ref(false)
const keyword = ref('')
const status = ref('')
const total = ref(0)
const detailVisible = ref(false)
const payments = ref<PaymentRecord[]>([])
const activePayment = ref<PaymentRecord | null>(null)

const query = reactive({
  page: 1,
  pageSize: 20,
})

const filters = [
  { label: '待核销', value: '0' },
  { label: '已核销', value: '1' },
]

const detailItems = computed(() =>
  activePayment.value
    ? [
        { label: '交易流水号', value: activePayment.value.trxNo },
        { label: '账单号', value: activePayment.value.billNo || '-' },
        { label: '资产名称', value: activePayment.value.houseName || '-' },
        { label: '金额', value: formatCurrency(Number(activePayment.value.payAmount)) },
        { label: '支付方式', value: getPayTypeText(activePayment.value.payType) },
        { label: '支付时间', value: activePayment.value.payTime || '-' },
        { label: '操作人', value: activePayment.value.operator || '-' },
        { label: '状态', value: activePayment.value.statusText || getStatusText(activePayment.value.status) },
      ]
    : [],
)

onMounted(() => {
  void loadPayments()
})

async function loadPayments() {
  loading.value = true
  try {
    const result = await financeApi.getPayments({
      page: query.page,
      pageSize: query.pageSize,
      trxNo: keyword.value.trim() || undefined,
      status: status.value ? Number(status.value) : undefined,
    })
    payments.value = result.records
    total.value = result.total
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '加载缴费流水失败')
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  query.page = 1
  void loadPayments()
}

function handleReset() {
  keyword.value = ''
  status.value = ''
  query.page = 1
  void loadPayments()
}

function handlePageChange(page: number) {
  query.page = page
  void loadPayments()
}

function handleSizeChange(size: number) {
  query.page = 1
  query.pageSize = size
  void loadPayments()
}

function inspectRecord(payment: PaymentRecord) {
  activePayment.value = payment
  detailVisible.value = true
}

async function confirmPayment(id: number) {
  try {
    await financeApi.auditPayment(id, {
      status: 1,
      operator: useAppStore().currentUser,
    })
    ElMessage.success('该笔流水已核销完成')
    await loadPayments()
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '核销失败')
  }
}

function getPayTypeText(value: string) {
  const mapping: Record<string, string> = {
    WECHAT: '微信支付',
    ALIPAY: '支付宝',
    TRANSFER: '银行转账',
    CASH: '现金',
    微信支付: '微信支付',
    支付宝: '支付宝',
    银行转账: '银行转账',
    现金: '现金',
  }
  return mapping[value] || value
}

function getStatusText(value: number) {
  return value === 1 ? '已核销' : '待核销'
}

function getStatusTone(value: number) {
  return value === 1 ? 'success' : 'warning'
}
</script>

<style scoped>
.pagination-wrap {
  display: flex;
  justify-content: flex-end;
  margin-top: 24px;
}
</style>
