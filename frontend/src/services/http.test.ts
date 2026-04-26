import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'

import { clearToken, getToken, request, setToken } from './http'

function mockJsonResponse<T>(payload: T, init: ResponseInit = {}) {
  return new Response(JSON.stringify(payload), {
    status: init.status ?? 200,
    headers: {
      'Content-Type': 'application/json',
      ...init.headers,
    },
  })
}

describe('http request client', () => {
  beforeEach(() => {
    window.localStorage.clear()
  })

  afterEach(() => {
    vi.restoreAllMocks()
    vi.unstubAllGlobals()
    window.localStorage.clear()
  })

  it('stores, reads, and clears the auth token', () => {
    setToken('token-123')
    expect(getToken()).toBe('token-123')

    clearToken()
    expect(getToken()).toBe('')
  })

  it('sends query params, authorization header, and returns response data', async () => {
    setToken('token-123')
    const fetchMock = vi.fn().mockResolvedValue(
      mockJsonResponse({
        code: 200,
        data: {
          records: [{ id: 1, name: 'A1-101' }],
          total: 1,
        },
      }),
    )
    vi.stubGlobal('fetch', fetchMock)

    const result = await request('/api/v1/assets', {
      method: 'GET',
      query: {
        page: 1,
        pageSize: 10,
        name: 'A1',
        status: '',
      },
    })

    expect(result).toEqual({
      records: [{ id: 1, name: 'A1-101' }],
      total: 1,
    })
    expect(fetchMock).toHaveBeenCalledWith('/api/v1/assets?page=1&pageSize=10&name=A1', expect.any(Object))
    const [, options] = fetchMock.mock.calls[0]
    expect((options.headers as Headers).get('Authorization')).toBe('Bearer token-123')
  })

  it('serializes JSON payloads for successful POST requests', async () => {
    const fetchMock = vi.fn().mockResolvedValue(
      mockJsonResponse({
        code: 200,
        data: 'jwt-token',
      }),
    )
    vi.stubGlobal('fetch', fetchMock)

    await expect(
      request('/api/v1/auth/login', {
        method: 'POST',
        body: {
          username: 'admin',
          password: '123456',
        },
      }),
    ).resolves.toBe('jwt-token')

    const [, options] = fetchMock.mock.calls[0]
    expect((options.headers as Headers).get('Content-Type')).toBe('application/json')
    expect(options.body).toBe(JSON.stringify({ username: 'admin', password: '123456' }))
  })

  it('throws when the server responds with a failed HTTP status', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn().mockResolvedValue(
        mockJsonResponse(
          {
            code: 500,
            msg: '服务器异常',
            data: null,
          },
          { status: 500 },
        ),
      ),
    )

    await expect(request('/api/v1/assets', { method: 'GET' })).rejects.toThrow('500')
  })

  it('throws when the API business code is not successful', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn().mockResolvedValue(
        mockJsonResponse({
          code: 401,
          msg: '登录已过期',
          data: null,
        }),
      ),
    )

    await expect(request('/api/v1/auth/info', { method: 'GET' })).rejects.toThrow('登录已过期')
  })
})
