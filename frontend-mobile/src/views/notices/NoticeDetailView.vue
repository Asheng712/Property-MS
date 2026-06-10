<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useNoticeStore } from '@/stores/notice'
import type { NoticeRecord } from '@/types'

import { noticeApi } from '@/services/api'

const router = useRouter()
const route = useRoute()
const noticeStore = useNoticeStore()

const notice = ref<NoticeRecord | null>(null)

onMounted(async () => {
  const id = Number(route.params.id)
  await noticeStore.fetchNotices()
  notice.value = noticeStore.notices.find((n) => n.id === id) || null
  if (notice.value) {
    noticeApi.incrementView(id).catch(() => {})
    notice.value.viewCount += 1
  }
})

</script>

<template>
  <div class="page-container--with-nav">
    <van-nav-bar title="公告详情" left-arrow @click-left="router.back()" />

    <div v-if="notice">
      <van-cell-group style="margin-top: 8px">
        <van-cell>
          <template #title>
            <h2 style="font-size: 18px; font-weight: 600; line-height: 1.5">{{ notice.title }}</h2>
          </template>
        </van-cell>
        <van-cell title="发布时间" :value="notice.createTime || '-'" />
        <van-cell title="浏览次数" :value="String(notice.viewCount)" />
      </van-cell-group>

      <van-cell-group title="正文" style="margin-top: 12px">
        <div class="notice-body" v-html="notice.content" />
      </van-cell-group>
    </div>

    <EmptyState v-else description="未找到公告" />
  </div>
</template>

<style scoped>
.notice-body {
  padding: 16px;
  font-size: 14px;
  line-height: 1.8;
  color: #334155;
}

.notice-body :deep(p) {
  margin: 0 0 10px;
}
</style>
