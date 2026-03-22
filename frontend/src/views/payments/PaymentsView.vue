<template>
  <PageContainer title="缴费流水与核销" description="聚合线上支付、人工核销和退款记录，方便财务快速对账与异常处理。">
    <template #actions>
      <el-button plain @click="reconcile">批量核销</el-button>
      <el-button type="primary" class="btn-primary-gradient" @click="exportRecords">导出流水</el-button>
    </template>

    <DataToolbar v-model:keyword="keyword" v-model:status="status" placeholder="搜索账单编号、缴费人..." :filters="filters" />

    <section class="surface-card">
      <el-table :data="filteredPayments">
        <el-table-column prop="billNo" label="账单编号" min-width="160" />
        <el-table-column prop="payer" label="缴费人" min-width="130" />
        <el-table-column label="缴费金额" min-width="130">
          <template #default="{ row }">¥{{ row.amount.toLocaleString() }}</template>
        </el-table-column>
        <el-table-column prop="channel" label="缴费渠道" min-width="130" />
        <el-table-column prop="paidAt" label="时间" min-width="150" />
        <el-table-column label="状态" min-width="120">
          <template #default="{ row }">
            <span class="status-pill" :class="getStatusClass(row.status)">{{ getStatusText(row.status) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button link type="primary" @click="inspectRecord(row.billNo)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { ElMessage } from 'element-plus'
import DataToolbar from '@/components/DataToolbar.vue'
import PageContainer from '@/components/PageContainer.vue'
import { paymentRecords } from '@/mock/data'

const keyword = ref('')
const status = ref('')

const filters = [
  { label: '已支付', value: 'paid' },
  { label: '待核销', value: 'pending' },
  { label: '退款中', value: 'refund' },
]

const statusText = {
  paid: '已支付',
  pending: '待核销',
  refund: '退款中',
}

const statusClass = {
  paid: 'success',
  pending: 'warning',
  refund: 'danger',
}

const filteredPayments = computed(() =>
  paymentRecords.filter((item) => {
    const matchesKeyword = !keyword.value || `${item.billNo}${item.payer}${item.channel}`.includes(keyword.value)
    const matchesStatus = !status.value || item.status === status.value
    return matchesKeyword && matchesStatus
  }),
)

function reconcile() {
  ElMessage.success('已提交批量核销请求')
}

function exportRecords() {
  ElMessage.success('缴费流水导出任务已加入队列')
}

function inspectRecord(billNo: string) {
  ElMessage.info(`查看 ${billNo} 的核销详情`)
}

function getStatusText(statusValue: 'paid' | 'pending' | 'refund') {
  return statusText[statusValue]
}

function getStatusClass(statusValue: 'paid' | 'pending' | 'refund') {
  return statusClass[statusValue]
}
</script>
