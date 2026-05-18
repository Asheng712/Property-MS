<script setup lang="ts">
defineProps<{
  rows?: number
}>()
</script>

<template>
  <div style="padding: 16px">
    <van-skeleton title avatar :row="rows || 3" v-for="i in 3" :key="i" style="margin-bottom: 16px" />
  </div>
</template>
