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
          background-color="transparent"
          text-color="rgba(255,255,255,0.45)"
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
      <header class="topbar">
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
          <el-button circle plain @click="openSearch">
            <el-icon><Search /></el-icon>
          </el-button>
          <el-button circle plain @click="goHome">
            <el-icon><House /></el-icon>
          </el-button>
          <el-button circle plain @click="settingsVisible = true">
            <el-icon><Setting /></el-icon>
          </el-button>

          <el-dropdown trigger="click" @command="handleUserCommand">
            <button class="topbar__user topbar__user-btn" type="button">
              <div class="avatar">{{ appStore.currentUser.slice(0, 1) }}</div>
              <span>{{ appStore.currentUser }} / {{ appStore.currentRole }}</span>
              <el-icon><ArrowDown /></el-icon>
            </button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item disabled>{{ appStore.currentUser }} 当前在线</el-dropdown-item>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
                <el-dropdown-item command="register">前往注册</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>
      <main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </main>
    </div>

    <el-dialog v-model="searchVisible" title="全局搜索" width="520px">
      <el-input v-model="searchKeyword" placeholder="搜索页面、模块或功能..." clearable />
      <div class="search-result">
        <button
          v-for="item in filteredNavigation"
          :key="item.path"
          type="button"
          class="search-result__item"
          @click="goTo(item.path)"
        >
          <span>{{ item.label }}</span>
          <small>{{ item.path }}</small>
        </button>
      </div>
    </el-dialog>

    <el-drawer v-model="settingsVisible" title="系统设置" size="420px">
      <div class="settings-list">
        <div class="settings-item">
          <div>
            <strong>当前用户</strong>
            <p>{{ appStore.currentUser }} / {{ appStore.currentRole }}</p>
          </div>
        </div>
        <div class="settings-item">
          <div>
            <strong>紧凑侧边栏</strong>
            <p>适合小屏幕或高密度操作</p>
          </div>
          <el-switch :model-value="appStore.sidebarCollapsed" @change="handleCompactChange" />
        </div>
        <div class="settings-item">
          <div>
            <strong>返回登录页</strong>
            <p>退出当前登录并跳转到登录页</p>
          </div>
          <el-button plain @click="logout">退出</el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ArrowDown, Fold, House, Menu, Search, Setting } from '@element-plus/icons-vue'
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import BrandLogo from '@/components/BrandLogo.vue'
import { navigationItems } from '@/mock/data'
import { useAppStore } from '@/stores/app'

const appStore = useAppStore()
const route = useRoute()
const router = useRouter()

const searchVisible = ref(false)
const settingsVisible = ref(false)
const searchKeyword = ref('')

const currentTitle = computed(() => String(route.meta.title ?? '智慧物业管理系统'))
const filteredNavigation = computed(() =>
  navigationItems.filter(
    (item) => !searchKeyword.value || item.label.includes(searchKeyword.value) || item.path.includes(searchKeyword.value),
  ),
)

function openSearch() {
  searchVisible.value = true
}

function goHome() {
  router.push('/dashboard')
}

function goTo(path: string) {
  searchVisible.value = false
  router.push(path)
}

function handleCompactChange(value: boolean | string | number) {
  const shouldCollapse = value === true || value === 'true' || value === 1
  if (appStore.sidebarCollapsed !== shouldCollapse) {
    appStore.toggleSidebar()
  }
}

function handleUserCommand(command: string) {
  if (command === 'logout') {
    logout()
    return
  }

  if (command === 'profile') {
    router.push('/profile')
    return
  }

  if (command === 'register') {
    router.push('/register')
  }
}

function logout() {
  appStore.logout()
  settingsVisible.value = false
  router.push('/login')
}
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
  background: var(--bg-sidebar);
  transition: width 0.25s ease;
  overflow: hidden;
}

.sidebar__brand {
  display: flex;
  align-items: center;
  min-height: 80px;
  padding: 24px 22px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
}

:deep(.el-menu) {
  border-right: none;
  padding: 8px 0;
}

:deep(.el-menu-item) {
  margin: 2px 10px;
  border-radius: 8px;
  height: 44px;
  font-size: 14px;
  transition: all 0.15s ease;
}

:deep(.el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.06);
}

:deep(.el-menu-item.is-active) {
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
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
  min-height: 72px;
  margin: 0;
  padding: 0 32px;
  background: var(--bg-card);
  border-bottom: 1px solid var(--border-light);
}

.topbar__left,
.topbar__right {
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 0;
}

.topbar__breadcrumb {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--text-subtle);
  min-width: 0;
  font-size: 14px;
}

.topbar__breadcrumb strong {
  color: var(--text-heading);
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.topbar__user {
  display: flex;
  align-items: center;
  gap: 10px;
  padding-left: 6px;
  color: var(--text-subtle);
  max-width: 220px;
}

.topbar__user-btn {
  border: none;
  background: transparent;
  cursor: pointer;
  font-size: 14px;
}

.topbar__user span {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.avatar {
  display: grid;
  place-items: center;
  width: 34px;
  height: 34px;
  border-radius: 50%;
  background: var(--brand);
  color: #fff;
  font-weight: 600;
  font-size: 14px;
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

.search-result {
  display: grid;
  gap: 8px;
  margin-top: 20px;
}

.search-result__item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 16px 18px;
  border: 1px solid var(--border-light);
  border-radius: var(--radius-control);
  background: var(--bg-card);
  cursor: pointer;
  transition: background 0.15s ease;
}

.search-result__item:hover {
  background: var(--bg-hover);
}

.search-result__item small {
  color: var(--text-subtle);
}

.settings-list {
  display: grid;
  gap: 24px;
}

.settings-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding-bottom: 20px;
  border-bottom: 1px solid var(--border-card);
}

.settings-item strong {
  color: var(--text-heading);
  font-weight: 500;
}

.settings-item p {
  margin: 6px 0 0;
  color: var(--text-subtle);
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
    padding: 0 20px;
    min-height: 64px;
  }

  .topbar__breadcrumb {
    font-size: 13px;
  }
}

@media (max-width: 768px) {
  .topbar__right .el-button:nth-child(-n + 2) {
    display: none;
  }

  .topbar__user span {
    display: none;
  }

  .topbar {
    gap: 8px;
  }

  .topbar__left,
  .topbar__right {
    gap: 8px;
  }
}

@media (max-width: 560px) {
  .topbar__breadcrumb span:first-child,
  .topbar__breadcrumb span:nth-child(2) {
    display: none;
  }

  :deep(.el-dialog) {
    width: calc(100vw - 28px) !important;
  }

  :deep(.el-drawer) {
    width: min(100vw, 360px) !important;
  }

  .search-result__item {
    align-items: flex-start;
    flex-direction: column;
  }
}
</style>
