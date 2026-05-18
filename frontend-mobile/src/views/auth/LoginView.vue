<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import type { LoginPayload } from '@/types'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const form = ref<LoginPayload>({
  username: '',
  password: '',
})

const loading = ref(false)
const showPassword = ref(false)
const errorMsg = ref('')

async function onSubmit() {
  if (!form.value.username.trim()) {
    errorMsg.value = '请输入用户名'
    return
  }
  if (!form.value.password) {
    errorMsg.value = '请输入密码'
    return
  }

  errorMsg.value = ''
  loading.value = true
  try {
    await userStore.login({
      username: form.value.username.trim(),
      password: form.value.password,
    })
    const redirect = (route.query.redirect as string) || '/home'
    router.replace(redirect)
  } catch {
    errorMsg.value = '用户名或密码错误'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="page-container" style="display: flex; flex-direction: column; justify-content: center; padding: 0 32px">
    <div style="text-align: center; margin-bottom: 40px">
      <h1 style="font-size: 28px; font-weight: 700; margin-bottom: 8px">智慧物业</h1>
      <p style="color: var(--van-text-color-weak); font-size: 14px">业主自服务端</p>
    </div>

    <van-form @submit="onSubmit">
      <van-field
        v-model="form.username"
        name="username"
        label="用户名"
        placeholder="请输入用户名"
        left-icon="user-o"
        :rules="[{ required: true, message: '请输入用户名' }]"
      />
      <van-field
        v-model="form.password"
        name="password"
        label="密码"
        placeholder="请输入密码"
        left-icon="lock"
        :type="showPassword ? 'text' : 'password'"
        :right-icon="showPassword ? 'eye-o' : 'closed-eye'"
        @click-right-icon="showPassword = !showPassword"
        :rules="[{ required: true, message: '请输入密码' }]"
      />

      <div v-if="errorMsg" style="color: var(--van-danger-color); font-size: 13px; padding: 8px 16px">
        {{ errorMsg }}
      </div>

      <div style="margin: 24px 16px">
        <van-button
          type="primary"
          block
          round
          native-type="submit"
          :loading="loading"
          loading-text="登录中..."
          size="large"
        >
          登 录
        </van-button>
      </div>
    </van-form>

    <div style="text-align: center; margin-top: 16px">
      <van-button type="default" size="small" @click="router.push('/register')">
        没有账号？立即注册
      </van-button>
    </div>
  </div>
</template>
