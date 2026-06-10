<script setup lang="ts">
import { onActivated } from 'vue'
import { useRouter } from 'vue-router'
import { useNoticeStore } from '@/stores/notice'

const router = useRouter()
const noticeStore = useNoticeStore()

onActivated(() => {
  if (noticeStore.notices.length === 0) {
    noticeStore.fetchNotices()
  }
})

</script>

<template>
  <div class="page-container">
    <van-nav-bar title="社区公告" fixed placeholder />

    <van-pull-refresh
      v-model="noticeStore.refreshing"
      @refresh="noticeStore.onRefresh"
    >
      <van-list
        v-model:loading="noticeStore.loading"
        :finished="noticeStore.finished"
        finished-text="没有更多了"
        @load="noticeStore.onLoad"
      >
        <div
          v-for="item in noticeStore.notices"
          :key="item.id"
          class="card-wrapper"
          @click="router.push(`/notices/${item.id}`)"
        >
          <div style="padding: 14px 16px">
            <h4 style="font-size: 15px; margin-bottom: 6px; line-height: 1.4">{{ item.title }}</h4>
            <div style="display: flex; justify-content: space-between; align-items: center">
              <span style="font-size: 12px; color: var(--van-text-color-weak)">
                {{ item.createTime || '-' }}
              </span>
              <div style="display: flex; align-items: center; gap: 8px">
                <span style="font-size: 12px; color: var(--van-text-color-weak)">
                  <van-icon name="eye-o" size="12" /> {{ item.viewCount }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <EmptyState v-if="!noticeStore.loading && noticeStore.notices.length === 0" description="暂无公告" />
      </van-list>
    </van-pull-refresh>
  </div>
</template>
