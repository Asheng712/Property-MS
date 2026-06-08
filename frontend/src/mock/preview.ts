import type {
  AssetRecord,
  AssetTreeNode,
  BillBatchRecord,
  BillRecord,
  ComplaintRecord,
  ContractRecord,
  DashboardData,
  NoticeRecord,
  PageResult,
  PaymentRecordVO,
  RepairKanban,
  UserInfo,
} from '@/types'

const user: UserInfo = {
  id: 1,
  username: 'preview',
  realName: '预览管理员',
  phone: '13800000000',
  email: 'preview@wisdompm.local',
  status: 1,
  roleId: 1,
  roleName: '超级管理员',
}

const assets: AssetRecord[] = [
  { id: 1, parentId: null, name: 'A区商业街', type: 'BUILDING', area: 3600, status: 'occupied', ownerName: '物业公司', ownerPhone: '021-88886666', createTime: '2026-05-01 09:00' },
  { id: 2, parentId: 1, name: 'A1-101 星河咖啡', type: 'SHOP', area: 128, status: 'occupied', ownerName: '张伟', ownerPhone: '138****1024', createTime: '2026-05-03 10:12' },
  { id: 3, parentId: 1, name: 'A1-102 空置商铺', type: 'SHOP', area: 96, status: 'vacant', ownerName: null, ownerPhone: null, createTime: '2026-05-04 11:20' },
  { id: 4, parentId: null, name: 'B区住宅 2 栋', type: 'BUILDING', area: 8200, status: 'sold', ownerName: '业主委员会', ownerPhone: '021-66668888', createTime: '2026-05-06 14:30' },
]

const assetTree: AssetTreeNode[] = [
  {
    id: 1,
    name: 'A区商业街',
    type: 'BUILDING',
    children: [
      { id: 2, name: 'A1-101 星河咖啡', type: 'SHOP' },
      { id: 3, name: 'A1-102 空置商铺', type: 'SHOP' },
    ],
  },
  {
    id: 4,
    name: 'B区住宅 2 栋',
    type: 'BUILDING',
    children: [
      { id: 5, name: '2-1201', type: 'RESIDENTIAL' },
      { id: 6, name: '2-1202', type: 'RESIDENTIAL' },
    ],
  },
]

const contracts: ContractRecord[] = [
  { id: 1, houseId: 2, houseName: 'A1-101 星河咖啡', tenantName: '星河咖啡', rentAmount: 168000, startDate: '2026-01-01', endDate: '2028-12-31', increaseRate: 3, deposit: 30000, contractStatus: 1, contractStatusText: '有效' },
  { id: 2, houseId: 3, houseName: 'A1-102 空置商铺', tenantName: '优品超市', rentAmount: 98000, startDate: '2025-06-01', endDate: '2026-06-30', increaseRate: 2, deposit: 18000, contractStatus: 1, contractStatusText: '有效' },
  { id: 3, houseId: 5, houseName: '2-1201', tenantName: '长住租户', rentAmount: 48000, startDate: '2025-01-01', endDate: '2025-12-31', deposit: 4000, contractStatus: 0, contractStatusText: '已终止' },
]

const billBatches: BillBatchRecord[] = [
  { id: 1, batchNo: 'BATCH-2605-01', feeType: '物业费', targetRange: '全园区', totalCount: 586, totalAmount: 286000, status: 'done', operator: 'preview', createTime: '2026-05-01 10:00' },
  { id: 2, batchNo: 'BATCH-2605-02', feeType: '商铺租金', targetRange: 'A区商业街', totalCount: 42, totalAmount: 780000, status: 'processing', operator: 'preview', createTime: '2026-05-02 09:30' },
]

const bills: BillRecord[] = [
  { id: 1, userId: 1, billNo: 'BILL-2605-001', houseId: 2, houseName: 'A1-101 星河咖啡', amount: 12800, feeType: 1, feeTypeText: '商铺租金', status: 0, statusText: '待缴费', dueDate: '2026-05-31', createTime: '2026-05-01 10:00' },
  { id: 2, userId: 1, billNo: 'BILL-2605-002', houseId: 5, houseName: '2-1201', amount: 680, feeType: 4, feeTypeText: '物业费', status: 2, statusText: '已缴费', dueDate: '2026-05-31', createTime: '2026-05-01 10:02' },
]

const payments: PaymentRecordVO[] = [
  { id: 1, paymentNo: 'PAY-2605-001', userId: 1, userName: 'preview', amount: 680, payMethod: 1, payMethodText: '微信支付', status: 1, statusText: '已核销', payTime: '2026-05-03 09:15', createTime: '2026-05-03 09:15' },
  { id: 2, paymentNo: 'PAY-2605-002', userId: 1, userName: 'preview', amount: 12800, payMethod: 3, payMethodText: '银行转账', status: 0, statusText: '待核销', payTime: '2026-05-05 11:20', createTime: '2026-05-05 11:20' },
]

const repairKanban: RepairKanban = {
  pending: [
    { id: 1, repairNo: 'REP-2605-001', houseId: 2, houseName: 'A1-101 星河咖啡', content: '卷帘门无法正常关闭', reporter: '王店长', status: 0, statusText: '待处理', priority: 2, priorityText: '紧急', createTime: '2026-05-24 09:20' },
  ],
  processing: [
    { id: 2, repairNo: 'REP-2605-002', houseId: 4, houseName: 'B区地下车库', content: '消防通道照明闪烁', reporter: '巡逻保安', workerId: 7, workerName: '赵工', status: 1, statusText: '处理中', priority: 1, priorityText: '普通', createTime: '2026-05-24 08:30' },
  ],
  completed: [
    { id: 3, repairNo: 'REP-2605-003', houseId: 5, houseName: '2-1201', content: '主卧空调漏水', reporter: '刘女士', workerId: 8, workerName: '李工', status: 2, statusText: '已完成', priority: 1, priorityText: '普通', createTime: '2026-05-23 16:10', finishTime: '2026-05-24 10:05' },
  ],
}

const complaints: ComplaintRecord[] = [
  { id: 1, complaintNo: 'CMP-2605-001', category: '噪音投诉', content: '夜间施工噪音影响休息', source: '业主App', status: 0, statusText: '待处理', createTime: '2026-05-23 22:15' },
  { id: 2, complaintNo: 'CMP-2605-002', category: '公共区域', content: '快递堆放占用消防通道', source: '前台登记', status: 1, statusText: '跟进中', handleResult: '已通知快递点整改', createTime: '2026-05-24 09:10' },
]

const notices: NoticeRecord[] = [
  { id: 1, title: '6月园区消防演练通知', content: '请各楼栋提前知悉演练安排与疏散路线。', targetType: '全体业主', status: 'published', viewCount: 368, createTime: '2026-05-20 09:00' },
  { id: 2, title: 'A区停车场临时封闭公告', content: '因排水改造，A区停车场将临时封闭 12 小时。', targetType: 'A区车主', status: 'draft', viewCount: 0, createTime: '2026-05-22 18:00' },
]

function page<T>(records: T[]): PageResult<T> {
  return {
    total: records.length,
    records,
  }
}

export function isPreviewMode() {
  return import.meta.env.DEV && window.sessionStorage.getItem('wisdompm-preview-mode') === '1'
}

export function getPreviewData<T>(path: string): T | null {
  if (path === '/api/v1/auth/info') {
    return user as T
  }

  if (path === '/api/v1/dashboard/data') {
    return {
      pendingChargeCount: 42,
      pendingChargeAmount: 186800,
      overdueCount: 8,
      overdueAmount: 42800,
      todayComplaintCount: 2,
      pendingComplaintCount: 5,
      todayRepairCount: 3,
      pendingRepairCount: 7,
      assetRentalList: [
        {
          type: 'SHOP',
          typeName: '商铺',
          totalCount: 6,
          vacantCount: 1,
          rentedCount: 1,
          soldCount: 0,
          occupiedCount: 4,
          vacantRate: 17,
          rentedRate: 17,
          soldRate: 0,
          occupiedRate: 67,
        },
        {
          type: 'RESIDENTIAL',
          typeName: '住宅',
          totalCount: 8,
          vacantCount: 1,
          rentedCount: 2,
          soldCount: 3,
          occupiedCount: 2,
          vacantRate: 13,
          rentedRate: 25,
          soldRate: 38,
          occupiedRate: 25,
        },
        {
          type: 'BUILDING',
          typeName: '楼栋',
          totalCount: 4,
          vacantCount: 0,
          rentedCount: 0,
          soldCount: 0,
          occupiedCount: 4,
          vacantRate: 0,
          rentedRate: 0,
          soldRate: 0,
          occupiedRate: 100,
        },
        {
          type: 'UNIT',
          typeName: '单元',
          totalCount: 2,
          vacantCount: 0,
          rentedCount: 2,
          soldCount: 0,
          occupiedCount: 0,
          vacantRate: 0,
          rentedRate: 100,
          soldRate: 0,
          occupiedRate: 0,
        },
      ],
    } satisfies DashboardData as T
  }

  if (path === '/api/v1/assets/tree') {
    return assetTree as T
  }

  if (path === '/api/v1/assets') {
    return page(assets) as T
  }

  if (path === '/api/v1/contracts') {
    return page(contracts) as T
  }

  if (path === '/api/v1/bills/batch-logs') {
    return page(billBatches) as T
  }

  if (path === '/api/v1/bills') {
    return page(bills) as T
  }

  if (path === '/api/v1/finance/payments') {
    return page(payments) as T
  }

  if (path === '/api/v1/repairs/kanban') {
    return repairKanban as T
  }

  if (path === '/api/v1/complaints') {
    return page(complaints) as T
  }

  if (path === '/api/v1/notices') {
    return page(notices) as T
  }

  return null
}
