<script setup lang="ts">
defineProps<{
  type: 'info' | 'warning' | 'success' | 'danger' | 'primary'
}>()
</script>

<template>
  <van-tag :type="type" size="medium" round>
    <slot />
  </van-tag>
</template>
