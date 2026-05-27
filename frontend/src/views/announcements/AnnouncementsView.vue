<template>
  <PageContainer title="定向公告发布" description="维护面向业主、住户和商户的公告内容。">
    <template #actions>
      <el-button type="primary" class="btn-primary-gradient" @click="openCreate">新建公告</el-button>
    </template>

    <section class="announcement-grid" v-loading="loading">
      <PanelCard v-for="notice in notices" :key="notice.id" class="announcement-card">
        <template #header>
          <div class="announcement-card__status">
            <StatusBadge :label="getStatusText(notice.status)" :tone="getStatusTone(notice.status)" />
            <span class="muted-text">{{ notice.createTime || '-' }}</span>
          </div>
        </template>
        <h3>{{ notice.title }}</h3>
        <p class="content-preview">{{ stripHtml(notice.content) }}</p>
        <footer>
          <span>投递对象: {{ getTargetText(notice.targetType) }}</span>
          <div>
            <el-button link type="primary" @click="openEdit(notice)">编辑</el-button>
            <el-button link type="primary" @click="openDetail(notice)">详情</el-button>
          </div>
        </footer>
      </PanelCard>
    </section>

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

    <el-dialog v-model="dialogVisible" :title="draft.id ? '编辑公告' : '新建公告'" width="560px">
      <el-form label-position="top" class="dialog-form">
        <el-form-item label="公告标题">
          <el-input v-model="draft.title" />
        </el-form-item>
        <el-form-item label="推送对象">
          <el-select v-model="draft.targetType">
            <el-option label="全体业主" value="ALL" />
            <el-option label="住户" value="RESIDENT" />
            <el-option label="商铺租户" value="TENANT" />
          </el-select>
        </el-form-item>
        <el-form-item label="公告状态">
          <el-select v-model="draft.status">
            <el-option label="草稿" value="draft" />
            <el-option label="已发布" value="published" />
          </el-select>
        </el-form-item>
        <el-form-item label="公告内容">
          <el-input v-model="draft.content" type="textarea" rows="8" placeholder="支持HTML格式：<p>段落</p> <b>加粗</b> <br>换行 <ul><li>列表</li></ul>" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="saveNotice">保存</el-button>
      </template>
    </el-dialog>

    <el-drawer v-model="detailVisible" title="公告详情" size="500px">
      <template v-if="activeNotice">
        <InfoList :items="detailItems" />
        <div class="content-html" v-html="activeNotice.content" />
      </template>
    </el-drawer>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import InfoList from '@/components/InfoList.vue'
import PageContainer from '@/components/PageContainer.vue'
import PanelCard from '@/components/PanelCard.vue'
import StatusBadge from '@/components/StatusBadge.vue'
import { noticeApi } from '@/services/api'
import type { NoticeRecord } from '@/types'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const detailVisible = ref(false)
const total = ref(0)
const notices = ref<NoticeRecord[]>([])
const activeNotice = ref<NoticeRecord | null>(null)

const draft = reactive({
  id: undefined as number | undefined,
  title: '',
  targetType: 'ALL',
  status: 'draft',
  content: '',
})

const query = reactive({
  page: 1,
  pageSize: 20,
})

const detailItems = computed(() =>
  activeNotice.value
    ? [
        { label: '公告标题', value: activeNotice.value.title },
        { label: '推送对象', value: getTargetText(activeNotice.value.targetType) },
        { label: '公告状态', value: getStatusText(activeNotice.value.status) },
        { label: '浏览量', value: String(activeNotice.value.viewCount) },
        { label: '发布时间', value: activeNotice.value.createTime || '-' },
      ]
    : [],
)

onMounted(() => {
  void loadNotices()
})

async function loadNotices() {
  loading.value = true
  try {
    const result = await noticeApi.getList({
      page: query.page,
      pageSize: query.pageSize,
    })
    notices.value = result.records
    total.value = result.total
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '加载公告失败')
  } finally {
    loading.value = false
  }
}

function openCreate() {
  resetDraft()
  dialogVisible.value = true
}

function handlePageChange(page: number) {
  query.page = page
  void loadNotices()
}

function handleSizeChange(size: number) {
  query.page = 1
  query.pageSize = size
  void loadNotices()
}

function openEdit(notice: NoticeRecord) {
  draft.id = notice.id
  draft.title = notice.title
  draft.targetType = notice.targetType
  draft.status = notice.status
  draft.content = notice.content
  dialogVisible.value = true
}

function openDetail(notice: NoticeRecord) {
  activeNotice.value = notice
  detailVisible.value = true
}

async function saveNotice() {
  if (!draft.title.trim()) {
    ElMessage.warning('请输入公告标题')
    return
  }

  submitting.value = true
  try {
    await noticeApi.save({
      id: draft.id,
      title: draft.title.trim(),
      targetType: draft.targetType,
      status: draft.status,
      content: draft.content.trim(),
    })
    ElMessage.success(draft.id ? '公告更新成功' : '公告发布成功')
    dialogVisible.value = false
    resetDraft()
    await loadNotices()
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '保存公告失败')
  } finally {
    submitting.value = false
  }
}

function resetDraft() {
  draft.id = undefined
  draft.title = ''
  draft.targetType = 'ALL'
  draft.status = 'draft'
  draft.content = ''
}

function getTargetText(value: string) {
  const mapping: Record<string, string> = {
    ALL: '全体业主',
    RESIDENT: '住户',
    TENANT: '商铺租户',
  }

  return mapping[value] ?? value
}

function getStatusText(value: string) {
  const mapping: Record<string, string> = {
    draft: '草稿',
    published: '已发布',
  }

  return mapping[value] ?? value
}

function getStatusTone(value: string) {
  return value === 'published' ? 'success' : 'info'
}

function stripHtml(html: string) {
  const div = document.createElement('div')
  div.innerHTML = html
  return (div.textContent || '').slice(0, 120)
}
</script>

<style scoped>
.announcement-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 20px;
}

.announcement-card h3 {
  margin: 0 0 10px;
  font-size: 24px;
  color: #22304a;
}

.announcement-card .content-preview {
  min-height: 54px;
  margin: 0 0 16px;
  color: var(--text-subtle);
  display: -webkit-box;
  overflow: hidden;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
}

.content-html {
  margin-top: 20px;
  padding: 16px;
  background: #f8fbff;
  border: 1px solid #edf1f7;
  border-radius: 12px;
  line-height: 1.8;
  color: #334155;
}

.content-html :deep(p) {
  margin: 0 0 10px;
}

.content-html :deep(ul) {
  padding-left: 20px;
}

.announcement-card footer,
.announcement-card__status {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.pagination-wrap {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

@media (max-width: 768px) {
  .announcement-grid {
    grid-template-columns: 1fr;
  }

}
</style>
