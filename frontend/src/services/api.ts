import { request } from '@/services/http'
import type {
  AssetPayload,
  AssetQuery,
  AssetRecord,
  AssetTreeNode,
  BillBatchRecord,
  BillGeneratePayload,
  BillQuery,
  BillRecord,
  ComplaintCreatePayload,
  ComplaintHandlePayload,
  ComplaintQuery,
  ComplaintRecord,
  ContractPayload,
  ContractQuery,
  ContractRecord,
  DashboardData,
  FileTaskRecord,
  FinanceAnalyzePayload,
  LoginPayload,
  NoticeGeneratePayload,
  NoticePayload,
  NoticeQuery,
  NoticeRecord,
  PageQuery,
  PageResult,
  PaymentAuditPayload,
  PaymentQuery,
  PaymentRecord,
  RegisterPayload,
  RepairAnalysisResult,
  RepairAnalyzePayload,
  RepairDispatchPayload,
  RepairKanban,
  RepairPayload,
  RepairStatusPayload,
  ReportExportPayload,
  RolePermPayload,
  RoleRecord,
  UserInfo,
} from '@/types'

export const authApi = {
  login(payload: LoginPayload) {
    return request<string>('/api/v1/auth/login', {
      method: 'POST',
      body: payload,
    })
  },
  register(payload: RegisterPayload) {
    return request<null>('/api/v1/auth/register', {
      method: 'POST',
      body: payload,
    })
  },
  getInfo() {
    return request<UserInfo>('/api/v1/auth/info', { method: 'GET' })
  },
}

export const assetApi = {
  getTree() {
    return request<AssetTreeNode[]>('/api/v1/assets/tree', { method: 'GET' })
  },
  getList(query: AssetQuery) {
    return request<PageResult<AssetRecord>>('/api/v1/assets', {
      method: 'GET',
      query,
    })
  },
  create(payload: AssetPayload) {
    return request<null>('/api/v1/assets', {
      method: 'POST',
      body: payload,
    })
  },
  update(payload: AssetPayload) {
    return request<null>('/api/v1/assets', {
      method: 'PUT',
      body: payload,
    })
  },
  remove(id: number) {
    return request<null>(`/api/v1/assets/${id}`, { method: 'DELETE' })
  },
}

export const contractApi = {
  getList(query: ContractQuery) {
    return request<PageResult<ContractRecord>>('/api/v1/contracts', {
      method: 'GET',
      query,
    })
  },
  save(payload: ContractPayload) {
    return request<null>('/api/v1/contracts', {
      method: payload.id ? 'PUT' : 'POST',
      body: payload,
    })
  },
}

export const financeApi = {
  generateBills(payload: BillGeneratePayload) {
    return request<BillBatchRecord>('/api/v1/bills/batch-generate', {
      method: 'POST',
      body: payload,
    })
  },
  getBatchLogs(query: PageQuery) {
    return request<PageResult<BillBatchRecord>>('/api/v1/bills/batch-logs', {
      method: 'GET',
      query,
    })
  },
  getBills(query: BillQuery) {
    return request<PageResult<BillRecord>>('/api/v1/bills', {
      method: 'GET',
      query,
    })
  },
  getPayments(query: PaymentQuery) {
    return request<PageResult<PaymentRecord>>('/api/v1/finance/payments', {
      method: 'GET',
      query,
    })
  },
  auditPayment(id: number, payload: PaymentAuditPayload) {
    return request<null>(`/api/v1/finance/audit/${id}`, {
      method: 'PUT',
      body: payload,
    })
  },
}

export const repairApi = {
  getKanban() {
    return request<RepairKanban>('/api/v1/repairs/kanban', { method: 'GET' })
  },
  create(payload: RepairPayload) {
    return request<null>('/api/v1/repairs', {
      method: 'POST',
      body: payload,
    })
  },
  dispatch(payload: RepairDispatchPayload) {
    return request<null>('/api/v1/repairs/dispatch', {
      method: 'PUT',
      body: payload,
    })
  },
  updateStatus(payload: RepairStatusPayload) {
    return request<null>('/api/v1/repairs/status', {
      method: 'PUT',
      body: payload,
    })
  },
}

export const complaintApi = {
  getList(query: ComplaintQuery) {
    return request<PageResult<ComplaintRecord>>('/api/v1/complaints', {
      method: 'GET',
      query,
    })
  },
  create(payload: ComplaintCreatePayload) {
    return request<null>('/api/v1/complaints', {
      method: 'POST',
      body: payload,
    })
  },
  handle(payload: ComplaintHandlePayload) {
    return request<null>('/api/v1/complaints/handle', {
      method: 'PUT',
      body: payload,
    })
  },
}

export const noticeApi = {
  getList(query: NoticeQuery) {
    return request<PageResult<NoticeRecord>>('/api/v1/notices', {
      method: 'GET',
      query,
    })
  },
  save(payload: NoticePayload) {
    return request<null>('/api/v1/notices', {
      method: 'POST',
      body: payload,
    })
  },
}

export const roleApi = {
  getList(query: PageQuery) {
    return request<PageResult<RoleRecord>>('/api/v1/roles', {
      method: 'GET',
      query,
    })
  },
  updatePermissions(id: number, payload: RolePermPayload) {
    return request<null>(`/api/v1/roles/${id}/perms`, {
      method: 'PUT',
      body: payload,
    })
  },
}

export const dashboardApi = {
  getData() {
    return request<DashboardData>('/api/v1/dashboard/data', { method: 'GET' })
  },
}

export const systemApi = {
  getTasks(query: PageQuery) {
    return request<PageResult<FileTaskRecord>>('/api/v1/system/tasks', {
      method: 'GET',
      query,
    })
  },
  exportFinance(payload: ReportExportPayload) {
    return request<null>('/api/v1/system/export/finance', {
      method: 'GET',
      query: payload,
    })
  },
  importAssets(file: File) {
    const formData = new FormData()
    formData.append('file', file)

    return request<null>('/api/v1/system/import/assets', {
      method: 'POST',
      body: formData,
      skipJson: true,
    })
  },
}

function toFormQuery(payload: object) {
  const params = new URLSearchParams()
  Object.entries(payload as Record<string, string | number | boolean | null | undefined>).forEach(([key, value]) => {
    if (value !== undefined && value !== null) {
      params.set(key, String(value))
    }
  })

  return params
}

export const aiApi = {
  generateNotice(payload: NoticeGeneratePayload) {
    return request<string>('/api/v1/ai/notice/generate', {
      method: 'POST',
      body: toFormQuery(payload),
      skipJson: true,
    })
  },
  analyzeFinance(payload: FinanceAnalyzePayload) {
    return request<string>('/api/v1/ai/finance/analyze', {
      method: 'POST',
      body: toFormQuery(payload),
      skipJson: true,
    })
  },
  analyzeRepair(payload: RepairAnalyzePayload) {
    return request<RepairAnalysisResult>('/api/v1/ai/repair/analyze', {
      method: 'POST',
      body: toFormQuery(payload),
      skipJson: true,
    })
  },
}
