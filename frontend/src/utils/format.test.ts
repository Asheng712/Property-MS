import { afterEach, describe, expect, it, vi } from 'vitest'

import { createLocalId, formatArea, formatCurrency, formatDateRange, nowText } from './format'

describe('format utils', () => {
  afterEach(() => {
    vi.restoreAllMocks()
    vi.useRealTimers()
  })

  it('formats currency with two decimal places', () => {
    expect(formatCurrency(123456.7)).toBe('¥123,456.70')
  })

  it('formats area with square meter unit', () => {
    expect(formatArea(88.5)).toBe('88.5 ㎡')
  })

  it('creates local ids with the provided prefix', () => {
    vi.spyOn(Date, 'now').mockReturnValue(1713777600000)

    expect(createLocalId('notice')).toBe('notice-1713777600000')
  })

  it('formats the current local time', () => {
    vi.useFakeTimers()
    vi.setSystemTime(new Date(2026, 3, 23, 9, 5))

    expect(nowText()).toBe('2026-04-23 09:05')
  })

  it('formats date ranges and empty values', () => {
    expect(formatDateRange('2026-04-01', '2026-04-30')).toBe('2026-04-01 至 2026-04-30')
    expect(formatDateRange(null, null)).toBe('-')
  })
})
