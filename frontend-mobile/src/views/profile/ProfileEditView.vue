<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { authApi } from '@/services/api'
import { showToast } from 'vant'

const router = useRouter()
const userStore = useUserStore()

const form = ref({
  realName: userStore.userInfo?.realName || '',
  phone: userStore.userInfo?.phone || '',
  email: userStore.userInfo?.email || '',
})

const loading = ref(false)

async function onSubmit() {
  if (!form.value.realName.trim()) {
    showToast('请输入真实姓名')
    return
  }
  if (!form.value.phone.trim()) {
    showToast('请输入手机号')
    return
  }

  loading.value = true
  try {
    await authApi.updateProfile({
      realName: form.value.realName.trim(),
      phone: form.value.phone.trim(),
      email: form.value.email.trim() || undefined,
    })
    // 更新本地 store 中的用户信息
    if (userStore.userInfo) {
      userStore.userInfo.realName = form.value.realName.trim()
      userStore.userInfo.phone = form.value.phone.trim()
      userStore.userInfo.email = form.value.email.trim() || undefined
    }
    showToast('保存成功')
    router.back()
  } catch (e: any) {
    showToast(e?.message || '保存失败')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="page-container--with-nav">
    <van-nav-bar title="编辑资料" left-arrow @click-left="router.back()" />

    <van-form @submit="onSubmit" style="margin-top: 12px">
      <van-cell-group title="个人信息">
        <van-field
          v-model="form.realName"
          name="realName"
          label="真实姓名"
          placeholder="请输入真实姓名"
        />
        <van-field
          v-model="form.phone"
          name="phone"
          label="手机号"
          type="tel"
          placeholder="请输入手机号"
        />
        <van-field
          v-model="form.email"
          name="email"
          label="邮箱"
          placeholder="请输入邮箱（选填）"
        />
      </van-cell-group>

      <div style="margin: 24px 16px">
        <van-button
          type="primary"
          block
          round
          native-type="submit"
          :loading="loading"
          loading-text="保存中..."
          size="large"
        >
          保存修改
        </van-button>
      </div>
    </van-form>
  </div>
</template>
