<template>
  <article class="stat-card glass-card">
    <div class="stat-card__icon" :style="{ background: stat.accent, color: stat.color }">
      {{ iconText }}
    </div>
    <div>
      <div class="stat-card__value">{{ stat.value }}</div>
      <div class="stat-card__label">{{ stat.label }} <span>({{ stat.unit }})</span></div>
    </div>
  </article>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { QuickStat } from '@/types'

const props = defineProps<{
  stat: QuickStat
}>()

const iconMap: Record<string, string> = {
  bell: '铃',
  money: '费',
  warning: '警',
  document: '单',
  service: '诉',
  clock: '时',
  tool: '修',
  repair: '办',
}

const iconText = computed(() => iconMap[props.stat.icon] ?? '数')
</script>

<style scoped>
.stat-card {
  display: flex;
  align-items: center;
  gap: 18px;
  padding: 22px;
}

.stat-card__icon {
  display: grid;
  place-items: center;
  width: 52px;
  height: 52px;
  border-radius: 50%;
  font-weight: 800;
  font-size: 18px;
}

.stat-card__value {
  font-size: 24px;
  font-weight: 800;
  color: #20304b;
}

.stat-card__label {
  margin-top: 6px;
  color: #8192ad;
  font-size: 15px;
}

.stat-card__label span {
  color: #a2b0c4;
}
</style>
