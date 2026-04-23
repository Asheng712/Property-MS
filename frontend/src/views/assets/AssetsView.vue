<template>
  <PageContainer title="资产管理" description="已接入最新后端资产树、分页查询、新增、编辑和删除接口。">
    <template #actions>
      <el-button type="primary" class="btn-primary-gradient" @click="openCreateDialog">新增资产</el-button>
    </template>

    <div class="asset-layout">
      <PanelCard title="资产空间树" class="tree-panel">
        <el-tree
          node-key="id"
          :data="assetTree"
          :props="{ label: 'name', children: 'children' }"
          default-expand-all
          :expand-on-click-node="false"
          @node-click="handleNodeClick"
        />
      </PanelCard>

      <section class="asset-main">
        <DataToolbar
          v-model:keyword="keyword"
          v-model:status="status"
          placeholder="搜索资产名称"
          select-placeholder="筛选资产状态"
          :filters="statusFilters"
        >
          <el-button @click="handleReset">重置</el-button>
          <el-button type="primary" @click="handleSearch">查询</el-button>
        </DataToolbar>

        <PanelCard title="资产台账" description="数据来自 /api/v1/assets。">
          <el-table v-loading="loading" :data="assets">
            <el-table-column prop="id" label="ID" width="90" />
            <el-table-column prop="name" label="资产名称" min-width="180" />
            <el-table-column label="资产类型" min-width="120">
              <template #default="{ row }">{{ getTypeText(row.type) }}</template>
            </el-table-column>
            <el-table-column label="建筑面积" min-width="140">
              <template #default="{ row }">{{ formatArea(Number(row.area)) }}</template>
            </el-table-column>
            <el-table-column label="状态" min-width="120">
              <template #default="{ row }">
                <StatusBadge :label="getStatusText(row.status)" :tone="getStatusTone(row.status)" />
              </template>
            </el-table-column>
            <el-table-column prop="ownerName" label="业主姓名" min-width="140">
              <template #default="{ row }">{{ row.ownerName || '-' }}</template>
            </el-table-column>
            <el-table-column prop="ownerPhone" label="业主电话" min-width="160">
              <template #default="{ row }">{{ row.ownerPhone || '-' }}</template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" min-width="180">
              <template #default="{ row }">{{ row.createTime || '-' }}</template>
            </el-table-column>
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="{ row }">
                <el-button link type="primary" @click="openDetail(row)">详情</el-button>
                <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
                <el-button link type="danger" @click="removeAsset(row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <div class="pagination-wrap">
            <el-pagination
              background
              layout="total, sizes, prev, pager, next"
              :current-page="query.page"
              :page-size="query.pageSize"
              :page-sizes="[10, 20, 50]"
              :total="total"
              @current-change="handlePageChange"
              @size-change="handleSizeChange"
            />
          </div>
        </PanelCard>
      </section>
    </div>

    <el-dialog v-model="dialogVisible" :title="draft.id ? '编辑资产' : '新增资产'" width="560px">
      <el-form label-position="top" class="dialog-form">
        <el-form-item label="上级资产 ID">
          <el-input-number v-model="draft.parentId" :min="0" :precision="0" class="full-width" />
        </el-form-item>
        <el-form-item label="资产名称">
          <el-input v-model="draft.name" placeholder="请输入资产名称" />
        </el-form-item>
        <el-form-item label="资产类型">
          <el-select v-model="draft.type" placeholder="请选择资产类型">
            <el-option label="楼栋" value="BUILDING" />
            <el-option label="单元" value="UNIT" />
            <el-option label="住宅" value="RESIDENTIAL" />
            <el-option label="商铺" value="SHOP" />
          </el-select>
        </el-form-item>
        <el-form-item label="建筑面积">
          <el-input-number v-model="draft.area" :min="0" :precision="2" class="full-width" />
        </el-form-item>
        <el-form-item label="资产状态">
          <el-select v-model="draft.status" placeholder="请选择资产状态">
            <el-option v-for="item in statusFilters" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="业主姓名">
          <el-input v-model="draft.ownerName" placeholder="请输入业主姓名" />
        </el-form-item>
        <el-form-item label="业主电话">
          <el-input v-model="draft.ownerPhone" placeholder="请输入业主电话" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="saveAsset">保存</el-button>
      </template>
    </el-dialog>

    <el-drawer v-model="detailVisible" title="资产详情" size="420px">
      <InfoList v-if="activeAsset" :items="detailItems" />
    </el-drawer>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import DataToolbar from '@/components/DataToolbar.vue'
import InfoList from '@/components/InfoList.vue'
import PageContainer from '@/components/PageContainer.vue'
import PanelCard from '@/components/PanelCard.vue'
import StatusBadge from '@/components/StatusBadge.vue'
import { assetApi } from '@/services/api'
import { formatArea } from '@/utils/format'
import type { AssetRecord, AssetTreeNode } from '@/types'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const detailVisible = ref(false)
const keyword = ref('')
const status = ref('')
const total = ref(0)
const selectedTreeNodeId = ref<number | null>(null)
const assets = ref<AssetRecord[]>([])
const assetTree = ref<AssetTreeNode[]>([])
const activeAsset = ref<AssetRecord | null>(null)

const query = reactive({
  page: 1,
  pageSize: 10,
})

const draft = reactive<{
  id?: number
  parentId: number
  name: string
  type: string
  area: number
  status: string
  ownerName: string
  ownerPhone: string
}>({
  id: undefined,
  parentId: 0,
  name: '',
  type: 'SHOP',
  area: 0,
  status: 'occupied',
  ownerName: '',
  ownerPhone: '',
})

const statusFilters = [
  { label: '已入住', value: 'occupied' },
  { label: '空置', value: 'vacant' },
  { label: '已售', value: 'sold' },
]

const detailItems = computed(() =>
  activeAsset.value
    ? [
        { label: 'ID', value: String(activeAsset.value.id) },
        { label: '上级资产 ID', value: activeAsset.value.parentId ? String(activeAsset.value.parentId) : '-' },
        { label: '资产名称', value: activeAsset.value.name },
        { label: '资产类型', value: getTypeText(activeAsset.value.type) },
        { label: '建筑面积', value: formatArea(Number(activeAsset.value.area)) },
        { label: '资产状态', value: getStatusText(activeAsset.value.status) },
        { label: '业主姓名', value: activeAsset.value.ownerName || '-' },
        { label: '业主电话', value: activeAsset.value.ownerPhone || '-' },
        { label: '创建时间', value: activeAsset.value.createTime || '-' },
      ]
    : [],
)

onMounted(() => {
  void Promise.all([loadTree(), loadAssets()])
})

async function loadTree() {
  try {
    assetTree.value = await assetApi.getTree()
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '加载资产树失败')
  }
}

async function loadAssets() {
  loading.value = true
  try {
    const result = await assetApi.getList({
      page: query.page,
      pageSize: query.pageSize,
      name: keyword.value.trim() || undefined,
      status: status.value || undefined,
      type: undefined,
    })

    assets.value = selectedTreeNodeId.value
      ? result.records.filter((item) => item.parentId === selectedTreeNodeId.value || item.id === selectedTreeNodeId.value)
      : result.records
    total.value = result.total
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '加载资产列表失败')
  } finally {
    loading.value = false
  }
}

function handleNodeClick(node: AssetTreeNode) {
  selectedTreeNodeId.value = node.id
  void loadAssets()
}

function handleSearch() {
  query.page = 1
  void loadAssets()
}

function handleReset() {
  keyword.value = ''
  status.value = ''
  selectedTreeNodeId.value = null
  query.page = 1
  void loadAssets()
}

function handlePageChange(page: number) {
  query.page = page
  void loadAssets()
}

function handleSizeChange(size: number) {
  query.pageSize = size
  query.page = 1
  void loadAssets()
}

function openCreateDialog() {
  resetDraft()
  draft.parentId = selectedTreeNodeId.value ?? 0
  dialogVisible.value = true
}

function openEdit(asset: AssetRecord) {
  draft.id = asset.id
  draft.parentId = asset.parentId ?? 0
  draft.name = asset.name
  draft.type = asset.type
  draft.area = Number(asset.area)
  draft.status = asset.status
  draft.ownerName = asset.ownerName || ''
  draft.ownerPhone = asset.ownerPhone || ''
  dialogVisible.value = true
}

function openDetail(asset: AssetRecord) {
  activeAsset.value = asset
  detailVisible.value = true
}

async function saveAsset() {
  if (!draft.name.trim()) {
    ElMessage.warning('请输入资产名称')
    return
  }

  if (draft.area <= 0) {
    ElMessage.warning('建筑面积必须大于 0')
    return
  }

  submitting.value = true
  try {
    const payload = {
      id: draft.id,
      parentId: draft.parentId,
      name: draft.name.trim(),
      type: draft.type,
      area: draft.area,
      status: draft.status,
      ownerName: draft.ownerName.trim() || undefined,
      ownerPhone: draft.ownerPhone.trim() || undefined,
    }

    if (draft.id) {
      await assetApi.update(payload)
      ElMessage.success('资产更新成功')
    } else {
      await assetApi.create(payload)
      ElMessage.success('资产新增成功')
    }

    dialogVisible.value = false
    resetDraft()
    await Promise.all([loadTree(), loadAssets()])
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '保存资产失败')
  } finally {
    submitting.value = false
  }
}

async function removeAsset(id: number) {
  await ElMessageBox.confirm('删除后不可恢复，确定继续吗？', '删除资产', {
    type: 'warning',
  })

  try {
    await assetApi.remove(id)
    ElMessage.success('资产删除成功')
    await Promise.all([loadTree(), loadAssets()])
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '删除资产失败')
  }
}

function resetDraft() {
  draft.id = undefined
  draft.parentId = 0
  draft.name = ''
  draft.type = 'SHOP'
  draft.area = 0
  draft.status = 'occupied'
  draft.ownerName = ''
  draft.ownerPhone = ''
}

function getTypeText(value: string) {
  const mapping: Record<string, string> = {
    BUILDING: '楼栋',
    UNIT: '单元',
    RESIDENTIAL: '住宅',
    SHOP: '商铺',
  }

  return mapping[value] || value
}

function getStatusText(value: string) {
  const mapping: Record<string, string> = {
    occupied: '已入住',
    vacant: '空置',
    sold: '已售',
  }

  return mapping[value] || value
}

function getStatusTone(value: string) {
  const mapping: Record<string, 'success' | 'warning' | 'info'> = {
    occupied: 'success',
    vacant: 'warning',
    sold: 'info',
  }

  return mapping[value] || 'info'
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

.pagination-wrap {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

.full-width {
  width: 100%;
}

@media (max-width: 1024px) {
  .asset-layout {
    grid-template-columns: 1fr;
  }
}
</style>
