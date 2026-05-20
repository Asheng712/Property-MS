<template>
  <article class="stat-card surface-card">
    <div class="stat-card__icon" :style="{ background: stat.accent, color: stat.color }">
      <el-icon :size="22">
        <component :is="iconComponent" />
      </el-icon>
    </div>
    <div>
      <div class="stat-card__value">{{ stat.value }}</div>
      <div class="stat-card__label">{{ stat.label }} <span>({{ stat.unit }})</span></div>
    </div>
  </article>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import {
  Bell,
  Money,
  WarningFilled,
  Document,
  Headset,
  Clock,
  Tools,
  CircleCheckFilled,
} from '@element-plus/icons-vue'
import type { QuickStat } from '@/types'

const props = defineProps<{
  stat: QuickStat
}>()

const iconMap: Record<string, typeof Bell> = {
  bell: Bell,
  money: Money,
  warning: WarningFilled,
  document: Document,
  service: Headset,
  clock: Clock,
  tool: Tools,
  repair: CircleCheckFilled,
}

const iconComponent = computed(() => iconMap[props.stat.icon] ?? Document)
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
  flex-shrink: 0;
}

.stat-card__value {
  font-size: 24px;
  font-weight: 800;
  color: var(--text-heading);
}

.stat-card__label {
  margin-top: 6px;
  color: var(--text-subtle);
  font-size: 15px;
}

.stat-card__label span {
  color: #a2b0c4;
}
</style>
