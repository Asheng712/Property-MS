type LogLevel = 'debug' | 'info' | 'warn' | 'error'

interface LogEntry {
  time: string
  level: LogLevel
  message: string
  module: string
  data?: unknown
}

const MAX_LOG_ENTRIES = 200

class JsonLogger {
  private module: string

  constructor(module: string) {
    this.module = module
  }

  private log(level: LogLevel, message: string, data?: unknown) {
    const entry: LogEntry = {
      time: new Date().toISOString(),
      level,
      message,
      module: this.module,
      ...(data !== undefined ? { data } : {}),
    }

    const output = JSON.stringify(entry)
    switch (level) {
      case 'error':
        console.error(output)
        break
      case 'warn':
        console.warn(output)
        break
      case 'debug':
        console.debug(output)
        break
      default:
        console.log(output)
    }

    this.persistEntry(entry)
  }

  private persistEntry(entry: LogEntry) {
    if (typeof window === 'undefined') return
    try {
      const key = `wisdompm-log-${Date.now()}`
      window.sessionStorage.setItem(key, JSON.stringify(entry))
      this.rotateLogsIfNeeded()
    } catch {
      this.rotateLogsIfNeeded()
    }
  }

  private rotateLogsIfNeeded() {
    const keys = Object.keys(window.sessionStorage).filter((k) =>
      k.startsWith('wisdompm-log-'),
    )
    if (keys.length > MAX_LOG_ENTRIES) {
      keys
        .slice(0, keys.length - MAX_LOG_ENTRIES / 2)
        .forEach((k) => window.sessionStorage.removeItem(k))
    }
  }

  debug(message: string, data?: unknown) {
    if (import.meta.env.DEV) this.log('debug', message, data)
  }

  info(message: string, data?: unknown) {
    this.log('info', message, data)
  }

  warn(message: string, data?: unknown) {
    this.log('warn', message, data)
  }

  error(message: string, data?: unknown) {
    this.log('error', message, data)
  }
}

export function createLogger(module: string) {
  return new JsonLogger(module)
}
