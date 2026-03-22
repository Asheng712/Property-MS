<template>
  <PageContainer title="商业合同管理" description="统一维护租赁合同、到期提醒和租金金额，支持新增草稿与续签跟进。">
    <template #actions>
      <el-button type="primary" class="btn-primary-gradient" @click="dialogVisible = true">新增合同</el-button>
    </template>

    <DataToolbar v-model:keyword="keyword" v-model:status="status" placeholder="搜索合同编号、租户..." :filters="filters" />

    <section class="surface-card">
      <el-table :data="filteredContracts">
        <el-table-column prop="contractNo" label="合同编号" min-width="150" />
        <el-table-column prop="tenant" label="租户名称" min-width="140" />
        <el-table-column prop="asset" label="关联资产" min-width="120" />
        <el-table-column prop="period" label="合同周期" min-width="220" />
        <el-table-column label="合同金额" min-width="140">
          <template #default="{ row }">¥{{ row.amount.toLocaleString() }}</template>
        </el-table-column>
        <el-table-column label="状态" min-width="120">
          <template #default="{ row }">
            <span class="status-pill" :class="getStatusClass(row.status)">{{ getStatusText(row.status) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default>
            <el-button link type="primary">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>

    <el-dialog v-model="dialogVisible" title="新增商业合同" width="520px">
      <el-form label-position="top" class="dialog-form">
        <el-form-item label="合同编号">
          <el-input v-model="draft.contractNo" />
        </el-form-item>
        <el-form-item label="租户名称">
          <el-input v-model="draft.tenant" />
        </el-form-item>
        <el-form-item label="关联资产">
          <el-input v-model="draft.asset" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="createContract">保存草稿</el-button>
      </template>
    </el-dialog>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import DataToolbar from '@/components/DataToolbar.vue'
import PageContainer from '@/components/PageContainer.vue'
import { contractRecords } from '@/mock/data'

const keyword = ref('')
const status = ref('')
const dialogVisible = ref(false)

const filters = [
  { label: '生效中', value: 'active' },
  { label: '即将到期', value: 'expiring' },
  { label: '草稿', value: 'draft' },
]

const statusText = {
  active: '生效中',
  expiring: '即将到期',
  draft: '草稿',
}

const statusClass = {
  active: 'success',
  expiring: 'warning',
  draft: 'info',
}

const draft = reactive({
  contractNo: '',
  tenant: '',
  asset: '',
})

const filteredContracts = computed(() =>
  contractRecords.filter((item) => {
    const matchesKeyword = !keyword.value || `${item.contractNo}${item.tenant}${item.asset}`.includes(keyword.value)
    const matchesStatus = !status.value || item.status === status.value
    return matchesKeyword && matchesStatus
  }),
)

function createContract() {
  dialogVisible.value = false
  ElMessage.success(`合同 ${draft.contractNo || '草稿'} 已创建`)
  draft.contractNo = ''
  draft.tenant = ''
  draft.asset = ''
}

function getStatusText(statusValue: 'active' | 'expiring' | 'draft') {
  return statusText[statusValue]
}

function getStatusClass(statusValue: 'active' | 'expiring' | 'draft') {
  return statusClass[statusValue]
}
</script>
