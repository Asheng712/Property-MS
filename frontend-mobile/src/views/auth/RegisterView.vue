<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import type { RegisterPayload } from '@/types'

const router = useRouter()
const userStore = useUserStore()

const form = ref<RegisterPayload & { confirmPassword: string }>({
  username: '',
  password: '',
  confirmPassword: '',
  realName: '',
  phone: '',
  roleId: 2,
})

const loading = ref(false)

async function onSubmit() {
  if (form.value.password !== form.value.confirmPassword) {
    return
  }

  loading.value = true
  try {
    await userStore.register({
      username: form.value.username.trim(),
      password: form.value.password,
      realName: form.value.realName.trim(),
      phone: form.value.phone.trim(),
      roleId: form.value.roleId,
    })
    router.push('/login')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="page-container">
    <van-nav-bar
      title="注册"
      left-arrow
      @click-left="router.back()"
    />

    <van-form @submit="onSubmit" style="margin-top: 12px">
      <van-field
        v-model="form.username"
        name="username"
        label="用户名"
        placeholder="请输入用户名"
        :rules="[{ required: true, message: '请输入用户名' }]"
      />
      <van-field
        v-model="form.realName"
        name="realName"
        label="真实姓名"
        placeholder="请输入真实姓名"
        :rules="[{ required: true, message: '请输入真实姓名' }]"
      />
      <van-field
        v-model="form.phone"
        name="phone"
        label="手机号"
        type="tel"
        placeholder="请输入手机号"
        :rules="[{ required: true, message: '请输入手机号' }]"
      />
      <van-field
        v-model="form.password"
        name="password"
        label="密码"
        type="password"
        placeholder="请输入密码"
        :rules="[{ required: true, message: '请输入密码' }]"
      />
      <van-field
        v-model="form.confirmPassword"
        name="confirmPassword"
        label="确认密码"
        type="password"
        placeholder="请再次输入密码"
        :rules="[
          { required: true, message: '请确认密码' },
          { validator: () => form.password === form.confirmPassword, message: '两次密码不一致' },
        ]"
      />

      <div style="margin: 24px 16px">
        <van-button
          type="primary"
          block
          round
          native-type="submit"
          :loading="loading"
          loading-text="注册中..."
          size="large"
        >
          注 册
        </van-button>
      </div>
    </van-form>
  </div>
</template>
