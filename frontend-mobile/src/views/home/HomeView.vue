<script setup lang="ts">
import { ref, onActivated } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useBillStore } from '@/stores/bill'
import { useRepairStore } from '@/stores/repair'
import { useComplaintStore } from '@/stores/complaint'
import { noticeApi, assetApi } from '@/services/api'
import { formatCurrency } from '@/utils/format'
import type { NoticeRecord, AssetRecord } from '@/types'

const router = useRouter()
const userStore = useUserStore()
const billStore = useBillStore()
const repairStore = useRepairStore()
const complaintStore = useComplaintStore()

const notices = ref<NoticeRecord[]>([])
const featuredAssets = ref<AssetRecord[]>([])
const loading = ref(true)

const swipeImages = ['#1989fa', '#07c160', '#ff976a']
const swipeTitles = ['智慧物业管理', '便捷在线缴费', '一键报修服务']
const swipeDescs = ['足不出户享受物业服务', '账单清晰透明缴费无忧', '快速响应解决您的问题']

const typeLabelMap: Record<string, string> = {
  BUILDING: '楼栋',
  UNIT: '单元',
  RESIDENTIAL: '住宅',
  SHOP: '商铺',
}

const statusTagMap: Record<string, string> = {
  VACANT: 'info',
  SOLD: 'danger',
  RENTING: 'warning',
  DECORATING: 'primary',
  OCCUPIED: 'success',
}

const statusLabelMap: Record<string, string> = {
  VACANT: '空置',
  SOLD: '已售',
  RENTING: '招租中',
  DECORATING: '装修中',
  OCCUPIED: '已入住',
}

const quickActions = [
  { text: '我的账单', icon: 'gold-coin-o', path: '/bills' },
  { text: '我要报修', icon: 'records-o', path: '/repairs/create' },
  { text: '我要投诉', icon: 'comment-o', path: '/complaints/create' },
  { text: '社区公告', icon: 'bullhorn-o', path: '/notices' },
]

async function loadData() {
  loading.value = true
  try {
    const result = await noticeApi.getList({ page: 1, pageSize: 3, status: 'PUBLISHED' })
    notices.value = result.records
  } catch { /* ignore */ }

  try { await billStore.fetchBills({ page: 1, pageSize: 10 }) } catch { /* ignore */ }
  try { await repairStore.fetchRepairs() } catch { /* ignore */ }
  try { await complaintStore.fetchComplaints() } catch { /* ignore */ }

  try {
    const result = await assetApi.getList({ page: 1, pageSize: 20 })
    // 过滤掉楼栋（BUILDING），只展示可购买/租赁的叶子节点资产
    featuredAssets.value = result.records
      .filter(a => a.type !== 'BUILDING')
      .slice(0, 5)
  } catch { /* ignore */ }

  loading.value = false
}

onActivated(() => {
  loadData()
})
</script>

<template>
  <div class="page-container">
    <van-nav-bar title="智慧物业" fixed placeholder>
      <template #left>
        <span style="font-weight: 600">{{ userStore.currentUser }}</span>
      </template>
    </van-nav-bar>

    <PageSkeleton v-if="loading" />

    <template v-else>
      <van-swipe :autoplay="3000" indicator-color="#fff" style="margin: 12px; border-radius: 8px; overflow: hidden">
        <van-swipe-item
          v-for="(item, idx) in 3"
          :key="idx"
          :style="{ background: swipeImages[idx], height: '140px', color: '#fff', flexDirection: 'column', justifyContent: 'center', padding: '24px' }"
        >
          <h3 style="font-size: 18px; margin-bottom: 6px">{{ swipeTitles[idx] }}</h3>
          <p style="font-size: 13px; opacity: 0.8">{{ swipeDescs[idx] }}</p>
        </van-swipe-item>
      </van-swipe>

      <van-grid :column-num="4" :border="false" style="background: #fff; margin: 0 12px; border-radius: 8px; padding: 8px 0">
        <van-grid-item
          v-for="item in quickActions"
          :key="item.path"
          :icon="item.icon"
          :text="item.text"
          @click="router.push(item.path)"
        />
      </van-grid>

      <div v-if="featuredAssets.length > 0" style="margin: 12px">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px">
          <span class="section-title" style="margin: 0">资产购买</span>
          <span style="font-size: 13px; color: var(--van-primary-color)" @click="router.push('/assets')">查看更多 →</span>
        </div>
        <div
          v-for="item in featuredAssets"
          :key="item.id"
          class="card-wrapper"
          style="margin-bottom: 8px"
          @click="router.push(`/assets/${item.id}`)"
        >
          <div style="padding: 12px 16px">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px">
              <span style="font-weight: 600; font-size: 15px">{{ item.name }}</span>
              <van-tag :type="(statusTagMap[item.status] || 'default') as any" size="small">
                {{ statusLabelMap[item.status] || item.status || '未知' }}
              </van-tag>
            </div>
            <div style="display: flex; justify-content: space-between; font-size: 13px; color: var(--van-text-color-weak)">
              <span>{{ typeLabelMap[item.type] || item.type }}</span>
              <span>{{ item.area ? `${item.area} ㎡` : '-' }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="section-title">待办事项</div>
      <van-cell-group style="margin: 0 12px; border-radius: 8px; overflow: hidden">
        <van-cell
          title="待缴账单"
          :value="`${billStore.bills.filter(b => b.payStatus === 0).length} 笔`"
          icon="gold-coin-o"
          is-link
          @click="router.push('/bills')"
        />
        <van-cell
          title="进行中报修"
          :value="`${repairStore.pendingRepairs.length + repairStore.processingRepairs.length} 条`"
          icon="records-o"
          is-link
          @click="router.push('/repairs')"
        />
        <van-cell
          title="处理中投诉"
          :value="`${complaintStore.pendingComplaints.length} 条`"
          icon="comment-o"
          is-link
          @click="router.push('/complaints')"
        />
      </van-cell-group>

      <div class="section-title">最新公告</div>
      <div v-if="notices.length > 0" style="margin: 0 12px">
        <van-cell
          v-for="item in notices"
          :key="item.id"
          :title="item.title"
          :label="item.createTime"
          is-link
          @click="router.push(`/notices/${item.id}`)"
        />
      </div>
      <EmptyState v-else description="暂无公告" />
    </template>
  </div>
</template>
