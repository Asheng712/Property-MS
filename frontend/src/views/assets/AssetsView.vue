<template>
  <PageContainer title="资产全景管理" description="以树形结构管理楼栋、单元、房间及商铺，支持新增、编辑与台账明细查看。">
    <template #actions>
      <el-button type="primary" class="btn-primary-gradient" @click="openCreateDialog">新增资产</el-button>
    </template>

    <div class="asset-layout">
      <PanelCard title="社区空间结构" class="tree-panel">
        <el-tree
          node-key="id"
          :data="assetTree"
          default-expand-all
          :expand-on-click-node="false"
          @node-click="handleNodeClick"
        />
      </PanelCard>

      <section class="asset-main">
        <DataToolbar
          v-model:keyword="keyword"
          v-model:status="status"
          placeholder="搜索房号、业主姓名..."
          select-placeholder="筛选状态"
          :filters="statusFilters"
        />

        <PanelCard title="资产台账" description="支持查看状态、承租人和面积信息">
          <el-table :data="filteredAssets">
            <el-table-column prop="code" label="房号/铺号" min-width="120" />
            <el-table-column prop="name" label="房间号/店铺号" min-width="140" />
            <el-table-column label="资源类型" min-width="120">
              <template #default="{ row }">{{ getCategoryText(row.category) }}</template>
            </el-table-column>
            <el-table-column label="建筑面积" min-width="160">
              <template #default="{ row }">{{ formatArea(row.area) }} {{ row.areaLabel }}</template>
            </el-table-column>
            <el-table-column label="交付状态" min-width="120">
              <template #default="{ row }">
                <StatusBadge :label="getAssetStatusText(row.deliveryStatus)" :tone="getAssetStatusTone(row.deliveryStatus)" />
              </template>
            </el-table-column>
            <el-table-column prop="occupant" label="业主/承租人" min-width="140" />
            <el-table-column label="操作" width="140" fixed="right">
              <template #default="{ row }">
                <el-button link type="primary" @click="openDetail(row.id)">详情</el-button>
                <el-button link type="primary" @click="openEdit(row.id)">编辑</el-button>
              </template>
            </el-table-column>
          </el-table>
        </PanelCard>
      </section>
    </div>

    <el-dialog v-model="assetDialogVisible" :title="editingAssetId ? '编辑资产' : '新增资产'" width="520px">
      <el-form label-position="top" class="dialog-form">
        <el-form-item label="房号/铺号">
          <el-input v-model="draftAsset.code" />
        </el-form-item>
        <el-form-item label="资源类型">
          <el-select v-model="draftAsset.category">
            <el-option label="商铺" value="shop" />
            <el-option label="住宅" value="residence" />
            <el-option label="车位" value="parking" />
          </el-select>
        </el-form-item>
        <el-form-item label="建筑面积">
          <el-input v-model.number="draftAsset.area" />
        </el-form-item>
        <el-form-item label="业主/承租人">
          <el-input v-model="draftAsset.occupant" />
        </el-form-item>
        <el-form-item label="交付状态">
          <el-select v-model="draftAsset.deliveryStatus">
            <el-option label="经营中" value="occupied" />
            <el-option label="空置" value="vacant" />
            <el-option label="已售" value="sold" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="assetDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveAsset">保存</el-button>
      </template>
    </el-dialog>

    <el-drawer v-model="detailVisible" title="资产详情" size="420px">
      <InfoList v-if="activeAsset" :items="detailItems" />
    </el-drawer>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import DataToolbar from '@/components/DataToolbar.vue'
import InfoList from '@/components/InfoList.vue'
import PageContainer from '@/components/PageContainer.vue'
import PanelCard from '@/components/PanelCard.vue'
import StatusBadge from '@/components/StatusBadge.vue'
import { assetRecords, assetTree } from '@/mock/data'
import type { AssetRecord } from '@/types'
import { createLocalId, formatArea } from '@/utils/format'

type AssetStatus = AssetRecord['deliveryStatus']
type AssetCategory = AssetRecord['category']

const keyword = ref('')
const status = ref('')
const selectedZone = ref('')
const assetDialogVisible = ref(false)
const detailVisible = ref(false)
const editingAssetId = ref('')
const activeAssetId = ref('')
const assets = ref<AssetRecord[]>(assetRecords.map((item) => ({ ...item })))

const statusFilters = [
  { label: '经营中', value: 'occupied' },
  { label: '空置', value: 'vacant' },
  { label: '已售', value: 'sold' },
]

const assetStatusText: Record<AssetStatus, string> = {
  occupied: '经营中',
  vacant: '空置',
  sold: '已售',
}

const assetStatusTone: Record<AssetStatus, 'success' | 'warning' | 'info'> = {
  occupied: 'success',
  vacant: 'warning',
  sold: 'info',
}

const categoryText: Record<AssetCategory, string> = {
  shop: '商铺',
  residence: '住宅',
  parking: '车位',
}

const draftAsset = reactive({
  code: '',
  category: 'shop' as AssetCategory,
  area: 0,
  occupant: '',
  deliveryStatus: 'vacant' as AssetStatus,
})

const filteredAssets = computed(() =>
  assets.value.filter((item) => {
    const matchesZone = !selectedZone.value || item.zone === selectedZone.value
    const matchesKeyword = !keyword.value || `${item.code}${item.occupant}${item.name}`.includes(keyword.value)
    const matchesStatus = !status.value || item.deliveryStatus === status.value
    return matchesZone && matchesKeyword && matchesStatus
  }),
)

const activeAsset = computed(() => assets.value.find((item) => item.id === activeAssetId.value) ?? null)

const detailItems = computed(() =>
  activeAsset.value
    ? [
        { label: '房号/铺号', value: activeAsset.value.code },
        { label: '资源类型', value: getCategoryText(activeAsset.value.category) },
        { label: '建筑面积', value: formatArea(activeAsset.value.area) },
        { label: '状态', value: getAssetStatusText(activeAsset.value.deliveryStatus) },
        { label: '业主/承租人', value: activeAsset.value.occupant || '-' },
        { label: '所属分区', value: activeAsset.value.zone.toUpperCase() },
      ]
    : [],
)

function handleNodeClick(node: { id: string }) {
  selectedZone.value = node.id.includes('-') ? node.id.split('-')[0] : node.id
}

function openCreateDialog() {
  editingAssetId.value = ''
  resetDraft()
  assetDialogVisible.value = true
}

function openEdit(assetId: string) {
  const found = assets.value.find((item) => item.id === assetId)
  if (!found) {
    return
  }
  editingAssetId.value = assetId
  draftAsset.code = found.code
  draftAsset.category = found.category
  draftAsset.area = found.area
  draftAsset.occupant = found.occupant === '-' ? '' : found.occupant
  draftAsset.deliveryStatus = found.deliveryStatus
  assetDialogVisible.value = true
}

function openDetail(assetId: string) {
  activeAssetId.value = assetId
  detailVisible.value = true
}

function getCategoryText(category: AssetCategory) {
  return categoryText[category]
}

function getAssetStatusText(statusValue: AssetStatus) {
  return assetStatusText[statusValue]
}

function getAssetStatusTone(statusValue: AssetStatus) {
  return assetStatusTone[statusValue]
}

function saveAsset() {
  if (!draftAsset.code.trim() || !draftAsset.area) {
    ElMessage.warning('请完整填写资产编号和面积')
    return
  }

  const baseRecord: AssetRecord = {
    id: editingAssetId.value || createLocalId('asset'),
    code: draftAsset.code,
    name: draftAsset.code,
    category: draftAsset.category,
    area: draftAsset.area,
    areaLabel: `${draftAsset.area}平方米`,
    deliveryStatus: draftAsset.deliveryStatus,
    occupant: draftAsset.occupant.trim() || '-',
    zone: selectedZone.value || 'a',
  }

  if (editingAssetId.value) {
    assets.value = assets.value.map((item) => (item.id === editingAssetId.value ? baseRecord : item))
    ElMessage.success('资产信息已更新')
  } else {
    assets.value = [baseRecord, ...assets.value]
    ElMessage.success('资产已新增到台账')
  }

  assetDialogVisible.value = false
  resetDraft()
}

function resetDraft() {
  draftAsset.code = ''
  draftAsset.category = 'shop'
  draftAsset.area = 0
  draftAsset.occupant = ''
  draftAsset.deliveryStatus = 'vacant'
}
</script>

<style scoped>
.asset-layout {
  display: grid;
  grid-template-columns: 280px minmax(0, 1fr);
  gap: 20px;
}

.tree-panel {
  min-height: 620px;
}

.asset-main {
  display: grid;
  gap: 16px;
}

@media (max-width: 1024px) {
  .asset-layout {
    grid-template-columns: 1fr;
  }
}
</style>
