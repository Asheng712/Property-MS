<template>
  <article class="stat-card surface-card">
    <div class="stat-card__icon" :style="{ background: stat.accent, color: stat.color }">
      <el-icon :size="23">
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
import { Bell, CircleCheckFilled, Clock, Coin, Document, Headset, Tools, WarningFilled } from '@element-plus/icons-vue'
import { computed } from 'vue'
import type { QuickStat } from '@/types'

const props = defineProps<{
  stat: QuickStat
}>()

const iconMap: Record<string, typeof Bell> = {
  bell: Bell,
  money: Coin,
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
  min-height: 112px;
  padding: 22px;
}

.stat-card__icon {
  display: grid;
  place-items: center;
  width: 52px;
  height: 52px;
  border-radius: 50%;
  flex: 0 0 auto;
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
  line-height: 1.35;
}

.stat-card__label span {
  color: #a2b0c4;
}

@media (max-width: 480px) {
  .stat-card {
    padding: 18px;
  }

  .stat-card__value {
    font-size: 22px;
  }
}
</style>
