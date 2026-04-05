<template>
  <PageContainer title="缴费流水与核销" description="聚合线上支付、人工核销和退款记录，支持对账查看、批量核销与异常状态跟踪。">
    <template #actions>
      <el-button plain @click="reconcile">批量核销</el-button>
      <el-button type="primary" class="btn-primary-gradient" @click="exportRecords">导出流水</el-button>
    </template>

    <DataToolbar v-model:keyword="keyword" v-model:status="status" placeholder="搜索账单编号、缴费人..." :filters="filters" />

    <PanelCard title="缴费流水">
      <el-table :data="filteredPayments">
        <el-table-column prop="billNo" label="账单编号" min-width="160" />
        <el-table-column prop="payer" label="缴费人" min-width="130" />
        <el-table-column label="缴费金额" min-width="130">
          <template #default="{ row }">{{ formatCurrency(row.amount) }}</template>
        </el-table-column>
        <el-table-column prop="channel" label="缴费渠道" min-width="130" />
        <el-table-column prop="paidAt" label="时间" min-width="150" />
        <el-table-column label="状态" min-width="120">
          <template #default="{ row }">
            <StatusBadge :label="getStatusText(row.status)" :tone="getStatusTone(row.status)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="170">
          <template #default="{ row }">
            <el-button link type="primary" @click="inspectRecord(row.id)">详情</el-button>
            <el-button v-if="row.status === 'pending'" link type="primary" @click="confirmPayment(row.id)">确认</el-button>
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
import { computed, ref } from 'vue'
import { ElMessage } from 'element-plus'
import DataToolbar from '@/components/DataToolbar.vue'
import InfoList from '@/components/InfoList.vue'
import PageContainer from '@/components/PageContainer.vue'
import PanelCard from '@/components/PanelCard.vue'
import StatusBadge from '@/components/StatusBadge.vue'
import { paymentRecords } from '@/mock/data'
import type { PaymentRecord } from '@/types'
import { formatCurrency, nowText } from '@/utils/format'

type PaymentStatus = PaymentRecord['status']

const keyword = ref('')
const status = ref('')
const detailVisible = ref(false)
const activeId = ref('')
const payments = ref<PaymentRecord[]>(paymentRecords.map((item) => ({ ...item })))

const filters = [
  { label: '已支付', value: 'paid' },
  { label: '待核销', value: 'pending' },
  { label: '退款中', value: 'refund' },
]

const statusText: Record<PaymentStatus, string> = {
  paid: '已支付',
  pending: '待核销',
  refund: '退款中',
}

const statusTone: Record<PaymentStatus, 'success' | 'warning' | 'danger'> = {
  paid: 'success',
  pending: 'warning',
  refund: 'danger',
}

const filteredPayments = computed(() =>
  payments.value.filter((item) => {
    const matchesKeyword = !keyword.value || `${item.billNo}${item.payer}${item.channel}`.includes(keyword.value)
    const matchesStatus = !status.value || item.status === status.value
    return matchesKeyword && matchesStatus
  }),
)

const activePayment = computed(() => payments.value.find((item) => item.id === activeId.value) ?? null)

const detailItems = computed(() =>
  activePayment.value
    ? [
        { label: '账单编号', value: activePayment.value.billNo },
        { label: '缴费人', value: activePayment.value.payer },
        { label: '金额', value: formatCurrency(activePayment.value.amount) },
        { label: '渠道', value: activePayment.value.channel },
        { label: '缴费时间', value: activePayment.value.paidAt },
        { label: '状态', value: getStatusText(activePayment.value.status) },
      ]
    : [],
)

function reconcile() {
  payments.value = payments.value.map((item) => (item.status === 'pending' ? { ...item, status: 'paid', paidAt: nowText() } : item))
  ElMessage.success('待核销流水已批量确认')
}

function exportRecords() {
  ElMessage.success('缴费流水导出任务已加入队列')
}

function inspectRecord(paymentId: string) {
  activeId.value = paymentId
  detailVisible.value = true
}

function confirmPayment(paymentId: string) {
  payments.value = payments.value.map((item) => (item.id === paymentId ? { ...item, status: 'paid', paidAt: nowText() } : item))
  ElMessage.success('该笔流水已核销完成')
}

function getStatusText(statusValue: PaymentStatus) {
  return statusText[statusValue]
}

function getStatusTone(statusValue: PaymentStatus) {
  return statusTone[statusValue]
}
</script>
