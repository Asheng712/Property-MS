<template>
  <PageContainer title="资产管理" description="维护楼栋、房屋和商铺等基础资产信息。">
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
        <div class="search-bar">
          <el-input v-model="keyword" placeholder="搜索资产名称" clearable class="search-input">
            <template #prefix><el-icon><Search /></el-icon></template>
          </el-input>
          <el-select v-model="status" clearable placeholder="筛选资产状态" class="search-select">
            <el-option v-for="item in statusFilters" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>

        <PanelCard title="资产台账">
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

  </PageContainer>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import PageContainer from '@/components/PageContainer.vue'
import PanelCard from '@/components/PanelCard.vue'
import StatusBadge from '@/components/StatusBadge.vue'
import { assetApi } from '@/services/api'
import { formatArea } from '@/utils/format'
import type { AssetRecord, AssetTreeNode } from '@/types'

const loading = ref(false)
const router = useRouter()
const submitting = ref(false)
const dialogVisible = ref(false)
const keyword = ref('')
const status = ref('')
const total = ref(0)
const selectedTreeNodeId = ref<number | null>(null)
const assets = ref<AssetRecord[]>([])
const assetTree = ref<AssetTreeNode[]>([])

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
  status: 'OCCUPIED',
  ownerName: '',
  ownerPhone: '',
})

const statusFilters = [
  { label: '已入住', value: 'OCCUPIED' },
  { label: '已出租', value: 'RENTING' },
  { label: '空置', value: 'VACANT' },
  { label: '已售', value: 'SOLD' },
  { label: '装修中', value: 'DECORATING' },
]

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
      parentId: selectedTreeNodeId.value || undefined,
    })

    assets.value = result.records
    total.value = result.total
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '加载资产列表失败')
  } finally {
    loading.value = false
  }
}

function handleNodeClick(node: AssetTreeNode) {
  selectedTreeNodeId.value = node.id
  query.page = 1
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
  router.push(`/assets/${asset.id}`)
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
  draft.status = 'OCCUPIED'
  draft.ownerName = ''
  draft.ownerPhone = ''
}

function getTypeText(value: string) {
  const normalized = value.trim().toUpperCase()
  const mapping: Record<string, string> = {
    BUILDING: '楼栋',
    UNIT: '单元',
    RESIDENTIAL: '住宅',
    SHOP: '商铺',
  }

  return mapping[normalized] || value
}

function getStatusText(value: string) {
  const normalized = value.trim().toLowerCase()
  const mapping: Record<string, string> = {
    occupied: '已入住',
    renting: '已出租',
    vacant: '空置',
    sold: '已售',
    decorating: '装修中',
  }
  return mapping[normalized] || value
}

function getStatusTone(value: string) {
  const normalized = value.trim().toLowerCase()
  const mapping: Record<string, 'success' | 'warning' | 'info'> = {
    occupied: 'success',
    renting: 'success',
    vacant: 'warning',
    sold: 'info',
    decorating: 'warning',
  }
  return mapping[normalized] || 'info'
}
</script>

<style scoped>
.asset-layout {
  display: grid;
  grid-template-columns: 280px minmax(0, 1fr);
  gap: 24px;
}

.tree-panel {
  min-height: 620px;
}

.asset-main {
  display: grid;
  gap: 24px;
}

.pagination-wrap {
  display: flex;
  justify-content: flex-end;
  margin-top: 24px;
}

.full-width {
  width: 100%;
}

.search-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 20px 24px;
  background: var(--bg-card);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-card);
}

.search-input {
  max-width: 280px;
}

.search-select {
  max-width: 180px;
}

@media (max-width: 1024px) {
  .asset-layout {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .search-bar {
    flex-wrap: wrap;
  }

  .search-input,
  .search-select {
    max-width: none;
    flex: 1;
    min-width: 140px;
  }
}
</style>
