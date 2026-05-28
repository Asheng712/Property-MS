<template>
  <div class="brand-logo" :class="{ compact }">
    <div class="brand-logo__mark">W</div>
    <div v-if="!compact" class="brand-logo__text">
      <strong>WisdomPM</strong>
      <span>智慧物业管理系统</span>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  compact?: boolean
}>()
</script>

<style scoped>
.brand-logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.brand-logo__mark {
  display: grid;
  place-items: center;
  width: 38px;
  height: 38px;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.15);
  color: #fff;
  font-size: 20px;
  font-weight: 700;
}

.brand-logo__text {
  display: flex;
  flex-direction: column;
}

.brand-logo__text strong {
  font-size: 16px;
  color: #fff;
  font-weight: 600;
  letter-spacing: -0.01em;
}

.brand-logo__text span {
  font-size: 11px;
  color: rgba(255, 255, 255, 0.35);
}

.compact .brand-logo__mark {
  width: 38px;
  height: 38px;
}
</style>
