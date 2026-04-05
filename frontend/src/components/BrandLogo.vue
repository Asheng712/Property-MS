<template>
  <div class="brand-logo" :class="{ compact }">
    <div class="brand-logo__mark">W</div>
    <div v-if="!compact" class="brand-logo__text">
      <strong>智慧物业管理系统</strong>
      <span>WisdomPM</span>
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
  width: 40px;
  height: 40px;
  border-radius: 14px;
  background: linear-gradient(135deg, #3b82f6, #1d4ed8);
  color: #fff;
  font-size: 22px;
  font-weight: 800;
  box-shadow: 0 10px 24px rgba(37, 99, 235, 0.22);
}

.brand-logo__text {
  display: flex;
  flex-direction: column;
}

.brand-logo__text strong {
  font-size: 17px;
  color: #fff;
}

.brand-logo__text span {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.48);
}

.compact .brand-logo__mark {
  width: 42px;
  height: 42px;
}
</style>
