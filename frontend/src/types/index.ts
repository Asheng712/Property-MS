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
  feeType: string
  targetRange: string
  month: string
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

export interface PaymentAuditPayload {
  id?: number
  status: number
  operator: string
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

export interface NoticePayload {
  id?: number
  title: string
  content: string
  targetType: string
  status: string
}

export interface RoleRecord {
  id: number
  roleName: string
  roleKey: string
  permissions: string
}

export interface RolePermPayload {
  permissions: string
}

export interface FileTaskRecord {
  id: number
  taskType: string
  fileName: string
  operator?: string | null
  dataCount?: number | null
  status: string
  fileUrl?: string | null
  createTime?: string | null
}

export interface ReportExportPayload {
  startDate: string
  endDate: string
  reportType: string
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

export interface PermissionGroup {
  id: string
  title: string
  permissions: string[]
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

export interface LegacyRoleRecord {
  id: string
  name: string
  description: string
  locked?: boolean
  permissionGroups: PermissionGroup[]
}

export interface ReportCard {
  id: string
  title: string
  subtitle: string
  progress: number
  metric: string
}

export interface NavigationItem {
  path: string
  label: string
  icon: Component
}
