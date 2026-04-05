<template>
  <PageContainer title="定向公告发布" description="支持按楼栋、角色或车主群体定向下发公告，支持草稿保存、发布和详情预览。">
    <template #actions>
      <el-button type="primary" class="btn-primary-gradient" @click="openCreate">新建公告</el-button>
    </template>

    <section class="announcement-grid">
      <PanelCard v-for="notice in notices" :key="notice.id" class="announcement-card">
        <template #header>
          <div class="announcement-card__status">
            <StatusBadge :label="notice.status === 'published' ? '已发布' : '草稿'" :tone="notice.status === 'published' ? 'success' : 'info'" />
            <span class="muted-text">{{ notice.publishAt }}</span>
          </div>
        </template>
        <h3>{{ notice.title }}</h3>
        <p>{{ notice.summary }}</p>
        <footer>
          <span>投递对象: {{ notice.audience }}</span>
          <div>
            <el-button v-if="notice.status === 'draft'" link type="primary" @click="publishExisting(notice.id)">发布</el-button>
            <el-button link type="primary" @click="openDetail(notice.id)">详情</el-button>
          </div>
        </footer>
      </PanelCard>
    </section>

    <el-dialog v-model="dialogVisible" title="新建公告" width="560px">
      <el-form label-position="top" class="dialog-form">
        <el-form-item label="公告标题">
          <el-input v-model="draft.title" />
        </el-form-item>
        <el-form-item label="推送对象">
          <el-select v-model="draft.audience">
            <el-option label="全体业主" value="全体业主" />
            <el-option label="A区车主" value="A区车主" />
            <el-option label="商铺租户" value="商铺租户" />
          </el-select>
        </el-form-item>
        <el-form-item label="公告内容">
          <el-input v-model="draft.summary" type="textarea" rows="6" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="saveDraft">存为草稿</el-button>
        <el-button type="primary" @click="publishNotice">立即发布</el-button>
      </template>
    </el-dialog>

    <el-drawer v-model="detailVisible" title="公告详情" size="420px">
      <InfoList v-if="activeNotice" :items="detailItems" />
    </el-drawer>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import InfoList from '@/components/InfoList.vue'
import PageContainer from '@/components/PageContainer.vue'
import PanelCard from '@/components/PanelCard.vue'
import StatusBadge from '@/components/StatusBadge.vue'
import { noticeRecords } from '@/mock/data'
import type { NoticeRecord } from '@/types'
import { createLocalId, nowText } from '@/utils/format'

const dialogVisible = ref(false)
const detailVisible = ref(false)
const activeId = ref('')
const notices = ref<NoticeRecord[]>(noticeRecords.map((item) => ({ ...item })))

const draft = reactive({
  title: '',
  audience: '全体业主',
  summary: '',
})

const activeNotice = computed(() => notices.value.find((item) => item.id === activeId.value) ?? null)
const detailItems = computed(() =>
  activeNotice.value
    ? [
        { label: '公告标题', value: activeNotice.value.title },
        { label: '推送对象', value: activeNotice.value.audience },
        { label: '发布时间', value: activeNotice.value.publishAt },
        { label: '状态', value: activeNotice.value.status === 'published' ? '已发布' : '草稿' },
        { label: '摘要', value: activeNotice.value.summary },
      ]
    : [],
)

function openCreate() {
  draft.title = ''
  draft.audience = '全体业主'
  draft.summary = ''
  dialogVisible.value = true
}

function saveDraft() {
  if (!draft.title.trim()) {
    ElMessage.warning('请先填写公告标题')
    return
  }
  notices.value = [
    {
      id: createLocalId('notice'),
      title: draft.title,
      audience: draft.audience,
      publishAt: nowText(),
      status: 'draft',
      summary: draft.summary,
    },
    ...notices.value,
  ]
  dialogVisible.value = false
  ElMessage.success('公告草稿已保存')
}

function publishNotice() {
  if (!draft.title.trim()) {
    ElMessage.warning('请先填写公告标题')
    return
  }
  notices.value = [
    {
      id: createLocalId('notice'),
      title: draft.title,
      audience: draft.audience,
      publishAt: nowText(),
      status: 'published',
      summary: draft.summary,
    },
    ...notices.value,
  ]
  dialogVisible.value = false
  ElMessage.success('公告已发布到目标对象')
}

function publishExisting(noticeId: string) {
  notices.value = notices.value.map((item) =>
    item.id === noticeId ? { ...item, status: 'published', publishAt: nowText() } : item,
  )
  ElMessage.success('草稿公告已发布')
}

function openDetail(noticeId: string) {
  activeId.value = noticeId
  detailVisible.value = true
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
