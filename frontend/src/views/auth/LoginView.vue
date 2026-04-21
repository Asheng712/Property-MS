<template>
  <div class="auth-shell">
    <section class="auth-hero">
      <div class="auth-hero__overlay"></div>
      <div class="auth-hero__content">
        <div class="auth-brand">
          <div class="auth-brand__mark">W</div>
          <div>
            <strong>WisdomPM</strong>
          </div>
        </div>
        <div class="auth-copy">
          <h1>智慧物业管理平台</h1>
          <p>基于最新后端接口完成登录、台账、缴费、工单、公告等模块联调。</p>
          <ul class="auth-tips">
            <li>登录接口：`POST /api/v1/auth/login`</li>
            <li>用户信息接口：`GET /api/v1/auth/info`</li>
          </ul>
        </div>
      </div>
    </section>

    <section class="auth-panel">
      <div class="auth-form glass-card">
        <div>
          <h2>欢迎登录</h2>
          <p>请输入后端数据库中已存在的用户名和密码。</p>
        </div>
        <el-form ref="formRef" :model="form" :rules="rules" label-position="top" @submit.prevent>
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" placeholder="请输入用户名" clearable />
          </el-form-item>
          <el-form-item prop="password">
            <template #label>
              <div class="auth-label">
                <span>登录密码</span>
                <router-link to="/register">去注册</router-link>
              </div>
            </template>
            <el-input
              v-model="form.password"
              type="password"
              show-password
              placeholder="请输入登录密码"
              @keyup.enter="handleLogin"
            />
          </el-form-item>
          <div class="auth-remember">
            <el-checkbox v-model="form.remember">记住登录状态</el-checkbox>
            <span v-if="redirectText" class="auth-redirect">{{ redirectText }}</span>
          </div>
          <el-button
            type="primary"
            size="large"
            class="auth-submit btn-primary-gradient"
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
import { computed, reactive, ref } from 'vue'
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
  remember: true,
})

const rules: FormRules<typeof form> = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入登录密码', trigger: 'blur' }],
}

const redirectText = computed(() =>
  route.query.redirect ? `登录后将跳转到 ${String(route.query.redirect)}` : '',
)

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
  background: #f5f7fb;
}

.auth-hero {
  position: relative;
  overflow: hidden;
  background:
    linear-gradient(180deg, rgba(13, 43, 135, 0.05), rgba(6, 16, 58, 0.42)),
    linear-gradient(120deg, #1d4ed8, #1e40af);
}

.auth-hero::before {
  content: '';
  position: absolute;
  inset: 0;
  background:
    linear-gradient(90deg, rgba(255, 255, 255, 0.12) 0 2px, transparent 2px 80px),
    linear-gradient(180deg, rgba(255, 255, 255, 0.1) 0 2px, transparent 2px 80px);
  opacity: 0.22;
}

.auth-hero__overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(37, 99, 235, 0.08), rgba(15, 23, 42, 0.42));
}

.auth-hero__content {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 100%;
  padding: 46px 48px;
}

.auth-brand {
  display: flex;
  align-items: center;
  gap: 14px;
  color: #fff;
  font-size: 22px;
  font-weight: 800;
}

.auth-brand__mark {
  display: grid;
  place-items: center;
  width: 42px;
  height: 42px;
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.92);
  color: #2563eb;
}

.auth-copy {
  max-width: 560px;
  color: #fff;
}

.auth-copy h1 {
  margin: 0 0 22px;
  font-size: 58px;
  line-height: 1.06;
}

.auth-copy p {
  margin: 0 0 20px;
  font-size: 21px;
  line-height: 1.7;
  color: rgba(255, 255, 255, 0.82);
}

.auth-tips {
  margin: 0;
  padding-left: 20px;
  color: rgba(255, 255, 255, 0.82);
}

.auth-tips li + li {
  margin-top: 10px;
}

.auth-panel {
  display: grid;
  place-items: center;
  padding: 28px;
}

.auth-form {
  width: min(100%, 560px);
  padding: 48px;
}

.auth-form h2 {
  margin: 0;
  font-size: 42px;
  color: #20304b;
}

.auth-form p {
  color: #91a0b8;
}

.auth-label {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.auth-label a {
  color: #2563eb;
  font-weight: 600;
}

.auth-remember {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 18px;
}

.auth-redirect {
  color: #8fa0b7;
  font-size: 13px;
}

.auth-submit {
  width: 100%;
  height: 52px;
  font-size: 17px;
}

@media (max-width: 1024px) {
  .auth-shell {
    grid-template-columns: 1fr;
  }

  .auth-hero {
    min-height: 420px;
  }

  .auth-copy h1 {
    font-size: 42px;
  }

  .auth-copy p {
    font-size: 18px;
  }
}

@media (max-width: 768px) {
  .auth-hero__content,
  .auth-form {
    padding: 28px;
  }

  .auth-copy h1 {
    font-size: 34px;
  }

  .auth-remember {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
