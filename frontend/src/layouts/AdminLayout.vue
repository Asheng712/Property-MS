<template>
  <div class="layout-shell">
    <aside class="sidebar" :style="{ width: appStore.sidebarWidth }">
      <div class="sidebar__brand">
        <BrandLogo :compact="appStore.sidebarCollapsed" />
      </div>
      <el-scrollbar>
        <el-menu
          :collapse="appStore.sidebarCollapsed"
          :default-active="route.path"
          background-color="#0f172a"
          text-color="#8ca0bd"
          active-text-color="#ffffff"
          router
        >
          <el-menu-item v-for="item in navigationItems" :key="item.path" :index="item.path">
            <el-icon><component :is="item.icon" /></el-icon>
            <template #title>{{ item.label }}</template>
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </aside>

    <el-drawer
      v-model="appStore.sidebarDrawerOpen"
      direction="ltr"
      size="260px"
      :with-header="false"
      class="mobile-sidebar"
      @close="appStore.closeSidebarDrawer"
    >
      <div class="mobile-sidebar__inner">
        <div class="sidebar__brand sidebar__brand--mobile">
          <BrandLogo />
        </div>
        <el-menu :default-active="route.path" router @select="appStore.closeSidebarDrawer">
          <el-menu-item v-for="item in navigationItems" :key="item.path" :index="item.path">
            <el-icon><component :is="item.icon" /></el-icon>
            <template #title>{{ item.label }}</template>
          </el-menu-item>
        </el-menu>
      </div>
    </el-drawer>

    <div class="layout-main">
      <header class="topbar glass-card">
        <div class="topbar__left">
          <el-button circle plain class="mobile-only" @click="appStore.openSidebarDrawer">
            <el-icon><Menu /></el-icon>
          </el-button>
          <el-button circle plain class="desktop-only" @click="appStore.toggleSidebar">
            <el-icon><Fold /></el-icon>
          </el-button>
          <div class="topbar__breadcrumb">
            <span>系统</span>
            <span>/</span>
            <strong>{{ currentTitle }}</strong>
          </div>
        </div>
        <div class="topbar__right">
          <el-button circle plain><el-icon><Search /></el-icon></el-button>
          <el-button circle plain><el-icon><FullScreen /></el-icon></el-button>
          <el-button circle plain><el-icon><Setting /></el-icon></el-button>
          <div class="topbar__user">
            <div class="avatar">{{ appStore.currentUser.slice(0, 1) }}</div>
            <span>{{ appStore.currentUser }} (退出)</span>
          </div>
        </div>
      </header>
      <main class="main-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Fold, FullScreen, Menu, Search, Setting } from '@element-plus/icons-vue'
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import BrandLogo from '@/components/BrandLogo.vue'
import { navigationItems } from '@/mock/data'
import { useAppStore } from '@/stores/app'

const appStore = useAppStore()
const route = useRoute()

const currentTitle = computed(() => String(route.meta.title ?? '智慧物业管理系统'))
</script>

<style scoped>
.layout-shell {
  display: flex;
  min-height: 100vh;
  background: var(--bg-page);
}

.sidebar {
  position: sticky;
  top: 0;
  height: 100vh;
  background: linear-gradient(180deg, #0f172a 0%, #101b34 100%);
  transition: width 0.25s ease;
  overflow: hidden;
}

.sidebar__brand {
  display: flex;
  align-items: center;
  min-height: 86px;
  padding: 22px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
}

:deep(.el-menu) {
  border-right: none;
}

:deep(.el-menu-item) {
  margin: 10px 10px 0;
  border-radius: 14px;
  height: 48px;
}

:deep(.el-menu-item.is-active) {
  background: linear-gradient(135deg, #2b6dff, #275ae1);
}

.layout-main {
  flex: 1;
  min-width: 0;
}

.topbar {
  position: sticky;
  top: 0;
  z-index: 20;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  min-height: 86px;
  margin: 18px 18px 0;
  padding: 0 24px;
  border-radius: 22px;
}

.topbar__left,
.topbar__right {
  display: flex;
  align-items: center;
  gap: 14px;
}

.topbar__breadcrumb {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #93a1b7;
}

.topbar__breadcrumb strong {
  color: #24344e;
}

.topbar__user {
  display: flex;
  align-items: center;
  gap: 10px;
  padding-left: 6px;
  color: #5f6f89;
}

.avatar {
  display: grid;
  place-items: center;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #dbeafe;
  color: #2563eb;
  font-weight: 700;
}

.main-content {
  min-width: 0;
}

.mobile-only {
  display: none;
}

.sidebar__brand--mobile {
  padding: 0 0 18px;
}

@media (max-width: 1024px) {
  .sidebar {
    display: none;
  }

  .mobile-only {
    display: inline-flex;
  }

  .desktop-only {
    display: none;
  }

  .topbar {
    margin: 12px 12px 0;
    padding: 0 16px;
  }

  .topbar__breadcrumb {
    font-size: 14px;
  }
}

@media (max-width: 768px) {
  .topbar__right .el-button:nth-child(-n + 2) {
    display: none;
  }

  .topbar__user span {
    display: none;
  }
}
</style>
