import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { authApi } from '@/services/api'
import { setToken, clearToken, getToken } from '@/services/http'
import type { LoginPayload, RegisterPayload, UserInfo } from '@/types'

export const useUserStore = defineStore('user', () => {
  const token = ref(getToken())
  const userInfo = ref<UserInfo | null>(null)

  const isLoggedIn = computed(() => !!token.value)
  const currentUser = computed(() => userInfo.value?.realName || userInfo.value?.username || '未知用户')

  async function login(payload: LoginPayload) {
    const t = await authApi.login(payload)
    setToken(t)
    token.value = t
    await fetchUserInfo()
  }

  async function register(payload: RegisterPayload) {
    await authApi.register(payload)
  }

  async function fetchUserInfo() {
    const info = await authApi.getInfo()
    userInfo.value = info
  }

  function logout() {
    clearToken()
    token.value = null
    userInfo.value = null
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    currentUser,
    login,
    register,
    fetchUserInfo,
    logout,
  }
})
