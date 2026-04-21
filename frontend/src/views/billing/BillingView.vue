<template>
  <PageContainer title="智能账单引擎" description="已接入批量生成账单、批次记录和账单明细接口。">
    <template #actions>
      <el-button type="primary" class="btn-primary-gradient" :loading="generating" @click="generateMonthlyBills">生成本月账单</el-button>
    </template>

    <div class="billing-summary">
      <article class="surface-card billing-summary__card">
        <div class="billing-summary__icon">账</div>
        <div>
          <strong>{{ totalGenerated }}</strong>
          <span>当前批次累计生成账单数</span>
        </div>
      </article>
    </div>

    <PanelCard title="账单批次记录">
      <el-table v-loading="loadingBatches" :data="batches">
        <el-table-column prop="batchNo" label="批次号" min-width="170" />
        <el-table-column prop="targetRange" label="计费范围" min-width="180" />
        <el-table-column prop="feeType" label="费用类型" min-width="140" />
        <el-table-column prop="totalCount" label="生成数量" min-width="100" />
        <el-table-column label="总金额" min-width="140">
          <template #default="{ row }">{{ formatCurrency(Number(row.totalAmount)) }}</template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="170" />
        <el-table-column prop="status" label="状态" min-width="120">
          <template #default="{ row }">
            <StatusBadge :label="row.status" :tone="row.status === 'COMPLETED' ? 'success' : 'info'" />
          </template>
        </el-table-column>
      </el-table>
    </PanelCard>

    <PanelCard title="账单明细" description="数据来自 /api/v1/bills。">
      <el-table v-loading="loadingBills" :data="bills">
        <el-table-column prop="billNo" label="账单号" min-width="170" />
        <el-table-column prop="houseName" label="资产名称" min-width="160">
          <template #default="{ row }">{{ row.houseName || `房屋ID: ${row.houseId}` }}</template>
        </el-table-column>
        <el-table-column label="金额" min-width="140">
          <template #default="{ row }">{{ formatCurrency(Number(row.amount)) }}</template>
        </el-table-column>
        <el-table-column prop="type" label="费用类型" min-width="130" />
        <el-table-column prop="deadline" label="截止日期" min-width="140" />
        <el-table-column label="缴费状态" min-width="120">
          <template #default="{ row }">
            <StatusBadge :label="row.payStatusText || (row.payStatus === 1 ? '已缴' : '未缴')" :tone="row.payStatus === 1 ? 'success' : 'warning'" />
          </template>
        </el-table-column>
      </el-table>
    </PanelCard>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import PageContainer from '@/components/PageContainer.vue'
import PanelCard from '@/components/PanelCard.vue'
import StatusBadge from '@/components/StatusBadge.vue'
import { financeApi } from '@/services/api'
import { formatCurrency } from '@/utils/format'
import type { BillBatchRecord, BillRecord } from '@/types'

const generating = ref(false)
const loadingBatches = ref(false)
const loadingBills = ref(false)
const batches = ref<BillBatchRecord[]>([])
const bills = ref<BillRecord[]>([])

const totalGenerated = computed(() =>
  batches.value.reduce((sum, item) => sum + Number(item.totalCount || 0), 0).toLocaleString('zh-CN'),
)

onMounted(() => {
  void Promise.all([loadBatchLogs(), loadBills()])
})

async function loadBatchLogs() {
  loadingBatches.value = true
  try {
    const result = await financeApi.getBatchLogs({ page: 1, pageSize: 20 })
    batches.value = result.records
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '加载批次记录失败')
  } finally {
    loadingBatches.value = false
  }
}

async function loadBills() {
  loadingBills.value = true
  try {
    const result = await financeApi.getBills({ page: 1, pageSize: 20 })
    bills.value = result.records
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '加载账单明细失败')
  } finally {
    loadingBills.value = false
  }
}

async function generateMonthlyBills() {
  generating.value = true
  try {
    await financeApi.generateBills({
      feeType: '物业费',
      targetRange: '全项目',
      month: new Date().toISOString().slice(0, 7),
    })
    ElMessage.success('账单批次已生成')
    await Promise.all([loadBatchLogs(), loadBills()])
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '生成账单失败')
  } finally {
    generating.value = false
  }
}
</script>

<style scoped>
.billing-summary {
  display: flex;
  margin-bottom: 20px;
}

.billing-summary__card {
  display: flex;
  align-items: center;
  gap: 18px;
  min-width: 340px;
}

.billing-summary__card strong {
  display: block;
  font-size: 38px;
  color: #20304b;
}

.billing-summary__card span {
  color: #8ea0b9;
}

.billing-summary__icon {
  display: grid;
  place-items: center;
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background: #e4efff;
  color: #2563eb;
  font-size: 24px;
}
</style>
