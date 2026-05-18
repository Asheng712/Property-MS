import axios from 'axios'
import { showToast } from 'vant'
import type { ApiResponse } from '@/types'

const TOKEN_KEY = 'wisdompm-mobile-token'

const http = axios.create({
  baseURL: '/api/v1',
  timeout: 15000,
})

http.interceptors.request.use((config) => {
  const token = sessionStorage.getItem(TOKEN_KEY)
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

http.interceptors.response.use(
  (response) => {
    const result = response.data as ApiResponse<unknown>
    if (result.code !== 200) {
      if (result.code === 401 || result.code === 403) {
        sessionStorage.removeItem(TOKEN_KEY)
        window.location.hash = '#/login'
      }
      showToast(result.msg || '请求失败')
      return Promise.reject(new Error(result.msg))
    }
    return result.data as any
  },
  (error) => {
    showToast('网络异常，请稍后重试')
    return Promise.reject(error)
  },
)

export function getToken() {
  return sessionStorage.getItem(TOKEN_KEY) || ''
}

export function setToken(token: string) {
  sessionStorage.setItem(TOKEN_KEY, token)
}

export function clearToken() {
  sessionStorage.removeItem(TOKEN_KEY)
}

export default http
