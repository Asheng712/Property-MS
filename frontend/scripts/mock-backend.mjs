import http from 'node:http'

const port = 8080

const now = new Date().toISOString()

const ok = (data) => ({ code: 200, msg: 'ok', data })

const page = (records) => ({ total: records.length, records })

const userInfo = {
  id: 1,
  username: 'admin',
  realName: 'System Admin',
  phone: '13800000000',
  email: 'admin@example.com',
  avatar: null,
  status: 1,
  roleId: 1,
  roleName: '超级管理员',
}

const assetRecords = [
  {
    id: 101,
    parentId: 0,
    name: '1栋-101',
    type: 'RESIDENTIAL',
    area: 98.5,
    status: 'OCCUPIED',
    ownerName: '张三',
    ownerPhone: '13900000000',
    createTime: now,
  },
]

const contractRecords = [
  {
    id: 201,
    houseId: 101,
    houseName: '1栋-101',
    tenantName: '张三',
    rentAmount: 3500,
    startDate: '2026-01-01',
    endDate: '2026-12-31',
    increaseRate: 5,
    deposit: 7000,
    contractStatus: 1,
    contractStatusText: '履约中',
  },
]

const billBatchRecords = [
  {
    id: 301,
    batchNo: 'BATCH-202604',
    feeType: 'PROPERTY',
    targetRange: 'ALL',
    totalCount: 120,
    totalAmount: 286000,
    status: 'DONE',
    operator: 'admin',
    createTime: now,
  },
]

const billRecords = [
  {
    id: 401,
    billNo: 'BILL-202604-0001',
    houseId: 101,
    houseName: '1栋-101',
    amount: 3800,
    type: 'PROPERTY',
    payStatus: 0,
    payStatusText: '待缴费',
    deadline: '2026-04-30',
    createTime: now,
  },
]

const paymentRecords = [
  {
    id: 501,
    trxNo: 'TRX-202604-0001',
    billId: 401,
    billNo: 'BILL-202604-0001',
    houseId: 101,
    houseName: '1栋-101',
    payAmount: 3800,
    payType: 'WECHAT',
    status: 1,
    statusText: '已支付',
    payTime: now,
    operator: 'admin',
  },
]

const repairRecords = [
  {
    id: 601,
    repairNo: 'R-202604-0001',
    houseId: 101,
    houseName: '1栋-101',
    content: '厨房漏水',
    reporter: '张三',
    workerId: 11,
    workerName: '王师傅',
    status: 1,
    statusText: '处理中',
    priority: 2,
    priorityText: '中',
    createTime: now,
    finishTime: null,
  },
]

const complaintRecords = [
  {
    id: 701,
    complaintNo: 'C-202604-0001',
    category: 'NOISE',
    content: '夜间噪音',
    source: 'APP',
    status: 0,
    statusText: '待处理',
    handleResult: null,
    createTime: now,
  },
]

const noticeRecords = [
  {
    id: 801,
    title: '停水通知',
    content: '4月22日 09:00-12:00 例行检修停水。',
    targetType: 'ALL',
    status: 'PUBLISHED',
    viewCount: 62,
    createTime: now,
  },
]

const roleRecords = [
  {
    id: 1,
    roleName: '超级管理员',
    roleKey: 'super_admin',
    permissions: 'asset:all,finance:all,system:all',
  },
]

const taskRecords = [
  {
    id: 901,
    taskType: 'EXPORT_FINANCE',
    fileName: 'finance-2026-04.xlsx',
    operator: 'admin',
    dataCount: 120,
    status: 'DONE',
    fileUrl: '/files/finance-2026-04.xlsx',
    createTime: now,
  },
]

function sendJson(res, statusCode, payload) {
  res.writeHead(statusCode, {
    'Content-Type': 'application/json; charset=utf-8',
    'Access-Control-Allow-Origin': '*',
    'Access-Control-Allow-Headers': 'Content-Type, Authorization',
    'Access-Control-Allow-Methods': 'GET,POST,PUT,DELETE,OPTIONS',
  })
  res.end(JSON.stringify(payload))
}

function route(method, path) {
  if (method === 'POST' && path === '/api/v1/auth/login') return ok('mock-token')
  if (method === 'POST' && path === '/api/v1/auth/register') return ok(null)
  if (method === 'GET' && path === '/api/v1/auth/info') return ok(userInfo)

  if (method === 'GET' && path === '/api/v1/assets/tree') {
    return ok([{ id: 1, name: '一期', type: 'BUILDING', children: [{ id: 101, name: '1栋-101', type: 'RESIDENTIAL' }] }])
  }
  if (method === 'GET' && path === '/api/v1/assets') return ok(page(assetRecords))
  if (method === 'POST' && path === '/api/v1/assets') return ok(null)
  if (method === 'PUT' && path === '/api/v1/assets') return ok(null)
  if (method === 'DELETE' && path.startsWith('/api/v1/assets/')) return ok(null)

  if (method === 'GET' && path === '/api/v1/contracts') return ok(page(contractRecords))
  if (method === 'POST' && path === '/api/v1/contracts') return ok(null)
  if (method === 'PUT' && path === '/api/v1/contracts') return ok(null)

  if (method === 'POST' && path === '/api/v1/bills/batch-generate') return ok(billBatchRecords[0])
  if (method === 'GET' && path === '/api/v1/bills/batch-logs') return ok(page(billBatchRecords))
  if (method === 'GET' && path === '/api/v1/bills') return ok(page(billRecords))
  if (method === 'GET' && path === '/api/v1/finance/payments') return ok(page(paymentRecords))
  if (method === 'PUT' && path.startsWith('/api/v1/finance/audit/')) return ok(null)

  if (method === 'GET' && path === '/api/v1/repairs/kanban') {
    return ok({ pending: [], processing: repairRecords, completed: [] })
  }
  if (method === 'POST' && path === '/api/v1/repairs') return ok(null)
  if (method === 'PUT' && path === '/api/v1/repairs/dispatch') return ok(null)
  if (method === 'PUT' && path === '/api/v1/repairs/status') return ok(null)

  if (method === 'GET' && path === '/api/v1/complaints') return ok(page(complaintRecords))
  if (method === 'PUT' && path === '/api/v1/complaints/handle') return ok(null)

  if (method === 'GET' && path === '/api/v1/notices') return ok(page(noticeRecords))
  if (method === 'POST' && path === '/api/v1/notices') return ok(null)

  if (method === 'GET' && path === '/api/v1/roles') return ok(page(roleRecords))
  if (method === 'PUT' && path.match(/^\/api\/v1\/roles\/\d+\/perms$/)) return ok(null)

  if (method === 'GET' && path === '/api/v1/system/tasks') return ok(page(taskRecords))
  if (method === 'GET' && path === '/api/v1/system/export/finance') return ok(null)
  if (method === 'POST' && path === '/api/v1/system/import/assets') return ok(null)

  return { code: 404, msg: `mock route not found: ${method} ${path}`, data: null }
}

const server = http.createServer((req, res) => {
  if (!req.url || !req.method) {
    sendJson(res, 400, { code: 400, msg: 'bad request', data: null })
    return
  }

  if (req.method === 'OPTIONS') {
    res.writeHead(204, {
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Headers': 'Content-Type, Authorization',
      'Access-Control-Allow-Methods': 'GET,POST,PUT,DELETE,OPTIONS',
    })
    res.end()
    return
  }

  const url = new URL(req.url, `http://localhost:${port}`)
  const payload = route(req.method, url.pathname)
  const statusCode = payload.code === 404 ? 404 : 200
  sendJson(res, statusCode, payload)
})

server.listen(port, '127.0.0.1', () => {
  process.stdout.write(`Mock backend is running at http://127.0.0.1:${port}\n`)
})
