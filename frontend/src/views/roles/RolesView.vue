<template>
  <PageContainer title="角色权限控制 (RBAC)" description="细粒度划分不同操作人员的权限边界，支持新增角色与本地权限实时编辑。">
    <template #actions>
      <el-button type="primary" class="btn-primary-gradient" @click="dialogVisible = true">新增角色</el-button>
    </template>

    <div class="roles-layout">
      <PanelCard title="系统角色组" class="role-list">
        <button
          v-for="role in roles"
          :key="role.id"
          type="button"
          class="role-list__item"
          :class="{ active: activeRole.id === role.id }"
          @click="activeRoleId = role.id"
        >
          <strong>{{ role.name }}</strong>
          <span>{{ role.description }}</span>
        </button>
      </PanelCard>

      <PanelCard class="permission-panel">
        <template #header>
          <div class="permission-panel__header">
            <div>
              <h3>{{ activeRole.name }}</h3>
              <p>{{ activeRole.locked ? '该角色拥有系统的完整控制权，不可修改核心权限。' : activeRole.description }}</p>
            </div>
            <el-button :disabled="Boolean(activeRole.locked)" type="primary" @click="savePermissions">保存配置</el-button>
          </div>
        </template>

        <div class="permission-group" v-for="group in editableGroups" :key="group.id">
          <div class="permission-group__title">
            <el-checkbox v-model="group.enabled" :disabled="Boolean(activeRole.locked)">{{ group.title }}</el-checkbox>
          </div>
          <div class="permission-group__grid">
            <el-checkbox
              v-for="permission in group.permissions"
              :key="permission.label"
              v-model="permission.checked"
              :disabled="Boolean(activeRole.locked) || !group.enabled"
            >
              {{ permission.label }}
            </el-checkbox>
          </div>
        </div>
      </PanelCard>
    </div>

    <el-dialog v-model="dialogVisible" title="新增角色" width="500px">
      <el-form label-position="top" class="dialog-form">
        <el-form-item label="角色名称">
          <el-input v-model="draft.name" />
        </el-form-item>
        <el-form-item label="角色说明">
          <el-input v-model="draft.description" type="textarea" rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="createRole">创建</el-button>
      </template>
    </el-dialog>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed, reactive, ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import PageContainer from '@/components/PageContainer.vue'
import PanelCard from '@/components/PanelCard.vue'
import { roleRecords } from '@/mock/data'
import type { RoleRecord } from '@/types'
import { createLocalId } from '@/utils/format'

interface EditablePermission {
  label: string
  checked: boolean
}

interface EditableGroup {
  id: string
  title: string
  enabled: boolean
  permissions: EditablePermission[]
}

const roles = ref<RoleRecord[]>(roleRecords.map((item) => ({ ...item, permissionGroups: item.permissionGroups.map((group) => ({ ...group, permissions: [...group.permissions] })) })))
const activeRoleId = ref(roles.value[0]?.id ?? '')
const dialogVisible = ref(false)
const editableGroups = ref<EditableGroup[]>([])

const draft = reactive({
  name: '',
  description: '',
})

const activeRole = computed(() => roles.value.find((item) => item.id === activeRoleId.value) ?? roles.value[0])

watch(
  activeRole,
  (role) => {
    if (!role) {
      editableGroups.value = []
      return
    }
    editableGroups.value = role.permissionGroups.map((group) => ({
      id: group.id,
      title: group.title,
      enabled: true,
      permissions: group.permissions.map((permission) => ({
        label: permission,
        checked: true,
      })),
    }))
  },
  { immediate: true },
)

function savePermissions() {
  if (!activeRole.value) {
    return
  }
  roles.value = roles.value.map((role) =>
    role.id === activeRole.value.id
      ? {
          ...role,
          permissionGroups: editableGroups.value
            .filter((group) => group.enabled)
            .map((group) => ({
              id: group.id,
              title: group.title,
              permissions: group.permissions.filter((permission) => permission.checked).map((permission) => permission.label),
            })),
        }
      : role,
  )
  ElMessage.success(`${activeRole.value.name} 的权限配置已保存`)
}

function createRole() {
  if (!draft.name.trim()) {
    ElMessage.warning('请先填写角色名称')
    return
  }
  const newRole: RoleRecord = {
    id: createLocalId('role'),
    name: draft.name,
    description: draft.description || '自定义角色',
    permissionGroups: [
      { id: 'asset', title: '资产全景管理', permissions: ['查看资产树'] },
    ],
  }
  roles.value = [...roles.value, newRole]
  activeRoleId.value = newRole.id
  dialogVisible.value = false
  draft.name = ''
  draft.description = ''
  ElMessage.success(`角色 ${newRole.name} 已创建`)
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

.permission-group {
  padding: 18px;
  border: 1px solid #e8eef6;
  border-radius: 20px;
  margin-bottom: 16px;
}

.permission-group__title {
  margin-bottom: 16px;
}

.permission-group__grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px 18px;
}

@media (max-width: 1024px) {
  .roles-layout {
    grid-template-columns: 1fr;
  }

  .permission-group__grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 768px) {
  .permission-group__grid {
    grid-template-columns: 1fr;
  }
}
</style>
