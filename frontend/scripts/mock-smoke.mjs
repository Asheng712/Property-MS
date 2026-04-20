const base = 'http://127.0.0.1:5173'

const token = 'mock-token'

const checks = [
  ['POST', '/api/v1/auth/login', { username: 'admin', password: '123456' }],
  ['POST', '/api/v1/auth/register', { username: 'u1', password: 'p1', realName: 'n1', phone: '13800000000', roleId: 1 }],
  ['GET', '/api/v1/auth/info'],
  ['GET', '/api/v1/assets/tree'],
  ['GET', '/api/v1/assets?page=1&pageSize=10'],
  ['POST', '/api/v1/assets', { name: '2栋-201', type: 'RESIDENTIAL', area: 88, status: 'VACANT' }],
  ['PUT', '/api/v1/assets', { id: 101, name: '1栋-101', type: 'RESIDENTIAL', area: 98.5, status: 'OCCUPIED' }],
  ['DELETE', '/api/v1/assets/101'],
  ['GET', '/api/v1/contracts?page=1&pageSize=10'],
  ['POST', '/api/v1/contracts', { houseId: 101, tenantName: '李四', rentAmount: 3200, startDate: '2026-01-01', endDate: '2026-12-31', contractStatus: 1 }],
  ['GET', '/api/v1/bills/batch-logs?page=1&pageSize=10'],
  ['POST', '/api/v1/bills/batch-generate', { feeType: 'PROPERTY', targetRange: 'ALL', month: '2026-04' }],
  ['GET', '/api/v1/bills?page=1&pageSize=10'],
  ['GET', '/api/v1/finance/payments?page=1&pageSize=10'],
  ['PUT', '/api/v1/finance/audit/501', { status: 1, operator: 'admin' }],
  ['GET', '/api/v1/repairs/kanban'],
  ['POST', '/api/v1/repairs', { houseId: 101, content: '门锁损坏', reporter: '王五', priority: 2 }],
  ['PUT', '/api/v1/repairs/dispatch', { id: 601, workerId: 11 }],
  ['PUT', '/api/v1/repairs/status', { id: 601, status: 2 }],
  ['GET', '/api/v1/complaints?page=1&pageSize=10'],
  ['PUT', '/api/v1/complaints/handle', { id: 701, status: 1, handleResult: '已处理' }],
  ['GET', '/api/v1/notices?page=1&pageSize=10'],
  ['POST', '/api/v1/notices', { title: '测试公告', content: 'mock', targetType: 'ALL', status: 'PUBLISHED' }],
  ['GET', '/api/v1/roles?page=1&pageSize=10'],
  ['PUT', '/api/v1/roles/1/perms', { permissions: 'asset:all' }],
  ['GET', '/api/v1/system/tasks?page=1&pageSize=10'],
  ['GET', '/api/v1/system/export/finance?startDate=2026-04-01&endDate=2026-04-30&reportType=MONTHLY'],
]

async function run() {
  let failed = 0

  for (const [method, path, body] of checks) {
    const res = await fetch(`${base}${path}`, {
      method,
      headers: {
        Authorization: `Bearer ${token}`,
        ...(body ? { 'Content-Type': 'application/json' } : {}),
      },
      body: body ? JSON.stringify(body) : undefined,
    })

    let payload
    try {
      payload = await res.json()
    } catch {
      payload = {}
    }

    const pass = res.ok && payload.code === 200
    if (!pass) {
      failed += 1
    }

    process.stdout.write(`${pass ? 'PASS' : 'FAIL'} ${method.padEnd(6)} ${path}\n`)
  }

  if (failed > 0) {
    process.stderr.write(`\nSmoke test failed: ${failed} endpoint(s) failed.\n`)
    process.exit(1)
  }

  process.stdout.write('\nSmoke test passed: all mocked endpoints are compatible.\n')
}

run()
