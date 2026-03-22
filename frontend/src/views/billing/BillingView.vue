<template>
  <PageContainer title="智能账单引擎" description="一键基于单价、面积或阶梯计费规则批量生成账单，批处理过程透明可追踪。">
    <template #actions>
      <el-button plain @click="ruleDialogVisible = true">计费规则配置</el-button>
      <el-button type="primary" class="btn-primary-gradient" @click="generateMonthlyBills">一键生成本月账单</el-button>
    </template>

    <div class="billing-summary">
      <article class="surface-card billing-summary__card">
        <div class="billing-summary__icon">⚡</div>
        <div>
          <strong>12,450</strong>
          <span>本月已生成账单数</span>
        </div>
      </article>
    </div>

    <section class="surface-card">
      <div class="section-title">
        <h3>账单批处理记录</h3>
      </div>
      <el-table :data="billBatches">
        <el-table-column prop="batchNo" label="生成批次号" min-width="150" />
        <el-table-column prop="scope" label="计费对象范围" min-width="180" />
        <el-table-column prop="feeType" label="费用类型" min-width="140" />
        <el-table-column prop="generatedCount" label="生成数量" min-width="100" />
        <el-table-column label="总金额" min-width="140">
          <template #default="{ row }">¥{{ row.totalAmount.toLocaleString() }}</template>
        </el-table-column>
        <el-table-column prop="executedAt" label="执行时间" min-width="150" />
        <el-table-column label="状态" min-width="120">
          <template #default="{ row }">
            <span class="status-pill" :class="row.status === 'done' ? 'success' : 'info'">
              {{ row.status === 'done' ? '已生成' : '处理中' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default>
            <el-button link type="primary">查看明细</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>

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
  </PageContainer>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import PageContainer from '@/components/PageContainer.vue'
import { billBatches } from '@/mock/data'

const ruleDialogVisible = ref(false)

const ruleForm = reactive({
  propertyMode: 'area',
  utilityRule: '0-100 度：0.56 元；100-200 度：0.68 元；200 度以上：0.82 元',
  cycle: 'monthly',
})

function generateMonthlyBills() {
  ElMessage.success('已触发本月账单生成任务，当前为演示态。')
}

function saveRules() {
  ruleDialogVisible.value = false
  ElMessage.success('计费规则已保存')
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
