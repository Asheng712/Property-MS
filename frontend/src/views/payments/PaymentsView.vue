<template>
  <PageContainer title="缴费流水与核销" description="已接入缴费流水分页查询与人工核销接口。">
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
        <el-table-column prop="payType" label="支付方式" min-width="120" />
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
    </PanelCard>

    <el-drawer v-model="detailVisible" title="流水详情" size="420px">
      <InfoList v-if="activePayment" :items="detailItems" />
    </el-drawer>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import DataToolbar from '@/components/DataToolbar.vue'
import InfoList from '@/components/InfoList.vue'
import PageContainer from '@/components/PageContainer.vue'
import PanelCard from '@/components/PanelCard.vue'
import StatusBadge from '@/components/StatusBadge.vue'
import { financeApi } from '@/services/api'
import { formatCurrency } from '@/utils/format'
import type { PaymentRecord } from '@/types'

const loading = ref(false)
const keyword = ref('')
const status = ref('')
const detailVisible = ref(false)
const payments = ref<PaymentRecord[]>([])
const activePayment = ref<PaymentRecord | null>(null)

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
        { label: '支付方式', value: activePayment.value.payType },
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
      page: 1,
      pageSize: 20,
      trxNo: keyword.value.trim() || undefined,
      status: status.value ? Number(status.value) : undefined,
    })
    payments.value = result.records
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '加载缴费流水失败')
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  void loadPayments()
}

function handleReset() {
  keyword.value = ''
  status.value = ''
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
      operator: 'admin',
    })
    ElMessage.success('该笔流水已核销完成')
    await loadPayments()
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '核销失败')
  }
}

function getStatusText(value: number) {
  return value === 1 ? '已核销' : '待核销'
}

function getStatusTone(value: number) {
  return value === 1 ? 'success' : 'warning'
}
</script>
