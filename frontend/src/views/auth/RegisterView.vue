<template>
  <div class="register-shell">
    <div class="register-panel glass-card">
      <div class="register-panel__copy">
        <h1>创建物业管理账户</h1>
        <p>支持管理账号注册、园区信息初始化和默认角色配置，帮助你快速搭起第一版运营后台。</p>
      </div>
      <el-form label-position="top" class="register-form" @submit.prevent>
        <el-form-item label="公司/项目名称">
          <el-input v-model="form.company" placeholder="例如：智慧物业管理系统" />
        </el-form-item>
        <el-form-item label="管理员姓名">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.mobile" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="设置密码">
          <el-input v-model="form.password" type="password" show-password />
        </el-form-item>
        <el-button type="primary" class="btn-primary-gradient register-submit" @click="router.push('/login')">
          完成注册
        </el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const form = reactive({
  company: '',
  name: '',
  mobile: '',
  password: '',
})
</script>

<style scoped>
.register-shell {
  min-height: 100vh;
  display: grid;
  place-items: center;
  padding: 24px;
  background:
    radial-gradient(circle at top left, rgba(37, 99, 235, 0.15), transparent 35%),
    linear-gradient(180deg, #f8fbff, #eff4fb);
}

.register-panel {
  width: min(100%, 920px);
  display: grid;
  grid-template-columns: 0.9fr 1.1fr;
  gap: 24px;
  padding: 28px;
}

.register-panel__copy {
  padding: 24px;
  border-radius: 24px;
  background: linear-gradient(180deg, #245ef5, #153aa8);
  color: #fff;
}

.register-panel__copy h1 {
  margin: 0 0 14px;
  font-size: 36px;
}

.register-form {
  padding: 16px;
}

.register-submit {
  width: 100%;
  height: 48px;
}

@media (max-width: 768px) {
  .register-panel {
    grid-template-columns: 1fr;
  }
}
</style>
