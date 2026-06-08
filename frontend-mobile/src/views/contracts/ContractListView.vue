<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { contractApi } from '@/services/api'
import { formatCurrency } from '@/utils/format'
import type { ContractRecord } from '@/types'

const router = useRouter()

const contracts = ref<ContractRecord[]>([])
const loading = ref(false)

const statusMap: Record<number, string> = { 0: '过期/终止', 1: '生效中' }
const statusTagMap: Record<number, string> = { 0: 'warning', 1: 'success' }

onMounted(async () => {
  loading.value = true
  try {
    const result = await contractApi.getList({
      page: 1,
      pageSize: 100,
    })
    contracts.value = result.records
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="page-container--with-nav">
    <van-nav-bar title="我的合同" left-arrow @click-left="router.back()" />

    <EmptyState
      v-if="!loading && contracts.length === 0"
      description="暂无合同"
    />

    <div v-for="item in contracts" :key="item.id" class="card-wrapper" @click="router.push(`/contracts/${item.id}`)">
      <div style="padding: 14px 16px">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px">
          <span style="font-weight: 600; font-size: 15px">{{ item.houseName || `房屋 #${item.houseId}` }}</span>
          <van-tag :type="statusTagMap[item.contractStatus] as any" size="small">
            {{ statusMap[item.contractStatus] || '未知' }}
          </van-tag>
        </div>
        <div style="display: flex; justify-content: space-between; font-size: 13px; margin-bottom: 4px">
          <span style="color: var(--van-text-color-weak)">租户: {{ item.tenantName }}</span>
          <span style="font-weight: 600">{{ formatCurrency(item.rentAmount) }}/月</span>
        </div>
        <div style="font-size: 12px; color: var(--van-text-color-weak)">
          租期: {{ item.startDate }} 至 {{ item.endDate }}
        </div>
      </div>
    </div>
  </div>
</template>
