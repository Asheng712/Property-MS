<template>
  <div class="info-list">
    <div v-for="item in items" :key="item.label" class="info-list__item">
      <span>{{ item.label }}</span>
      <strong>{{ item.value }}</strong>
    </div>
  </div>
</template>

<script setup lang="ts">
interface InfoItem {
  label: string
  value: string | number
}

defineProps<{
  items: InfoItem[]
}>()
</script>

<style scoped>
.info-list {
  display: grid;
  gap: 14px;
}

.info-list__item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding-bottom: 12px;
  border-bottom: 1px solid #edf1f7;
}

.info-list__item:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.info-list__item span {
  color: #8da0b8;
}

.info-list__item strong {
  color: #23324a;
}
</style>
