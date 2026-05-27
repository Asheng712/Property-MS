<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { showToast } from 'vant'

const router = useRouter()
const userStore = useUserStore()

const form = ref({
  realName: userStore.userInfo?.realName || '',
  phone: userStore.userInfo?.phone || '',
  email: userStore.userInfo?.email || '',
})

const loading = ref(false)

function onSubmit() {
  loading.value = true
  showToast('该功能暂未开放，请联系管理员')
  loading.value = false
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
