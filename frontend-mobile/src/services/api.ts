import http from './http'
import type {
  UserInfo,
  LoginPayload,
  RegisterPayload,
  BillRecord,
  BillQuery,
  PaymentRecord,
  PaymentQuery,
  PaymentCreatePayload,
  RepairKanban,
  RepairPayload,
  RepairRecord,
  ComplaintRecord,
  ComplaintQuery,
  ComplaintCreatePayload,
  NoticeRecord,
  NoticeQuery,
  ContractRecord,
  ContractQuery,
  AssetRecord,
  AssetQuery,
  AssetTreeNode,
  PurchaseApplicationRecord,
  PurchaseApplicationQuery,
  PurchaseApplicationPayload,
  PageResult,
} from '@/types'

export const authApi = {
  login(data: LoginPayload) {
    return http.post<string>('/auth/login', data)
  },
  register(data: RegisterPayload) {
    return http.post<null>('/auth/register', data)
  },
  getInfo() {
    return http.get<UserInfo>('/auth/info')
  },
  updateProfile(data: Partial<UserInfo>) {
    return http.put<null>('/auth/profile', data)
  },
}

export const billApi = {
  getList(params: BillQuery) {
    return http.get<PageResult<BillRecord>>('/bills', { params })
  },
}

export const paymentApi = {
  getList(params: PaymentQuery) {
    return http.get<PageResult<PaymentRecord>>('/finance/payments', { params })
  },
  create(data: PaymentCreatePayload) {
    return http.post<PaymentRecord>('/finance/payments', data)
  },
}

export const repairApi = {
  getKanban(reporter?: string) {
    return http.get<RepairKanban>('/repairs/kanban', {
      params: reporter ? { reporter } : undefined,
    })
  },
  create(data: RepairPayload) {
    return http.post<null>('/repairs', data)
  },
}

export const complaintApi = {
  getList(params: ComplaintQuery) {
    return http.get<PageResult<ComplaintRecord>>('/complaints', { params })
  },
  create(data: ComplaintCreatePayload) {
    return http.post<null>('/complaints', data)
  },
}

export const noticeApi = {
  getList(params: NoticeQuery) {
    return http.get<PageResult<NoticeRecord>>('/notices', { params })
  },
}

export const contractApi = {
  getList(params: ContractQuery) {
    return http.get<PageResult<ContractRecord>>('/contracts', { params })
  },
}

export const assetApi = {
  getTree() {
    return http.get<AssetTreeNode[]>('/assets/tree')
  },
  getMyHouses() {
    return http.get<AssetRecord[]>('/assets/my-houses')
  },
  getList(params?: AssetQuery) {
    return http.get<PageResult<AssetRecord>>('/assets', { params })
  },
  getById(id: number) {
    return http.get<AssetRecord>(`/assets/${id}`)
  },
}

export const purchaseApi = {
  getList(params: PurchaseApplicationQuery) {
    return http.get<PageResult<PurchaseApplicationRecord>>('/purchase-applications', { params })
  },
  getById(id: number) {
    return http.get<PurchaseApplicationRecord>(`/purchase-applications/${id}`)
  },
  create(data: PurchaseApplicationPayload) {
    return http.post<null>('/purchase-applications', data)
  },
}
