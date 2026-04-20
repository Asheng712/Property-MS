<template>
  <PageContainer title="角色权限控制" description="已接入角色分页查询与权限字符串更新接口。">
    <div class="roles-layout">
      <PanelCard title="系统角色列表" class="role-list">
        <button
          v-for="role in roles"
          :key="role.id"
          type="button"
          class="role-list__item"
          :class="{ active: activeRole?.id === role.id }"
          @click="activeRoleId = role.id"
        >
          <strong>{{ role.roleName }}</strong>
          <span>{{ role.roleKey }}</span>
        </button>
      </PanelCard>

      <PanelCard class="permission-panel">
        <template #header>
          <div class="permission-panel__header">
            <div>
              <h3>{{ activeRole?.roleName || '未选择角色' }}</h3>
              <p>后端当前以字符串形式存储权限，多个权限建议使用英文逗号分隔。</p>
            </div>
            <el-button type="primary" :loading="submitting" @click="savePermissions">保存配置</el-button>
          </div>
        </template>

        <el-input
          v-model="permissionText"
          type="textarea"
          :rows="12"
          placeholder="例如：asset:view,asset:edit,finance:audit"
        />
      </PanelCard>
    </div>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import PageContainer from '@/components/PageContainer.vue'
import PanelCard from '@/components/PanelCard.vue'
import { roleApi } from '@/services/api'
import type { RoleRecord } from '@/types'

const submitting = ref(false)
const roles = ref<RoleRecord[]>([])
const activeRoleId = ref<number | null>(null)
const permissionText = ref('')

const activeRole = computed(() => roles.value.find((item) => item.id === activeRoleId.value) ?? null)

watch(
  activeRole,
  (role) => {
    permissionText.value = role?.permissions || ''
  },
  { immediate: true },
)

onMounted(() => {
  void loadRoles()
})

async function loadRoles() {
  try {
    const result = await roleApi.getList({
      page: 1,
      pageSize: 50,
    })
    roles.value = result.records
    activeRoleId.value = result.records[0]?.id ?? null
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '加载角色失败')
  }
}

async function savePermissions() {
  if (!activeRole.value) {
    ElMessage.warning('请先选择角色')
    return
  }

  submitting.value = true
  try {
    await roleApi.updatePermissions(activeRole.value.id, {
      permissions: permissionText.value.trim(),
    })
    ElMessage.success('角色权限已保存')
    await loadRoles()
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '更新角色权限失败')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.roles-layout {
  display: grid;
  grid-template-columns: 260px minmax(0, 1fr);
  gap: 20px;
}

.role-list {
  display: grid;
  gap: 12px;
  align-content: start;
}

.role-list__item {
  border: 1px solid #e7eef6;
  background: #fff;
  border-radius: 18px;
  padding: 16px;
  text-align: left;
  cursor: pointer;
  transition: all 0.2s ease;
}

.role-list__item strong {
  display: block;
  color: #20304b;
  margin-bottom: 8px;
}

.role-list__item span {
  color: #8fa0b7;
}

.role-list__item.active {
  border-color: #b8d0ff;
  background: #eef4ff;
}

.permission-panel__header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  width: 100%;
}

.permission-panel__header h3 {
  margin: 0 0 8px;
  font-size: 28px;
}

.permission-panel__header p {
  margin: 0;
  color: #8ea0b8;
}

@media (max-width: 1024px) {
  .roles-layout {
    grid-template-columns: 1fr;
  }
}
</style>
