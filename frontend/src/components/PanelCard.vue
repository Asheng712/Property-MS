<template>
  <section class="surface-card panel-card">
    <div v-if="title || $slots.header || $slots.actions" class="panel-card__header">
      <div>
        <slot name="header">
          <div class="section-title">
            <h3>{{ title }}</h3>
            <span v-if="description" class="muted-text">{{ description }}</span>
          </div>
        </slot>
      </div>
      <div v-if="$slots.actions" class="panel-card__actions">
        <slot name="actions" />
      </div>
    </div>
    <slot />
  </section>
</template>

<script setup lang="ts">
defineProps<{
  title?: string
  description?: string
}>()
</script>

<style scoped>
.panel-card__header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 18px;
}

.panel-card__actions {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}
</style>
