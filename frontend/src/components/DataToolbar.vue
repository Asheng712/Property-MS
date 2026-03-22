<template>
  <div class="toolbar surface-card">
    <div class="toolbar__left">
      <el-input v-model="keywordModel" :placeholder="placeholder" clearable>
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      <el-select v-if="filters.length" v-model="statusModel" clearable :placeholder="selectPlaceholder">
        <el-option v-for="item in filters" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
    </div>
    <div class="toolbar__actions">
      <slot />
    </div>
  </div>
</template>

<script setup lang="ts">
import { Search } from '@element-plus/icons-vue'
import { computed } from 'vue'

interface FilterOption {
  label: string
  value: string
}

const props = withDefaults(
  defineProps<{
    keyword: string
    status?: string
    placeholder?: string
    selectPlaceholder?: string
    filters?: FilterOption[]
  }>(),
  {
    status: '',
    placeholder: '请输入关键词',
    selectPlaceholder: '筛选状态',
    filters: () => [],
  },
)

const emit = defineEmits<{
  (event: 'update:keyword', value: string): void
  (event: 'update:status', value: string): void
}>()

const keywordModel = computed({
  get: () => props.keyword,
  set: (value: string) => emit('update:keyword', value),
})

const statusModel = computed({
  get: () => props.status ?? '',
  set: (value: string) => emit('update:status', value),
})
</script>

<style scoped>
.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 14px;
}

.toolbar__left {
  display: flex;
  gap: 12px;
  flex: 1;
}

.toolbar__left .el-input,
.toolbar__left .el-select {
  max-width: 360px;
}

.toolbar__actions {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

@media (max-width: 768px) {
  .toolbar {
    flex-direction: column;
    align-items: stretch;
  }

  .toolbar__left {
    flex-direction: column;
  }
}
</style>
