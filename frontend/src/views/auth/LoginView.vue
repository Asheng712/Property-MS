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
          <h1>打造下一代<br />智慧物业管理体验</h1>
          <p>基于前沿的数字化引擎，一站式解决资产、财务、服务与人员权限调度，全面提升物业服务品质与经营效率。</p>
        </div>
      </div>
    </section>

    <section class="auth-panel">
      <div class="auth-form glass-card">
        <div>
          <h2>欢迎回来</h2>
          <p>请登录您的管理账户以继续</p>
        </div>
        <el-form label-position="top" @submit.prevent>
          <el-form-item label="登录账号 / 手机号">
            <el-input v-model="form.username" placeholder="请输入管理员账号" />
          </el-form-item>
          <el-form-item>
            <template #label>
              <div class="auth-label">
                <span>登录密码</span>
                <router-link to="/register">忘记密码?</router-link>
              </div>
            </template>
            <el-input v-model="form.password" type="password" show-password placeholder="请输入登录密码" />
          </el-form-item>
          <div class="auth-remember">
            <el-checkbox v-model="form.remember">记住我</el-checkbox>
          </div>
          <el-button type="primary" size="large" class="auth-submit btn-primary-gradient" @click="handleLogin">
            登录系统
          </el-button>
          <p class="auth-footer">
            还没有账户？
            <router-link to="/register">立即注册</router-link>
          </p>
        </el-form>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAppStore } from '@/stores/app'

const router = useRouter()
const appStore = useAppStore()

const form = reactive({
  username: 'admin',
  password: '123456',
  remember: true,
})

function handleLogin() {
  appStore.setCurrentUser('管理员')
  router.push('/dashboard')
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
  max-width: 520px;
  color: #fff;
}

.auth-copy h1 {
  margin: 0 0 22px;
  font-size: 58px;
  line-height: 1.06;
}

.auth-copy p {
  margin: 0;
  font-size: 22px;
  line-height: 1.7;
  color: rgba(255, 255, 255, 0.82);
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

.auth-label a,
.auth-footer a {
  color: #2563eb;
  font-weight: 600;
}

.auth-remember {
  margin-bottom: 18px;
}

.auth-submit {
  width: 100%;
  height: 52px;
  font-size: 17px;
}

.auth-footer {
  text-align: center;
  margin-top: 20px;
}

@media (max-width: 1024px) {
  .auth-shell {
    grid-template-columns: 1fr;
  }

  .auth-hero {
    min-height: 360px;
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
}
</style>
