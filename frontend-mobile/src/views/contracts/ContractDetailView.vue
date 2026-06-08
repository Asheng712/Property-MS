<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { contractApi } from '@/services/api'
import { formatCurrency } from '@/utils/format'
import type { ContractRecord } from '@/types'

const router = useRouter()
const route = useRoute()

const contract = ref<ContractRecord | null>(null)

const statusMap: Record<number, string> = { 0: '过期/终止', 1: '生效中' }
const statusTagMap: Record<number, string> = { 0: 'warning', 1: 'success' }

onMounted(async () => {
  const id = Number(route.params.id)
  try {
    const result = await contractApi.getList({ page: 1, pageSize: 100 })
    contract.value = result.records.find((c) => c.id === id) || null
  } catch {
    /* ignore */
  }
})
</script>

<template>
  <div class="page-container--with-nav">
    <van-nav-bar title="合同详情" left-arrow @click-left="router.back()" />

    <div v-if="contract">
      <van-cell-group title="合同信息" style="margin-top: 8px">
        <van-cell title="房屋" :value="contract.houseName || `#${contract.houseId}`" />
        <van-cell title="租户" :value="contract.tenantName" />
        <van-cell title="月租金" :value="formatCurrency(contract.rentAmount)" />
        <van-cell title="押金" :value="contract.deposit ? formatCurrency(contract.deposit) : '-'" />
        <van-cell title="合同状态">
          <template #value>
            <van-tag :type="statusTagMap[contract.contractStatus] as any" size="small">
              {{ statusMap[contract.contractStatus] || '未知' }}
            </van-tag>
          </template>
        </van-cell>
        <van-cell title="开始日期" :value="contract.startDate" />
        <van-cell title="结束日期" :value="contract.endDate" />
        <van-cell v-if="contract.increaseRate" title="年递增率" :value="`${contract.increaseRate}%`" />
      </van-cell-group>
    </div>

    <EmptyState v-else description="未找到合同信息" />
  </div>
</template>
