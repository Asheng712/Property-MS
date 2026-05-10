import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { authApi } from '@/services/api'
import { clearToken, setToken } from '@/services/http'
import type { LoginPayload, RegisterPayload, UserInfo } from '@/types'

const STORAGE_KEY = 'wisdompm-app-state'
const PROFILE_KEY = 'wisdompm-user-profile'

function safeParse<T>(raw: string | null): T | null {
  if (!raw) {
    return null
  }

  try {
    return JSON.parse(raw) as T
  } catch {
    return null
  }
}

function loadState() {
  if (typeof window === 'undefined') {
    return null
  }

  return safeParse<{
    isAuthenticated: boolean
    compactMode: boolean
    noticeSound: boolean
  }>(window.sessionStorage.getItem(STORAGE_KEY))
}

function loadProfile() {
  if (typeof window === 'undefined') {
    return null
  }

  return safeParse<UserInfo>(window.sessionStorage.getItem(PROFILE_KEY))
}

export const useAppStore = defineStore('app', () => {
  const savedState = loadState()
  const savedProfile = loadProfile()

  const sidebarCollapsed = ref(savedState?.compactMode ?? false)
  const sidebarDrawerOpen = ref(false)
  const isAuthenticated = ref(savedState?.isAuthenticated ?? false)
  const noticeSoundEnabled = ref(savedState?.noticeSound ?? true)
  const currentUserProfile = ref<UserInfo | null>(savedProfile)

  const sidebarWidth = computed(() => (sidebarCollapsed.value ? '88px' : '236px'))
  const currentUser = computed(
    () => currentUserProfile.value?.realName || currentUserProfile.value?.username || '未登录用户',
  )
  const currentRole = computed(() => currentUserProfile.value?.roleName || '未分配角色')
  const users = computed(() => (currentUserProfile.value ? [currentUserProfile.value] : []))
  const currentUserId = computed(() => currentUserProfile.value?.id ?? 0)

  function persistState() {
    if (typeof window === 'undefined') {
      return
    }

    window.localStorage.removeItem(STORAGE_KEY)
    window.sessionStorage.setItem(
      STORAGE_KEY,
      JSON.stringify({
        isAuthenticated: isAuthenticated.value,
        compactMode: sidebarCollapsed.value,
        noticeSound: noticeSoundEnabled.value,
      }),
    )
  }

  function persistProfile() {
    if (typeof window === 'undefined') {
      return
    }

    if (!currentUserProfile.value) {
      window.sessionStorage.removeItem(PROFILE_KEY)
      window.localStorage.removeItem(PROFILE_KEY)
      return
    }

    window.localStorage.removeItem(PROFILE_KEY)
    window.sessionStorage.setItem(PROFILE_KEY, JSON.stringify(currentUserProfile.value))
  }

  function toggleSidebar() {
    sidebarCollapsed.value = !sidebarCollapsed.value
    persistState()
  }

  function openSidebarDrawer() {
    sidebarDrawerOpen.value = true
  }

  function closeSidebarDrawer() {
    sidebarDrawerOpen.value = false
  }

  async function fetchCurrentUser() {
    const profile = await authApi.getInfo()
    currentUserProfile.value = profile
    isAuthenticated.value = true
    persistProfile()
    persistState()
    return profile
  }

  async function loginByCredential(payload: LoginPayload) {
    const token = await authApi.login(payload)
    setToken(token)
    const profile = await fetchCurrentUser()
    return profile
  }

  async function registerUser(payload: RegisterPayload) {
    await authApi.register(payload)
  }

  function logout() {
    isAuthenticated.value = false
    sidebarDrawerOpen.value = false
    currentUserProfile.value = null
    clearToken()
    persistProfile()
    persistState()
  }

  function switchUser(_userId: string) {
    // 最新后端尚未提供切换用户接口，保留方法避免布局组件报错。
  }

  function toggleNoticeSound(value?: boolean) {
    noticeSoundEnabled.value = typeof value === 'boolean' ? value : !noticeSoundEnabled.value
    persistState()
  }

  return {
    users,
    currentUser,
    currentRole,
    currentUserId,
    currentUserProfile,
    isAuthenticated,
    noticeSoundEnabled,
    sidebarCollapsed,
    sidebarDrawerOpen,
    sidebarWidth,
    toggleSidebar,
    openSidebarDrawer,
    closeSidebarDrawer,
    loginByCredential,
    fetchCurrentUser,
    logout,
    switchUser,
    toggleNoticeSound,
    registerUser,
  }
})
