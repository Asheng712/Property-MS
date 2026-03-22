<template>
  <PageContainer title="资产全景管理" description="以树形结构管理楼栋、单元、房间及商铺，基础台账一目了然。">
    <template #actions>
      <el-button type="primary" class="btn-primary-gradient" @click="assetDialogVisible = true">新增资产</el-button>
    </template>

    <div class="asset-layout">
      <section class="surface-card tree-panel">
        <div class="section-title">
          <h3>社区空间结构</h3>
        </div>
        <el-tree
          node-key="id"
          :data="assetTree"
          default-expand-all
          :expand-on-click-node="false"
          @node-click="handleNodeClick"
        />
      </section>

      <section class="asset-main">
        <DataToolbar
          v-model:keyword="keyword"
          v-model:status="status"
          placeholder="搜索房间号、业主姓名..."
          select-placeholder="筛选状态"
          :filters="statusFilters"
        />
        <section class="surface-card">
          <el-table :data="filteredAssets">
            <el-table-column prop="code" label="房号/铺号" min-width="120" />
            <el-table-column prop="name" label="房间号/店铺号" min-width="140" />
            <el-table-column label="资源类型" min-width="120">
              <template #default="{ row }">{{ getCategoryText(row.category) }}</template>
            </el-table-column>
            <el-table-column label="建筑面积" min-width="160">
              <template #default="{ row }">{{ row.area }}㎡ {{ row.areaLabel }}</template>
            </el-table-column>
            <el-table-column label="交付状态" min-width="120">
              <template #default="{ row }">
                <span class="status-pill" :class="getAssetStatusClass(row.deliveryStatus)">{{ getAssetStatusText(row.deliveryStatus) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="occupant" label="业主/承租人" min-width="140" />
            <el-table-column label="操作" width="100" fixed="right">
              <template #default="{ row }">
                <el-button link type="primary" @click="openEdit(row.code)">编辑</el-button>
              </template>
            </el-table-column>
          </el-table>
        </section>
      </section>
    </div>

    <el-dialog v-model="assetDialogVisible" title="新增资产" width="520px">
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
          <el-input v-model="draftAsset.area" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="assetDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveAsset">保存</el-button>
      </template>
    </el-dialog>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import DataToolbar from '@/components/DataToolbar.vue'
import PageContainer from '@/components/PageContainer.vue'
import { assetRecords, assetTree } from '@/mock/data'

type AssetStatus = 'occupied' | 'vacant' | 'sold'
type AssetCategory = 'shop' | 'residence' | 'parking'

const keyword = ref('')
const status = ref('')
const selectedZone = ref('')
const assetDialogVisible = ref(false)

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

const assetStatusClass: Record<AssetStatus, string> = {
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
  area: '',
})

const filteredAssets = computed(() =>
  assetRecords.filter((item) => {
    const matchesZone = !selectedZone.value || item.zone === selectedZone.value || item.id === selectedZone.value
    const matchesKeyword = !keyword.value || `${item.code}${item.occupant}`.includes(keyword.value)
    const matchesStatus = !status.value || item.deliveryStatus === status.value
    return matchesZone && matchesKeyword && matchesStatus
  }),
)

function handleNodeClick(node: { id: string }) {
  selectedZone.value = node.id.includes('-') ? node.id.split('-')[0] : node.id
}

function openEdit(code: string) {
  ElMessage.success(`已打开 ${code} 的编辑弹窗占位，可继续接后端接口。`)
}

function getCategoryText(category: AssetCategory) {
  return categoryText[category]
}

function getAssetStatusText(statusValue: AssetStatus) {
  return assetStatusText[statusValue]
}

function getAssetStatusClass(statusValue: AssetStatus) {
  return assetStatusClass[statusValue]
}

function saveAsset() {
  if (!draftAsset.code.trim()) {
    ElMessage.warning('请先填写资产编号')
    return
  }
  assetDialogVisible.value = false
  ElMessage.success('资产已保存到本地演示态')
  draftAsset.code = ''
  draftAsset.category = 'shop'
  draftAsset.area = ''
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
