import type { Component } from 'vue'

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

export interface AssetTreeNode {
  id: string
  label: string
  children?: AssetTreeNode[]
}

export interface AssetRecord {
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

export interface ContractRecord {
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

export interface PaymentRecord {
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

export interface ComplaintRecord {
  id: string
  resident: string
  topic: string
  createdAt: string
  status: 'new' | 'following' | 'closed'
  priority: 'high' | 'medium' | 'low'
}

export interface NoticeRecord {
  id: string
  title: string
  audience: string
  publishAt: string
  status: 'draft' | 'published'
  summary: string
}

export interface PermissionGroup {
  id: string
  title: string
  permissions: string[]
}

export interface RoleRecord {
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
