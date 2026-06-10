import type { Component } from 'vue'

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

export type AssetType = 'BUILDING' | 'UNIT' | 'RESIDENTIAL' | 'SHOP'

export interface AssetTreeNode {
  id: number
  name: string
  type: string
  children?: AssetTreeNode[]
}

export interface AssetRecord {
  id: number
  parentId?: number | null
  name: string
  type: AssetType | string
  area: number
  status: string
  ownerName?: string | null
  ownerPhone?: string | null
  createTime?: string | null
}

export interface AssetQuery extends PageQuery {
  name?: string
  type?: string
  status?: string
  parentId?: number
}

export interface AssetPayload {
  id?: number
  parentId?: number
  name: string
  type: string
  area: number
  status: string
  ownerName?: string
  ownerPhone?: string
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

export interface ContractPayload {
  id?: number
  houseId: number
  tenantName: string
  rentAmount: number
  startDate: string
  endDate: string
  increaseRate?: number
  deposit?: number
  contractStatus: number
}

export interface BillBatchRecord {
  id: number
  batchNo: string
  feeType: string
  targetRange: string
  totalCount: number
  totalAmount: number
  status: string
  operator?: string | null
  createTime?: string | null
}

export interface BillGeneratePayload {
  feeType: number
  billMonth?: string
  houseId?: number
  remark?: string
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
  status?: number
  payMethod?: number
}

export interface PaymentSubmitPayload {
  billIds: number[]
  payMethod: number
  voucherUrl?: string
  remark?: string
}

export interface PaymentCancelPayload {
  cancelReason: string
}

export interface PropertyFeeConfigVO {
  id: number
  unitPrice: number
  effectiveMonth?: string | null
  status: number
  createTime?: string | null
}

export interface PropertyFeeConfigPayload {
  unitPrice: number
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
  id?: number
  houseId: number
  content: string
  reporter: string
  priority: number
}

export interface RepairDispatchPayload {
  id: number
  workerId: number
}

export interface RepairStatusPayload {
  id: number
  status: number
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

export interface ComplaintHandlePayload {
  id: number
  status: number
  handleResult: string
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

export interface NoticePayload {
  id?: number
  title: string
  content: string
  status: string
}

export interface AssetRentalStatus {
  type: string
  typeName: string
  totalCount: number
  vacantCount: number
  rentedCount: number
  soldCount: number
  occupiedCount: number
  vacantRate: number
  rentedRate: number
  soldRate: number
  occupiedRate: number
}

export interface DashboardData {
  pendingChargeCount: number
  pendingChargeAmount: number
  overdueCount: number
  overdueAmount: number
  todayComplaintCount: number
  pendingComplaintCount: number
  todayRepairCount: number
  pendingRepairCount: number
  assetRentalList: AssetRentalStatus[]
}

export interface QuickStat {
  id: string
  label: string
  value: number | string
  unit: string
  color: string
  accent: string
  icon: string
}

export interface ChartSegment {
  label: string
  value: number
  color: string
}

export interface LegacyAssetTreeNode {
  id: string
  label: string
  children?: LegacyAssetTreeNode[]
}

export interface LegacyAssetRecord {
  id: string
  code: string
  name: string
  category: 'shop' | 'residence' | 'parking'
  area: number
  areaLabel: string
  deliveryStatus: 'occupied' | 'vacant' | 'sold'
  occupant: string
  zone: string
}

export interface LegacyContractRecord {
  id: string
  contractNo: string
  tenant: string
  asset: string
  period: string
  amount: number
  status: 'active' | 'expiring' | 'draft'
}

export interface BillBatch {
  id: string
  batchNo: string
  scope: string
  feeType: string
  generatedCount: number
  totalAmount: number
  executedAt: string
  status: 'done' | 'processing'
}

export interface LegacyPaymentRecord {
  id: string
  billNo: string
  payer: string
  amount: number
  channel: string
  paidAt: string
  status: 'paid' | 'pending' | 'refund'
}

export interface RepairTicket {
  id: string
  title: string
  location: string
  reporter: string
  assignee?: string
  ageLabel: string
  stage: 'todo' | 'doing' | 'done'
}

export interface LegacyComplaintRecord {
  id: string
  resident: string
  topic: string
  createdAt: string
  status: 'new' | 'following' | 'closed'
  priority: 'high' | 'medium' | 'low'
}

export interface LegacyNoticeRecord {
  id: string
  title: string
  audience: string
  publishAt: string
  status: 'draft' | 'published'
  summary: string
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
  createdContractId?: number | null
  createTime?: string | null
}

export interface PurchaseApplicationQuery extends PageQuery {
  status?: number
  type?: string
  applicantName?: string
}

export interface PurchaseApprovalPayload {
  id: number
  approved: boolean
  proposedPrice?: number
  startDate?: string
  endDate?: string
  deposit?: number
  increaseRate?: number
  remark?: string
}

export interface NavigationItem {
  path: string
  label: string
  icon: Component
}
