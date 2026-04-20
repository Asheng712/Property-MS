import {
  Bell,
  Coin,
  CreditCard,
  DataAnalysis,
  DocumentCopy,
  Files,
  House,
  MagicStick,
  Monitor,
  OfficeBuilding,
  Opportunity,
  Service,
  SetUp,
} from '@element-plus/icons-vue'
import type {
  BillBatch,
  ChartSegment,
  LegacyAssetRecord,
  LegacyAssetTreeNode,
  LegacyComplaintRecord,
  LegacyContractRecord,
  LegacyNoticeRecord,
  LegacyPaymentRecord,
  LegacyRoleRecord,
  NavigationItem,
  QuickStat,
  RepairTicket,
  ReportCard,
} from '@/types'

export const navigationItems: NavigationItem[] = [
  { path: '/dashboard', label: '管理员运营看板', icon: Monitor },
  { path: '/assets', label: '资产全景管理', icon: House },
  { path: '/contracts', label: '商业合同管理', icon: OfficeBuilding },
  { path: '/billing', label: '智能账单引擎', icon: MagicStick },
  { path: '/payments', label: '缴费流水与核销', icon: Coin },
  { path: '/repairs', label: '报修工单调度', icon: Service },
  { path: '/complaints', label: '投诉建议闭环', icon: Opportunity },
  { path: '/announcements', label: '定向公告发布', icon: Bell },
  { path: '/roles', label: '角色权限控制', icon: SetUp },
  { path: '/reports', label: '报表导出与导入', icon: Files },
]

export const dashboardStats: QuickStat[] = [
  { id: '1', label: '待收费数据', value: 30, unit: '条', color: '#a855f7', accent: '#f3e8ff', icon: 'bell' },
  { id: '2', label: '待收费金额', value: 2000, unit: '元', color: '#2563eb', accent: '#dbeafe', icon: 'money' },
  { id: '3', label: '欠费数据', value: 50, unit: '条', color: '#f97316', accent: '#ffedd5', icon: 'warning' },
  { id: '4', label: '欠费金额', value: 5000, unit: '元', color: '#ef4444', accent: '#fee2e2', icon: 'document' },
  { id: '5', label: '今日投诉', value: 1, unit: '件', color: '#16a34a', accent: '#dcfce7', icon: 'service' },
  { id: '6', label: '投诉待办', value: 1, unit: '件', color: '#ec4899', accent: '#fce7f3', icon: 'clock' },
  { id: '7', label: '今日报修', value: 1, unit: '件', color: '#e11d48', accent: '#ffe4e6', icon: 'tool' },
  { id: '8', label: '报修待办', value: 1, unit: '件', color: '#14b8a6', accent: '#ccfbf1', icon: 'repair' },
]

export const rentalChart: ChartSegment[] = [
  { label: '空置', value: 48, color: '#3b82f6' },
  { label: '已出租', value: 34, color: '#10b981' },
  { label: '已出售', value: 18, color: '#f59e0b' },
]

export const parkingChart: ChartSegment[] = [
  { label: '空置', value: 22, color: '#3b82f6' },
  { label: '已出租', value: 71, color: '#10b981' },
  { label: '已出售', value: 7, color: '#f59e0b' },
]

export const assetTree: LegacyAssetTreeNode[] = [
  {
    id: 'a',
    label: 'A区商业街',
    children: [
      { id: 'a-1', label: '1号楼' },
      { id: 'a-2', label: '2号楼' },
    ],
  },
  {
    id: 'b',
    label: 'B区住宅',
    children: [
      { id: 'b-1', label: '1栋' },
      { id: 'b-2', label: '2栋' },
    ],
  },
]

export const assetRecords: LegacyAssetRecord[] = [
  { id: '1', code: 'A1-101', name: 'A1-101', category: 'shop', area: 120, areaLabel: '120平方米', deliveryStatus: 'occupied', occupant: '张伟', zone: 'a' },
  { id: '2', code: 'A1-102', name: 'A1-102', category: 'shop', area: 85, areaLabel: '85平方米', deliveryStatus: 'vacant', occupant: '-', zone: 'a' },
  { id: '3', code: 'B2-201', name: 'B2-201', category: 'residence', area: 105, areaLabel: '105平方米', deliveryStatus: 'sold', occupant: '李娜', zone: 'b' },
  { id: '4', code: 'B2-202', name: 'B2-202', category: 'residence', area: 140, areaLabel: '140平方米', deliveryStatus: 'sold', occupant: '王强', zone: 'b' },
]

export const contractRecords: LegacyContractRecord[] = [
  { id: '1', contractNo: 'CT-2024-001', tenant: '星河咖啡', asset: 'A1-101', period: '2024-01-01 至 2026-12-31', amount: 168000, status: 'active' },
  { id: '2', contractNo: 'CT-2024-016', tenant: '优品超市', asset: 'A1-103', period: '2024-04-01 至 2025-03-31', amount: 98000, status: 'expiring' },
  { id: '3', contractNo: 'CT-2024-021', tenant: '萌宠护理', asset: 'A2-202', period: '待审批', amount: 72000, status: 'draft' },
]

export const billBatches: BillBatch[] = [
  { id: '1', batchNo: 'BATCH-2403-01', scope: 'A区商业街', feeType: '商铺租金', generatedCount: 120, totalAmount: 1450000, executedAt: '2024-03-01 10:00', status: 'done' },
  { id: '2', batchNo: 'BATCH-2403-02', scope: 'B区住宅(1栋-2栋)', feeType: '物业费', generatedCount: 450, totalAmount: 210000, executedAt: '2024-03-01 10:05', status: 'done' },
  { id: '3', batchNo: 'BATCH-2403-03', scope: '全小区', feeType: '阶梯水电费', generatedCount: 570, totalAmount: 85600, executedAt: '2024-03-02 08:30', status: 'processing' },
]

export const paymentRecords: LegacyPaymentRecord[] = [
  { id: '1', billNo: 'PAY-202403-001', payer: '张伟', amount: 3200, channel: '微信支付', paidAt: '2024-03-03 09:15', status: 'paid' },
  { id: '2', billNo: 'PAY-202403-018', payer: '李娜', amount: 1680, channel: '支付宝', paidAt: '2024-03-03 11:20', status: 'paid' },
  { id: '3', billNo: 'PAY-202403-026', payer: '优品超市', amount: 56000, channel: '银行转账', paidAt: '2024-03-03 14:05', status: 'pending' },
  { id: '4', billNo: 'PAY-202403-027', payer: '王强', amount: 800, channel: '人工核销', paidAt: '2024-03-03 15:30', status: 'refund' },
]

export const repairTickets: RepairTicket[] = [
  { id: 'REP-24-001', title: '卷帘门无法正常关闭', location: 'A1-105', reporter: '王店长', ageLabel: '10分钟前', stage: 'todo' },
  { id: 'REP-24-002', title: '消防通道照明灯闪烁', location: 'B1-地下车库', reporter: '巡逻保安', ageLabel: '1小时前', stage: 'todo' },
  { id: 'REP-24-003', title: '主卧空调漏水', location: 'B2-302', reporter: '138****1234', assignee: '赵工', ageLabel: '处理中', stage: 'doing' },
  { id: 'REP-24-004', title: '自动感应门异响', location: '大堂', reporter: '前台客服', assignee: '赵工', ageLabel: '已办结', stage: 'done' },
]

export const complaintRecords: LegacyComplaintRecord[] = [
  { id: 'CMP-001', resident: '陈女士', topic: '夜间噪音扰民', createdAt: '2024-03-02 20:30', status: 'new', priority: 'high' },
  { id: 'CMP-002', resident: '刘先生', topic: '快递堆放占用消防通道', createdAt: '2024-03-03 09:10', status: 'following', priority: 'medium' },
  { id: 'CMP-003', resident: '周女士', topic: '电梯广告屏亮度过高', createdAt: '2024-03-03 11:45', status: 'closed', priority: 'low' },
]

export const noticeRecords: LegacyNoticeRecord[] = [
  { id: 'N-001', title: '4月园区消防演练通知', audience: '全体业主', publishAt: '2024-03-25 09:00', status: 'published', summary: '请各楼栋提前知悉演练安排与疏散路线。' },
  { id: 'N-002', title: 'A区停车场临时封闭公告', audience: 'A区车主', publishAt: '2024-03-26 18:00', status: 'draft', summary: '因排水改造，A区停车场将临时封闭 12 小时。' },
]

export const roleRecords: LegacyRoleRecord[] = [
  {
    id: 'admin',
    name: '超级管理员',
    description: '系统最高权限',
    locked: true,
    permissionGroups: [
      { id: 'asset', title: '资产全景管理', permissions: ['查看资产树', '新增/编辑资产', '删除资产'] },
      { id: 'finance', title: '财务与收费管理', permissions: ['查看智能账单', '生成批处理账单', '财务核销确认', '导出财务报表'] },
      { id: 'service', title: '服务与工单流转', permissions: ['查看报修单', '派发工单', '修改工单状态', '处理投诉建议'] },
    ],
  },
  {
    id: 'finance-manager',
    name: '财务经理',
    description: '仅管理收费与账单',
    permissionGroups: [
      { id: 'finance', title: '财务与收费管理', permissions: ['查看智能账单', '生成批处理账单', '财务核销确认', '导出财务报表'] },
    ],
  },
  {
    id: 'repair-team',
    name: '维修工程部',
    description: '仅处理报修工单',
    permissionGroups: [
      { id: 'service', title: '服务与工单流转', permissions: ['查看报修单', '派发工单', '修改工单状态'] },
    ],
  },
  {
    id: 'service-desk',
    name: '前台客服',
    description: '公告与投诉建议',
    permissionGroups: [
      { id: 'notice', title: '公告与投诉闭环', permissions: ['发布公告', '筛选受众', '处理投诉建议'] },
    ],
  },
]

export const reportCards: ReportCard[] = [
  { id: '1', title: '收费日报', subtitle: '近 24 小时收费统计', progress: 76, metric: '已生成 12 份' },
  { id: '2', title: '资产台账导入', subtitle: '支持 Excel/CSV 模板', progress: 52, metric: '待校验 87 行' },
  { id: '3', title: '维修工单周报', subtitle: '自动汇总工单 SLA', progress: 91, metric: '8:00 自动推送' },
]

export const reportActions = [
  { id: 'export-income', title: '导出本月收入报表', icon: DocumentCopy },
  { id: 'export-assets', title: '导出资产台账', icon: DataAnalysis },
  { id: 'import-assets', title: '导入资产模板', icon: CreditCard },
]
