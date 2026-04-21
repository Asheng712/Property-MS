import type { ApiResponse } from '@/types'

type Primitive = string | number | boolean

interface RequestOptions extends Omit<RequestInit, 'body'> {
  body?: BodyInit | object
  query?: object
  skipJson?: boolean
}

const TOKEN_KEY = 'wisdompm-token'

export function getToken() {
  if (typeof window === 'undefined') {
    return ''
  }

  return window.localStorage.getItem(TOKEN_KEY) || ''
}

export function setToken(token: string) {
  if (typeof window !== 'undefined') {
    window.localStorage.setItem(TOKEN_KEY, token)
  }
}

export function clearToken() {
  if (typeof window !== 'undefined') {
    window.localStorage.removeItem(TOKEN_KEY)
  }
}

function buildUrl(path: string, query?: RequestOptions['query']) {
  const url = new URL(path, window.location.origin)

  if (query) {
    Object.entries(query as Record<string, Primitive | null | undefined>).forEach(([key, value]) => {
      if (value === undefined || value === null || value === '') {
        return
      }

      url.searchParams.set(key, String(value))
    })
  }

  return `${url.pathname}${url.search}`
}

export async function request<T>(path: string, options: RequestOptions = {}) {
  const { body, headers, query, skipJson, ...rest } = options
  const token = getToken()
  const isFormData = typeof FormData !== 'undefined' && body instanceof FormData
  const nextHeaders = new Headers(headers)

  if (token) {
    nextHeaders.set('Authorization', `Bearer ${token}`)
  }

  if (!skipJson && body !== undefined && !isFormData) {
    nextHeaders.set('Content-Type', 'application/json')
  }

  const response = await fetch(buildUrl(path, query), {
    ...rest,
    headers: nextHeaders,
    body:
      body === undefined
        ? undefined
        : skipJson || isFormData
          ? (body as BodyInit)
          : JSON.stringify(body),
  })

  if (!response.ok) {
    throw new Error(`请求失败: ${response.status}`)
  }

  const result = (await response.json()) as ApiResponse<T>

  if (result.code !== 200) {
    throw new Error(result.msg || '接口返回异常')
  }

  return result.data
}
