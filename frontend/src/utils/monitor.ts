import { createLogger } from './logger'

const logger = createLogger('monitor')

interface RequestMetrics {
  total: number
  success: number
  error: number
  totalDuration: number
}

interface AggregateMetrics {
  requests: RequestMetrics
  lastUpdated: string
}

const metrics: AggregateMetrics = {
  requests: { total: 0, success: 0, error: 0, totalDuration: 0 },
  lastUpdated: new Date().toISOString(),
}

function updateTimestamp() {
  metrics.lastUpdated = new Date().toISOString()
}

export const monitor = {
  recordRequest(duration: number, success: boolean) {
    metrics.requests.total++
    metrics.requests.totalDuration += duration
    if (success) {
      metrics.requests.success++
    } else {
      metrics.requests.error++
    }
    updateTimestamp()

    logger.info('request recorded', {
      duration,
      success,
      total: metrics.requests.total,
    })
  },

  getMetrics(): AggregateMetrics {
    return {
      requests: { ...metrics.requests },
      lastUpdated: metrics.lastUpdated,
    }
  },

  getRequestCount() {
    return metrics.requests.total
  },

  getErrorRate() {
    if (metrics.requests.total === 0) return 0
    return (metrics.requests.error / metrics.requests.total) * 100
  },

  getAverageResponseTime() {
    if (metrics.requests.total === 0) return 0
    return metrics.requests.totalDuration / metrics.requests.total
  },

  getActiveUsers(): number {
    try {
      const token = window.sessionStorage.getItem('wisdompm-token')
      return token ? 1 : 0
    } catch {
      return 0
    }
  },

  reset() {
    metrics.requests = { total: 0, success: 0, error: 0, totalDuration: 0 }
    updateTimestamp()
    logger.info('metrics reset')
  },
}

export function getSystemHealth() {
  const avgTime = monitor.getAverageResponseTime()
  const errorRate = monitor.getErrorRate()

  const apiHealthy = errorRate < 10
  const responseHealthy = avgTime < 3000
  const status = apiHealthy && responseHealthy ? 'healthy' : 'degraded'

  return {
    status,
    issues: [
      ...(!apiHealthy ? [`错误率过高: ${errorRate.toFixed(1)}%`] : []),
      ...(!responseHealthy ? [`平均响应时间过长: ${avgTime.toFixed(0)}ms`] : []),
    ],
  }
}
