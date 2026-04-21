export function formatCurrency(value: number) {
  return `¥${value.toLocaleString('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  })}`
}

export function formatArea(value: number) {
  return `${value} ㎡`
}

export function createLocalId(prefix: string) {
  return `${prefix}-${Date.now()}`
}

export function nowText() {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  const hour = String(now.getHours()).padStart(2, '0')
  const minute = String(now.getMinutes()).padStart(2, '0')
  return `${year}-${month}-${day} ${hour}:${minute}`
}

export function formatDateRange(start?: string | null, end?: string | null) {
  return [start, end].filter(Boolean).join(' 至 ') || '-'
}
