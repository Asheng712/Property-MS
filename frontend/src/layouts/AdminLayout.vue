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
        <router-view />
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
            <strong>消息提示音</strong>
            <p>用于报修、收费等业务提醒</p>
          </div>
          <el-switch :model-value="appStore.noticeSoundEnabled" @change="handleNoticeSoundChange" />
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
import { ElMessage } from 'element-plus'
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
  const shouldCollapse = Boolean(value)
  if (appStore.sidebarCollapsed !== shouldCollapse) {
    appStore.toggleSidebar()
  }
}

function handleNoticeSoundChange(value: boolean | string | number) {
  appStore.toggleNoticeSound(Boolean(value))
  ElMessage.success(Boolean(value) ? '已开启消息提示音' : '已关闭消息提示音')
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

.topbar__user-btn {
  border: none;
  background: transparent;
  cursor: pointer;
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

.search-result {
  display: grid;
  gap: 10px;
  margin-top: 16px;
}

.search-result__item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 14px 16px;
  border: 1px solid #e8eef6;
  border-radius: 14px;
  background: #fbfdff;
  cursor: pointer;
}

.search-result__item small {
  color: #93a1b7;
}

.settings-list {
  display: grid;
  gap: 18px;
}

.settings-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #edf1f7;
}

.settings-item strong {
  color: #24344e;
}

.settings-item p {
  margin: 6px 0 0;
  color: #8ea0b8;
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
