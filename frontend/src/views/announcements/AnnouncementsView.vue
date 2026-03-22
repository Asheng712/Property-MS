<template>
  <PageContainer title="定向公告发布" description="支持按楼栋、角色或车主群体定向下发公告，提升通知触达效率。">
    <template #actions>
      <el-button type="primary" class="btn-primary-gradient" @click="dialogVisible = true">新建公告</el-button>
    </template>

    <section class="announcement-grid">
      <article v-for="notice in noticeRecords" :key="notice.id" class="surface-card announcement-card">
        <div class="announcement-card__status">
          <span class="status-pill" :class="notice.status === 'published' ? 'success' : 'info'">
            {{ notice.status === 'published' ? '已发布' : '草稿' }}
          </span>
          <span class="muted-text">{{ notice.publishAt }}</span>
        </div>
        <h3>{{ notice.title }}</h3>
        <p>{{ notice.summary }}</p>
        <footer>
          <span>投递对象: {{ notice.audience }}</span>
          <el-button link type="primary">查看详情</el-button>
        </footer>
      </article>
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
  </PageContainer>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import PageContainer from '@/components/PageContainer.vue'
import { noticeRecords } from '@/mock/data'

const dialogVisible = ref(false)

const draft = reactive({
  title: '',
  audience: '全体业主',
  summary: '',
})

function saveDraft() {
  dialogVisible.value = false
  ElMessage.success('公告草稿已保存')
}

function publishNotice() {
  dialogVisible.value = false
  ElMessage.success('公告已发布到目标对象')
}
</script>

<style scoped>
.announcement-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 20px;
}

.announcement-card h3 {
  margin: 16px 0 10px;
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
