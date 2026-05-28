<template>
  <div class="auth-shell">
    <section class="auth-hero">
      <div class="auth-hero__content">
        <div class="auth-brand">
          <div class="auth-brand__mark">W</div>
          <div>
            <strong>WisdomPM</strong>
          </div>
        </div>
        <div class="auth-copy">
          <h1>智慧物业管理平台</h1>
          <p>统一管理资产、收费、工单、投诉和公告。</p>
        </div>
      </div>
    </section>

    <section class="auth-panel">
      <div class="auth-form">
        <div>
          <h2>欢迎登录</h2>
          <p>请输入用户名和密码进入系统。</p>
        </div>
        <el-form ref="formRef" :model="form" :rules="rules" label-position="top" @submit.prevent>
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" placeholder="请输入用户名" clearable />
          </el-form-item>
          <el-form-item prop="password">
            <template #label>
              <span>登录密码</span>
            </template>
            <el-input
              v-model="form.password"
              type="password"
              show-password
              placeholder="请输入登录密码"
              @keyup.enter="handleLogin"
            />
          </el-form-item>
          <div class="auth-form__links">
            <router-link to="/register">没有账号？立即注册</router-link>
          </div>
          <el-button
            type="primary"
            size="large"
            class="auth-submit"
            :loading="submitting"
            @click="handleLogin"
          >
            登录系统
          </el-button>
        </el-form>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { useRoute, useRouter } from 'vue-router'
import { useAppStore } from '@/stores/app'

const router = useRouter()
const route = useRoute()
const appStore = useAppStore()
const formRef = ref<FormInstance>()
const submitting = ref(false)

const form = reactive({
  username: (route.query.account as string) || '',
  password: '',
})

const rules: FormRules<typeof form> = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入登录密码', trigger: 'blur' }],
}

async function handleLogin() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) {
    return
  }

  submitting.value = true

  try {
    const profile = await appStore.loginByCredential({
      username: form.username.trim(),
      password: form.password,
    })

    ElMessage.success(`欢迎回来，${profile.realName || profile.username}`)
    const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : '/dashboard'
    router.push(redirect)
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '登录失败')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.auth-shell {
  display: grid;
  grid-template-columns: 1.05fr 1fr;
  min-height: 100vh;
  background: #fafafa;
}

.auth-hero {
  position: relative;
  overflow: hidden;
  background: #0a0a0a;
}

.auth-hero__content {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 100%;
  padding: 56px 64px;
}

.auth-brand {
  display: flex;
  align-items: center;
  gap: 14px;
  color: #fff;
  font-size: 20px;
  font-weight: 700;
}

.auth-brand__mark {
  display: grid;
  place-items: center;
  width: 42px;
  height: 42px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.15);
  color: #fff;
}

.auth-copy {
  max-width: 560px;
  color: #fff;
}

.auth-copy h1 {
  margin: 0 0 24px;
  font-size: 52px;
  font-weight: 600;
  line-height: 1.08;
  letter-spacing: -0.02em;
}

.auth-copy p {
  margin: 0;
  font-size: 20px;
  line-height: 1.7;
  color: rgba(255, 255, 255, 0.55);
}

.auth-panel {
  display: grid;
  place-items: center;
  padding: 40px;
}

.auth-form {
  width: min(100%, 520px);
  padding: 48px;
  background: #fff;
  border: 1px solid #ebebeb;
  border-radius: 16px;
}

.auth-form h2 {
  margin: 0;
  font-size: 32px;
  font-weight: 600;
  color: var(--text-heading);
  letter-spacing: -0.01em;
}

.auth-form p {
  margin: 8px 0 0;
  color: var(--text-subtle);
  font-size: 15px;
}

.auth-form__links {
  display: flex;
  justify-content: flex-end;
  margin: -4px 0 18px;
  font-size: 14px;
}

.auth-form__links a {
  color: var(--text-heading);
  font-weight: 500;
  text-decoration: underline;
  text-underline-offset: 3px;
}

.auth-submit {
  width: 100%;
  height: 50px;
  font-size: 16px;
  font-weight: 500;
  background: #0a0a0a;
  border-color: #0a0a0a;
}

.auth-submit:hover {
  background: #1a1a1a;
  border-color: #1a1a1a;
}

@media (max-width: 1024px) {
  .auth-shell {
    grid-template-columns: 1fr;
  }

  .auth-hero {
    min-height: 300px;
  }

  .auth-copy h1 {
    font-size: 38px;
  }

  .auth-copy p {
    font-size: 17px;
  }
}

@media (max-width: 768px) {
  .auth-shell {
    min-height: 100dvh;
  }

  .auth-hero {
    min-height: 260px;
  }

  .auth-hero__content {
    padding: 32px 28px;
  }

  .auth-copy h1 {
    font-size: 30px;
  }

  .auth-form {
    padding: 32px 24px;
  }

  .auth-form h2 {
    font-size: 28px;
  }
}

@media (max-width: 480px) {
  .auth-hero {
    min-height: 220px;
  }

  .auth-hero__content {
    padding: 28px 24px;
  }

  .auth-copy h1 {
    font-size: 26px;
  }

  .auth-copy p {
    font-size: 15px;
  }

  .auth-panel {
    align-items: start;
    padding: 24px 16px 32px;
  }

  .auth-form {
    padding: 28px 20px;
  }
}
</style>
