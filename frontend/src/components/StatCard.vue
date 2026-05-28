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
  gap: 20px;
  min-height: 108px;
  padding: 24px 28px;
}

.stat-card__icon {
  display: grid;
  place-items: center;
  width: 48px;
  height: 48px;
  border-radius: 12px;
  flex: 0 0 auto;
}

.stat-card__value {
  font-size: 28px;
  font-weight: 700;
  color: var(--text-heading);
  letter-spacing: -0.02em;
  line-height: 1;
}

.stat-card__label {
  margin-top: 8px;
  color: var(--text-subtle);
  font-size: 14px;
  line-height: 1.35;
}

.stat-card__label span {
  color: #bababa;
}

@media (max-width: 480px) {
  .stat-card {
    padding: 20px 22px;
  }

  .stat-card__value {
    font-size: 24px;
  }
}
</style>
