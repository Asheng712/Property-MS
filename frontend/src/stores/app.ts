import { computed, ref } from 'vue'
import { defineStore } from 'pinia'

interface UserProfile {
  id: string
  name: string
  role: string
  phone?: string
  password: string
  projectName?: string
}

const STORAGE_KEY = 'wisdompm-app-state'
const USERS_STORAGE_KEY = 'wisdompm-users'

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
    currentUserId: string
    compactMode: boolean
    noticeSound: boolean
  }>(window.localStorage.getItem(STORAGE_KEY))
}

function loadUsers() {
  if (typeof window === 'undefined') {
    return null
  }

  return safeParse<UserProfile[]>(window.localStorage.getItem(USERS_STORAGE_KEY))
}

export const useAppStore = defineStore('app', () => {
  const savedState = loadState()
  const savedUsers = loadUsers()

  const users = ref<UserProfile[]>(
    savedUsers ?? [
      { id: 'admin', name: '管理员', role: '超级管理员', phone: '13800000000', password: '123456', projectName: '智慧物业管理系统' },
      { id: 'finance', name: '财务经理', role: '财务经理', phone: '13800000001', password: '123456', projectName: '智慧物业管理系统' },
      { id: 'service', name: '前台客服', role: '前台客服', phone: '13800000002', password: '123456', projectName: '智慧物业管理系统' },
      { id: 'repair', name: '维修主管', role: '维修工程部', phone: '13800000003', password: '123456', projectName: '智慧物业管理系统' },
    ],
  )

  const sidebarCollapsed = ref(savedState?.compactMode ?? false)
  const sidebarDrawerOpen = ref(false)
  const isAuthenticated = ref(savedState?.isAuthenticated ?? false)
  const currentUserId = ref(savedState?.currentUserId ?? users.value[0].id)
  const noticeSoundEnabled = ref(savedState?.noticeSound ?? true)

  const sidebarWidth = computed(() => (sidebarCollapsed.value ? '88px' : '236px'))
  const currentUserProfile = computed(
    () => users.value.find((item) => item.id === currentUserId.value) ?? users.value[0],
  )
  const currentUser = computed(() => currentUserProfile.value.name)
  const currentRole = computed(() => currentUserProfile.value.role)

  function persistState() {
    if (typeof window === 'undefined') {
      return
    }

    window.localStorage.setItem(
      STORAGE_KEY,
      JSON.stringify({
        isAuthenticated: isAuthenticated.value,
        currentUserId: currentUserId.value,
        compactMode: sidebarCollapsed.value,
        noticeSound: noticeSoundEnabled.value,
      }),
    )
  }

  function persistUsers() {
    if (typeof window === 'undefined') {
      return
    }

    window.localStorage.setItem(USERS_STORAGE_KEY, JSON.stringify(users.value))
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

  function login(userId = 'admin') {
    isAuthenticated.value = true
    currentUserId.value = userId
    persistState()
  }

  function loginByCredential(account: string, password: string, role?: string) {
    const normalized = account.trim()
    const matched = users.value.find((item) => {
      const accountMatches =
        item.id === normalized ||
        item.name === normalized ||
        item.phone === normalized
      const roleMatches = !role || item.role === role
      return accountMatches && item.password === password && roleMatches
    })

    if (!matched) {
      return null
    }

    login(matched.id)
    return matched
  }

  function logout() {
    isAuthenticated.value = false
    sidebarDrawerOpen.value = false
    persistState()
  }

  function switchUser(userId: string) {
    currentUserId.value = userId
    persistState()
  }

  function toggleNoticeSound(value?: boolean) {
    noticeSoundEnabled.value = typeof value === 'boolean' ? value : !noticeSoundEnabled.value
    persistState()
  }

  function registerUser(payload: {
    company: string
    name: string
    mobile: string
    password: string
    role: string
  }) {
    const existing = users.value.find((item) => item.phone === payload.mobile)
    if (existing) {
      return { ok: false as const, message: '该手机号已注册，请直接登录' }
    }

    const user: UserProfile = {
      id: `user-${Date.now()}`,
      name: payload.name,
      role: payload.role,
      phone: payload.mobile,
      password: payload.password,
      projectName: payload.company,
    }

    users.value = [user, ...users.value]
    persistUsers()
    return { ok: true as const, user }
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
    login,
    loginByCredential,
    logout,
    switchUser,
    toggleNoticeSound,
    registerUser,
  }
})
