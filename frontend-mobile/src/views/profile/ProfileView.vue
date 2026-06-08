<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const showLogoutDialog = ref(false)
const notificationEnabled = ref(true)

function handleLogout() {
  userStore.logout()
  router.replace('/login')
}
</script>

<template>
  <div class="page-container">
    <van-nav-bar title="个人中心" fixed placeholder />

    <div style="padding: 24px 16px; text-align: center; background: linear-gradient(135deg, #1989fa, #07c160); color: #fff; margin: 12px; border-radius: 12px">
      <div style="width: 64px; height: 64px; border-radius: 50%; background: rgba(255,255,255,0.3); display: flex; align-items: center; justify-content: center; margin: 0 auto 12px; border: 3px solid rgba(255,255,255,0.4)">
        <van-icon name="manager" size="36" color="#fff" />
      </div>
      <h3 style="font-size: 20px; margin-bottom: 4px">{{ userStore.currentUser }}</h3>
      <p style="font-size: 13px; opacity: 0.8">{{ userStore.userInfo?.roleName || '业主' }}</p>
      <van-button
        size="small"
        round
        style="margin-top: 12px; background: rgba(255,255,255,0.2); color: #fff; border: 1px solid rgba(255,255,255,0.4)"
        @click="router.push('/profile/edit')"
      >
        编辑资料
      </van-button>
    </div>

    <van-cell-group title="功能入口" style="margin: 12px; border-radius: 8px; overflow: hidden">
      <van-cell title="我的合同" icon="description-o" is-link @click="router.push('/contracts')" />
      <van-cell title="我的报修" icon="records-o" is-link @click="router.push('/repairs')" />
      <van-cell title="我的投诉" icon="comment-o" is-link @click="router.push('/complaints')" />
    </van-cell-group>

    <van-cell-group title="设置" style="margin: 12px; border-radius: 8px; overflow: hidden">
      <van-cell title="消息通知" icon="bell-o">
        <template #right-icon>
          <van-switch v-model="notificationEnabled" size="24px" />
        </template>
      </van-cell>
      <van-cell title="关于我们" icon="info-o" is-link value="v2.1.1" @click="router.push('/profile/about')" />
      <van-cell
        title="退出登录"
        icon="revoke"
        title-style="color: var(--van-danger-color)"
        center
        @click="showLogoutDialog = true"
      />
    </van-cell-group>

    <van-dialog
      v-model:show="showLogoutDialog"
      title="确认退出"
      message="确定要退出登录吗？"
      show-cancel-button
      @confirm="handleLogout"
    />
  </div>
</template>
