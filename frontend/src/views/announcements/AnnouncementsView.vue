<template>
  <PageContainer title="定向公告发布" description="已接入公告列表与发布接口。">
    <template #actions>
      <el-button type="primary" class="btn-primary-gradient" @click="openCreate">新建公告</el-button>
    </template>

    <section class="announcement-grid" v-loading="loading">
      <PanelCard v-for="notice in notices" :key="notice.id" class="announcement-card">
        <template #header>
          <div class="announcement-card__status">
            <StatusBadge :label="notice.status" :tone="notice.status === 'published' ? 'success' : 'info'" />
            <span class="muted-text">{{ notice.createTime || '-' }}</span>
          </div>
        </template>
        <h3>{{ notice.title }}</h3>
        <p>{{ notice.content }}</p>
        <footer>
          <span>投递对象: {{ notice.targetType }}</span>
          <div>
            <el-button link type="primary" @click="openEdit(notice)">编辑</el-button>
            <el-button link type="primary" @click="openDetail(notice)">详情</el-button>
          </div>
        </footer>
      </PanelCard>
    </section>

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
          <el-input v-model="draft.content" type="textarea" rows="6" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="saveNotice">保存</el-button>
      </template>
    </el-dialog>

    <el-drawer v-model="detailVisible" title="公告详情" size="420px">
      <InfoList v-if="activeNotice" :items="detailItems" />
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
const notices = ref<NoticeRecord[]>([])
const activeNotice = ref<NoticeRecord | null>(null)

const draft = reactive({
  id: undefined as number | undefined,
  title: '',
  targetType: 'ALL',
  status: 'draft',
  content: '',
})

const detailItems = computed(() =>
  activeNotice.value
    ? [
        { label: '公告标题', value: activeNotice.value.title },
        { label: '推送对象', value: activeNotice.value.targetType },
        { label: '公告状态', value: activeNotice.value.status },
        { label: '浏览量', value: String(activeNotice.value.viewCount) },
        { label: '发布时间', value: activeNotice.value.createTime || '-' },
        { label: '公告内容', value: activeNotice.value.content },
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
      page: 1,
      pageSize: 20,
    })
    notices.value = result.records
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

.announcement-card p {
  min-height: 54px;
  margin: 0 0 16px;
  color: #7f90aa;
}

.announcement-card footer,
.announcement-card__status {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

@media (max-width: 900px) {
  .announcement-grid {
    grid-template-columns: 1fr;
  }
}
</style>
