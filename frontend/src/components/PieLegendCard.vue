<template>
  <section class="surface-card pie-card">
    <div class="section-title">
      <h3>{{ title }}</h3>
      <el-icon class="muted-text"><InfoFilled /></el-icon>
    </div>
    <div class="pie-card__body">
      <div class="pie-card__chart">
        <div class="pie-card__circle" :style="{ background: pieBackground }">
          <div class="pie-card__center">{{ centerLabel }}</div>
        </div>
      </div>
      <div class="pie-card__legend">
        <div v-for="segment in segments" :key="segment.label" class="pie-card__legend-item">
          <span class="dot" :style="{ background: segment.color }"></span>
          <span>{{ segment.label }}</span>
          <strong>{{ segment.value }}%</strong>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { InfoFilled } from '@element-plus/icons-vue'
import { computed } from 'vue'
import type { ChartSegment } from '@/types'

const props = defineProps<{
  title: string
  centerLabel: string
  segments: ChartSegment[]
}>()

const pieBackground = computed(() => {
  let current = 0
  const entries = props.segments.map((segment) => {
    const start = current
    current += segment.value
    return `${segment.color} ${start}% ${current}%`
  })
  return `conic-gradient(${entries.join(', ')})`
})
</script>

<style scoped>
.pie-card__body {
  display: grid;
  grid-template-columns: minmax(260px, 1fr) 220px;
  gap: 16px;
  align-items: center;
  min-height: 280px;
}

.pie-card__chart {
  display: flex;
  justify-content: center;
  align-items: center;
}

.pie-card__circle {
  position: relative;
  width: 220px;
  height: 220px;
  border-radius: 50%;
}

.pie-card__circle::after {
  content: '';
  position: absolute;
  inset: 36px;
  border-radius: 50%;
  background: #fff;
}

.pie-card__center {
  position: absolute;
  inset: 0;
  display: grid;
  place-items: center;
  font-size: 18px;
  font-weight: 700;
  color: #2c3c57;
  z-index: 1;
}

.pie-card__legend {
  display: grid;
  gap: 14px;
}

.pie-card__legend-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 14px;
  background: #f8fbff;
  border-radius: 16px;
}

.pie-card__legend-item strong {
  margin-left: auto;
}

.dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
}

@media (max-width: 1200px) {
  .pie-card__body {
    grid-template-columns: 1fr;
  }
}
</style>
