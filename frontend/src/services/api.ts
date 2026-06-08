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
  LoginPayload,
  NoticePayload,
  NoticeQuery,
  NoticeRecord,
  PageQuery,
  PageResult,
  PaymentCancelPayload,
  PaymentQuery,
  PaymentRecordVO,
  PropertyFeeConfigVO,
  PropertyFeeConfigPayload,
  PurchaseApplicationQuery,
  PurchaseApplicationRecord,
  PurchaseApprovalPayload,
  RegisterPayload,
  RepairDispatchPayload,
  RepairKanban,
  RepairPayload,
  RepairStatusPayload,
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
  getById(id: number) {
    return request<AssetRecord>(`/api/v1/assets/${id}`, { method: 'GET' })
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
    return request<any>('/api/v1/bills/generate', {
      method: 'POST',
      body: payload,
    })
  },
  getBills(query: BillQuery) {
    return request<PageResult<BillRecord>>('/api/v1/bills', {
      method: 'GET',
      query,
    })
  },
  getPayments(query: PaymentQuery) {
    return request<PageResult<PaymentRecordVO>>('/api/v1/payments', {
      method: 'GET',
      query,
    })
  },
  getPaymentDetail(id: number) {
    return request<PaymentRecordVO>(`/api/v1/payments/${id}`, {
      method: 'GET',
    })
  },
  verifyPayment(id: number) {
    return request<null>(`/api/v1/payments/${id}/verify`, {
      method: 'PUT',
    })
  },
  cancelPayment(id: number, payload: PaymentCancelPayload) {
    return request<null>(`/api/v1/payments/${id}/cancel`, {
      method: 'PUT',
      body: payload,
    })
  },
  getPropertyFeeConfig() {
    return request<PropertyFeeConfigVO>('/api/v1/property-fee-config', {
      method: 'GET',
    })
  },
  setPropertyFeeConfig(payload: PropertyFeeConfigPayload) {
    return request<null>('/api/v1/property-fee-config', {
      method: 'POST',
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

export const purchaseApi = {
  getList(query: PurchaseApplicationQuery) {
    return request<PageResult<PurchaseApplicationRecord>>('/api/v1/purchase-applications', { method: 'GET', query })
  },
  approve(payload: PurchaseApprovalPayload) {
    return request<null>('/api/v1/purchase-applications/approve', { method: 'PUT', body: payload })
  },
}

export const dashboardApi = {
  getData() {
    return request<DashboardData>('/api/v1/dashboard/data', { method: 'GET' })
  },
}
