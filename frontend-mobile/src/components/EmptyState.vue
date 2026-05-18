<script setup lang="ts">
defineProps<{
  description?: string
  actionText?: string
}>()

defineEmits<{
  action: []
}>()
</script>

<template>
  <div class="empty-container">
    <van-empty :description="description || '暂无数据'" />
    <van-button
      v-if="actionText"
      type="primary"
      size="small"
      round
      @click="$emit('action')"
    >
      {{ actionText }}
    </van-button>
  </div>
</template>
