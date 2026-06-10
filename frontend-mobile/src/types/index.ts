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
  userId: number
  houseId: number
  houseName?: string | null
  contractId?: number | null
  feeType: number
  feeTypeText?: string | null
  billNo: string
  billMonth?: string | null
  amount: number
  status: number
  statusText?: string | null
  dueDate?: string | null
  remark?: string | null
  createTime?: string | null
}

export interface BillQuery extends PageQuery {
  billNo?: string
  houseId?: number
  feeType?: number
  status?: number
  billMonth?: string
}

export interface PaymentRecordVO {
  id: number
  paymentNo: string
  userId: number
  userName?: string | null
  amount: number
  payMethod: number
  payMethodText?: string | null
  payTime?: string | null
  status: number
  statusText?: string | null
  voucherUrl?: string | null
  remark?: string | null
  verifyUser?: number | null
  verifyUserName?: string | null
  verifyTime?: string | null
  cancelUser?: number | null
  cancelUserName?: string | null
  cancelTime?: string | null
  cancelReason?: string | null
  bills?: PaymentRecordDetailVO[]
  createTime?: string | null
}

export interface PaymentRecordDetailVO {
  id: number
  billId: number
  billNo?: string | null
  houseName?: string | null
  feeType?: number | null
  feeTypeText?: string | null
  billMonth?: string | null
  amount: number
}

export interface PaymentQuery extends PageQuery {
  paymentNo?: string
  houseId?: number
  status?: number
  payMethod?: number
}

export interface PaymentSubmitPayload {
  billIds: number[]
  payMethod: number
  voucherUrl?: string
  remark?: string
}

export interface PropertyFeeConfigVO {
  id: number
  unitPrice: number
  effectiveMonth?: string | null
  status: number
  createTime?: string | null
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
  status: string
  viewCount: number
  createTime?: string | null
}

export interface NoticeQuery extends PageQuery {
  title?: string
  status?: string
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
