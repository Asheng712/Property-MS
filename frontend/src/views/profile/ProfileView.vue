<template>
  <PageContainer title="个人中心" description="查看当前登录账号、角色权限和本地偏好设置。">
    <template #actions>
      <el-button plain @click="refreshProfile">刷新资料</el-button>
      <el-button type="danger" plain @click="logout">退出登录</el-button>
    </template>

    <div class="profile-grid">
      <section class="profile-hero surface-card">
        <div class="profile-avatar">{{ avatarText }}</div>
        <div>
          <h2>{{ appStore.currentUser }}</h2>
          <p>{{ appStore.currentRole }}</p>
        </div>
      </section>

      <PanelCard title="账号资料">
        <InfoList :items="profileItems" />
      </PanelCard>

      <PanelCard title="使用偏好">
        <div class="preference-list">
          <div class="preference-item">
            <div>
              <strong>紧凑侧边栏</strong>
              <p>在较小屏幕上保留更多业务操作空间</p>
            </div>
            <el-switch :model-value="appStore.sidebarCollapsed" @change="handleCompactChange" />
          </div>
          <div class="preference-item">
            <div>
              <strong>消息提示音</strong>
              <p>报修、投诉和收费待办变化时提示</p>
            </div>
            <el-switch :model-value="appStore.noticeSoundEnabled" @change="handleNoticeSoundChange" />
          </div>
        </div>
      </PanelCard>
    </div>
  </PageContainer>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import InfoList from '@/components/InfoList.vue'
import PageContainer from '@/components/PageContainer.vue'
import PanelCard from '@/components/PanelCard.vue'
import { useAppStore } from '@/stores/app'

const appStore = useAppStore()
const router = useRouter()

const avatarText = computed(() => appStore.currentUser.slice(0, 1))
const profileItems = computed(() => {
  const profile = appStore.currentUserProfile

  return [
    { label: '用户 ID', value: profile?.id ?? '-' },
    { label: '用户名', value: profile?.username || '-' },
    { label: '真实姓名', value: profile?.realName || '-' },
    { label: '手机号', value: profile?.phone || '-' },
    { label: '邮箱', value: profile?.email || '-' },
    { label: '角色', value: profile?.roleName || '未分配角色' },
    { label: '账号状态', value: profile?.status === 1 ? '启用' : '停用' },
  ]
})

async function refreshProfile() {
  try {
    await appStore.fetchCurrentUser()
    ElMessage.success('个人资料已更新')
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '刷新个人资料失败')
  }
}

function handleCompactChange(value: boolean | string | number) {
  const shouldCollapse = Boolean(value)
  if (appStore.sidebarCollapsed !== shouldCollapse) {
    appStore.toggleSidebar()
  }
}

function handleNoticeSoundChange(value: boolean | string | number) {
  appStore.toggleNoticeSound(Boolean(value))
  ElMessage.success(Boolean(value) ? '已开启消息提示音' : '已关闭消息提示音')
}

function logout() {
  appStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.profile-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(320px, 420px);
  gap: 20px;
}

.profile-hero {
  grid-column: 1 / -1;
  display: flex;
  align-items: center;
  gap: 18px;
  padding: 28px;
}

.profile-avatar {
  display: grid;
  place-items: center;
  width: 72px;
  height: 72px;
  border-radius: 22px;
  background: linear-gradient(135deg, #2563eb, #14b8a6);
  color: #fff;
  font-size: 30px;
  font-weight: 800;
}

.profile-hero h2 {
  margin: 0;
  color: #20304b;
  font-size: 28px;
}

.profile-hero p {
  margin: 8px 0 0;
  color: #8ea0b8;
}

.preference-list {
  display: grid;
  gap: 18px;
}

.preference-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #edf1f7;
}

.preference-item:last-child {
  padding-bottom: 0;
  border-bottom: none;
}

.preference-item strong {
  color: #24344e;
}

.preference-item p {
  margin: 6px 0 0;
  color: #8ea0b8;
}

@media (max-width: 1024px) {
  .profile-grid {
    grid-template-columns: 1fr;
  }
}
</style>
