export interface ApiResponse<T> {
  code: number
  msg?: string
  data: T
}

export interface PageResult<T> {
  total: number
  records: T[]
}

export interface PageQuery {
  page: number
  pageSize: number
}

export interface UserInfo {
  id: number
  username: string
  realName?: string | null
  phone?: string | null
  email?: string | null
  avatar?: string | null
  status: number
  roleId?: number | null
  roleName?: string | null
}

export interface LoginPayload {
  username: string
  password: string
}

export interface RegisterPayload {
  username: string
  password: string
  realName: string
  phone: string
  email?: string
  avatar?: string
  roleId: number
}

export interface AssetRecord {
  id: number
  parentId: number
  name: string
  type: string
  area: number
  status: string
  ownerName?: string | null
  ownerPhone?: string | null
  ownerId?: number | null
  createTime?: string | null
}

export interface AssetQuery extends PageQuery {
  name?: string
  type?: string
  status?: string
}

export interface AssetTreeNode {
  id: number
  name: string
  type: string
  children?: AssetTreeNode[]
}

export interface BillRecord {
  id: number
  billNo: string
  houseId: number
  houseName?: string | null
  amount: number
  type: string
  payStatus: number
  payStatusText?: string | null
  deadline?: string | null
  createTime?: string | null
}

export interface BillQuery extends PageQuery {
  billNo?: string
  houseId?: number
  payStatus?: number
  type?: string
}

export interface PaymentRecord {
  id: number
  trxNo: string
  billId: number
  billNo?: string | null
  houseId: number
  houseName?: string | null
  payAmount: number
  payType: string
  status: number
  statusText?: string | null
  payTime?: string | null
  operator?: string | null
}

export interface PaymentQuery extends PageQuery {
  trxNo?: string
  houseId?: number
  status?: number
  payType?: string
}

export interface PaymentCreatePayload {
  billId: number
  payType: string
  payAmount: number
}

export interface RepairRecord {
  id: number
  repairNo: string
  houseId: number
  houseName?: string | null
  content: string
  reporter: string
  workerId?: number | null
  workerName?: string | null
  status: number
  statusText?: string | null
  priority: number
  priorityText?: string | null
  createTime?: string | null
  finishTime?: string | null
}

export interface RepairKanban {
  pending: RepairRecord[]
  processing: RepairRecord[]
  completed: RepairRecord[]
}

export interface RepairPayload {
  houseId: number
  content: string
  reporter?: string
  priority: number
}

export interface ComplaintRecord {
  id: number
  complaintNo: string
  category: string
  content: string
  source: string
  status: number
  statusText?: string | null
  handleResult?: string | null
  createTime?: string | null
}

export interface ComplaintQuery extends PageQuery {
  category?: string
  status?: number
}

export interface ComplaintCreatePayload {
  category: string
  content: string
  source: string
}

export interface NoticeRecord {
  id: number
  title: string
  content: string
  targetType: string
  status: string
  viewCount: number
  createTime?: string | null
}

export interface NoticeQuery extends PageQuery {
  title?: string
  status?: string
  targetType?: string
}

export interface ContractRecord {
  id: number
  houseId: number
  houseName?: string | null
  tenantName: string
  rentAmount: number
  startDate: string
  endDate: string
  increaseRate?: number | null
  deposit?: number | null
  contractStatus: number
  contractStatusText?: string | null
  createTime?: string | null
}

export interface ContractQuery extends PageQuery {
  tenantName?: string
  contractStatus?: number
}

export interface PurchaseApplicationRecord {
  id: number
  applicationNo: string
  type?: string | null
  houseId: number
  houseName?: string | null
  applicantId: number
  applicantName: string
  applicantPhone: string
  status: number
  statusText?: string | null
  proposedPrice?: number | null
  startDate?: string | null
  endDate?: string | null
  remark?: string | null
  createTime?: string | null
}

export interface PurchaseApplicationQuery extends PageQuery {
  status?: number
  type?: string
}

export interface PurchaseApplicationPayload {
  houseId: number
  type: string
  applicantName: string
  applicantPhone: string
  remark?: string
}

export interface MobileTabItem {
  path: string
  name: string
  icon: string
  dot?: boolean
}
