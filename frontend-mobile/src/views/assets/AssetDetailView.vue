<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { showToast, showDialog } from 'vant'
import { assetApi, purchaseApi } from '@/services/api'
import { useUserStore } from '@/stores/user'
import { formatArea } from '@/utils/format'
import type { AssetRecord } from '@/types'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const asset = ref<AssetRecord | null>(null)
const loading = ref(true)
const submitting = ref(false)

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

const typeLabelMap: Record<string, string> = {
  BUILDING: '楼栋',
  UNIT: '单元',
  RESIDENTIAL: '住宅',
  SHOP: '商铺',
}

onMounted(async () => {
  const id = Number(route.params.id)
  if (!id) {
    router.back()
    return
  }
  loading.value = true
  try {
    asset.value = await assetApi.getById(id)
  } catch {
    asset.value = null
  } finally {
    loading.value = false
  }
})

async function handleApply() {
  if (!asset.value) return

  try {
    await showDialog({
      title: '申请购买资产',
      message: `确认申请购买「${asset.value.name}」吗？提交后管理员将进行审批。`,
      showCancelButton: true,
      confirmButtonText: '确认申请',
      cancelButtonText: '取消',
    })
  } catch {
    return
  }

  submitting.value = true
  try {
    const userInfo = userStore.userInfo
    await purchaseApi.create({
      houseId: asset.value!.id,
      applicantName: userInfo?.realName || userInfo?.username || '',
      applicantPhone: userInfo?.phone || '',
    })
    showToast('申请已提交，请等待管理员审批')
    router.back()
  } catch (e: any) {
    showToast(e?.message || '提交失败')
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <div class="page-container--with-nav">
    <van-nav-bar title="资产详情" left-arrow @click-left="router.back()" />

    <PageSkeleton v-if="loading" />

    <template v-else-if="asset">
      <div style="background: #fff; margin: 12px; border-radius: 8px; padding: 20px 16px">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px">
          <h2 style="font-size: 18px; font-weight: 700; margin: 0">{{ asset.name }}</h2>
          <van-tag type="primary" size="medium">{{ typeLabelMap[asset.type] || asset.type }}</van-tag>
        </div>

        <van-cell-group style="border-radius: 8px; overflow: hidden">
          <van-cell title="类型" :value="typeLabelMap[asset.type] || asset.type" />
          <van-cell title="面积" :value="asset.area ? formatArea(asset.area) : '-'" />
          <van-cell title="状态">
            <template #value>
              <van-tag :type="(statusTagMap[asset.status] || 'default') as any" size="small">
                {{ statusLabelMap[asset.status] || asset.status || '未知' }}
              </van-tag>
            </template>
          </van-cell>
          <van-cell v-if="asset.ownerName" title="当前业主" :value="asset.ownerName" />
          <van-cell v-if="asset.ownerPhone" title="联系电话" :value="asset.ownerPhone" />
        </van-cell-group>
      </div>

      <div style="padding: 0 12px">
        <van-button
          type="primary"
          block
          round
          :loading="submitting"
          @click="handleApply"
        >
          申请购买
        </van-button>
      </div>
    </template>

    <EmptyState v-else description="未找到资产信息" />
  </div>
</template>
