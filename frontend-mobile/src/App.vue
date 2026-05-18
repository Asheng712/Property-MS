<script setup lang="ts">
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const tabRoutes = ['/home', '/bills', '/services', '/notices', '/profile']

const showTabbar = computed(() => {
  return userStore.isLoggedIn && tabRoutes.includes(route.path)
})

const tabs = [
  { path: '/home', name: '首页', icon: 'home-o' },
  { path: '/bills', name: '账单', icon: 'gold-coin-o' },
  { path: '/services', name: '服务', icon: 'service-o' },
  { path: '/notices', name: '公告', icon: 'bullhorn-o' },
  { path: '/profile', name: '我的', icon: 'user-o' },
]

function getActive() {
  return tabs.findIndex((t) => t.path === route.path)
}

function onTabChange(idx: number) {
  const tab = tabs[idx]
  if (tab) {
    router.replace(tab.path)
  }
}
</script>

<template>
  <router-view v-slot="{ Component }">
    <keep-alive :include="['HomeView', 'BillListView', 'NoticeListView', 'ProfileView']">
      <component :is="Component" />
    </keep-alive>
  </router-view>

  <van-tabbar
    v-if="showTabbar"
    :model-value="getActive()"
    active-color="#1989fa"
    inactive-color="#969799"
    safe-area-inset-bottom
    @change="onTabChange"
  >
    <van-tabbar-item
      v-for="tab in tabs"
      :key="tab.path"
      :icon="tab.icon"
    >
      {{ tab.name }}
    </van-tabbar-item>
  </van-tabbar>
</template>
