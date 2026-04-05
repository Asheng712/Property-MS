<template>
  <PageContainer title="智能账单引擎" description="基于物业费、租金与阶梯水电规则批量生成账单，保留处理记录与详情。">
    <template #actions>
      <el-button plain @click="ruleDialogVisible = true">计费规则配置</el-button>
      <el-button type="primary" class="btn-primary-gradient" @click="generateMonthlyBills">一键生成本月账单</el-button>
    </template>

    <div class="billing-summary">
      <article class="surface-card billing-summary__card">
        <div class="billing-summary__icon">⚡</div>
        <div>
          <strong>{{ totalGenerated }}</strong>
          <span>本月已生成账单数</span>
        </div>
      </article>
    </div>

    <PanelCard title="账单批处理记录">
      <el-table :data="batches">
        <el-table-column prop="batchNo" label="生成批次号" min-width="150" />
        <el-table-column prop="scope" label="计费对象范围" min-width="180" />
        <el-table-column prop="feeType" label="费用类型" min-width="140" />
        <el-table-column prop="generatedCount" label="生成数量" min-width="100" />
        <el-table-column label="总金额" min-width="140">
          <template #default="{ row }">{{ formatCurrency(row.totalAmount) }}</template>
        </el-table-column>
        <el-table-column prop="executedAt" label="执行时间" min-width="150" />
        <el-table-column label="状态" min-width="120">
          <template #default="{ row }">
            <StatusBadge :label="row.status === 'done' ? '已生成' : '处理中'" :tone="row.status === 'done' ? 'success' : 'info'" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button link type="primary" @click="openDetail(row.id)">查看明细</el-button>
          </template>
        </el-table-column>
      </el-table>
    </PanelCard>

    <el-dialog v-model="ruleDialogVisible" title="计费规则配置" width="560px">
      <el-form label-position="top" class="dialog-form">
        <el-form-item label="物业费计价方式">
          <el-select v-model="ruleForm.propertyMode">
            <el-option label="按面积计费" value="area" />
            <el-option label="固定费用" value="fixed" />
          </el-select>
        </el-form-item>
        <el-form-item label="水电阶梯规则">
          <el-input v-model="ruleForm.utilityRule" type="textarea" rows="4" />
        </el-form-item>
        <el-form-item label="生成周期">
          <el-select v-model="ruleForm.cycle">
            <el-option label="每月 1 日" value="monthly" />
            <el-option label="每季度首月 1 日" value="seasonal" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="ruleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveRules">保存配置</el-button>
      </template>
    </el-dialog>

    <el-drawer v-model="detailVisible" title="批处理详情" size="420px">
      <InfoList v-if="activeBatch" :items="detailItems" />
    </el-drawer>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import InfoList from '@/components/InfoList.vue'
import PageContainer from '@/components/PageContainer.vue'
import PanelCard from '@/components/PanelCard.vue'
import StatusBadge from '@/components/StatusBadge.vue'
import { billBatches } from '@/mock/data'
import type { BillBatch } from '@/types'
import { createLocalId, formatCurrency, nowText } from '@/utils/format'

const ruleDialogVisible = ref(false)
const detailVisible = ref(false)
const activeId = ref('')
const batches = ref<BillBatch[]>(billBatches.map((item) => ({ ...item })))

const ruleForm = reactive({
  propertyMode: 'area',
  utilityRule: '0-100 度：0.56 元；100-200 度：0.68 元；200 度以上：0.82 元',
  cycle: 'monthly',
})

const totalGenerated = computed(() => batches.value.reduce((sum, item) => sum + item.generatedCount, 0).toLocaleString('zh-CN'))
const activeBatch = computed(() => batches.value.find((item) => item.id === activeId.value) ?? null)
const detailItems = computed(() =>
  activeBatch.value
    ? [
        { label: '批次号', value: activeBatch.value.batchNo },
        { label: '范围', value: activeBatch.value.scope },
        { label: '费用类型', value: activeBatch.value.feeType },
        { label: '生成数量', value: activeBatch.value.generatedCount },
        { label: '总金额', value: formatCurrency(activeBatch.value.totalAmount) },
        { label: '执行时间', value: activeBatch.value.executedAt },
      ]
    : [],
)

function generateMonthlyBills() {
  batches.value = [
    {
      id: createLocalId('batch'),
      batchNo: `BATCH-${Date.now()}`,
      scope: '全小区',
      feeType: ruleForm.propertyMode === 'area' ? '物业费' : '固定费用',
      generatedCount: 268,
      totalAmount: 186000,
      executedAt: nowText(),
      status: 'done',
    },
    ...batches.value,
  ]
  ElMessage.success('已生成本月批处理账单')
}

function saveRules() {
  ruleDialogVisible.value = false
  ElMessage.success('计费规则已保存')
}

function openDetail(batchId: string) {
  activeId.value = batchId
  detailVisible.value = true
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
