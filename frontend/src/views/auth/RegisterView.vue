<template>
  <div class="register-shell">
    <div class="register-panel">
      <div class="register-panel__copy">
        <h1>创建物业管理账号</h1>
        <p>填写基础资料后即可创建账号。</p>
      </div>
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top" class="register-form" @submit.prevent>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="角色 ID" prop="roleId">
          <el-input-number v-model="form.roleId" :min="1" class="full-width" />
        </el-form-item>
        <el-form-item label="设置密码" prop="password">
          <el-input v-model="form.password" type="password" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" show-password @keyup.enter="handleRegister" />
        </el-form-item>
        <el-form-item prop="agree">
          <el-checkbox v-model="form.agree">我已阅读并同意平台使用协议</el-checkbox>
        </el-form-item>
        <div class="register-actions">
          <el-button @click="router.push('/login')">返回登录</el-button>
          <el-button type="primary" class="register-submit" :loading="submitting" @click="handleRegister">
            完成注册
          </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { useRouter } from 'vue-router'
import { useAppStore } from '@/stores/app'

const router = useRouter()
const appStore = useAppStore()
const formRef = ref<FormInstance>()
const submitting = ref(false)

const form = reactive({
  username: '',
  realName: '',
  phone: '',
  email: '',
  roleId: 1,
  password: '',
  confirmPassword: '',
  agree: false,
})

const rules: FormRules<typeof form> = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1\d{10}$/, message: '请输入正确的 11 位手机号', trigger: 'blur' },
  ],
  roleId: [{ required: true, message: '请输入角色 ID', trigger: 'change' }],
  password: [
    { required: true, message: '请设置登录密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少 6 位', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        if (value !== form.password) {
          callback(new Error('两次输入的密码不一致'))
          return
        }
        callback()
      },
      trigger: 'blur',
    },
  ],
  agree: [
    {
      validator: (_rule, value, callback) => {
        if (!value) {
          callback(new Error('请先勾选使用协议'))
          return
        }
        callback()
      },
      trigger: 'change',
    },
  ],
}

async function handleRegister() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) {
    return
  }

  submitting.value = true

  try {
    await appStore.registerUser({
      username: form.username.trim(),
      password: form.password,
      realName: form.realName.trim(),
      phone: form.phone.trim(),
      email: form.email.trim() || undefined,
      roleId: form.roleId,
    })

    ElMessage.success('注册成功，请使用新账号登录')
    router.push({
      path: '/login',
      query: {
        account: form.username,
      },
    })
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '注册失败')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.register-shell {
  min-height: 100vh;
  display: grid;
  place-items: center;
  padding: 40px 24px;
  background: #fafafa;
}

.register-panel {
  width: min(100%, 960px);
  display: grid;
  grid-template-columns: 0.9fr 1.1fr;
  gap: 0;
  background: #fff;
  border: 1px solid #ebebeb;
  border-radius: 16px;
  overflow: hidden;
}

.register-panel__copy {
  padding: 48px 40px;
  background: #0a0a0a;
  color: #fff;
}

.register-panel__copy h1 {
  margin: 0 0 16px;
  font-size: 34px;
  font-weight: 600;
  letter-spacing: -0.02em;
}

.register-panel__copy p {
  margin: 0;
  line-height: 1.8;
  color: rgba(255, 255, 255, 0.55);
  font-size: 16px;
}

.register-form {
  padding: 40px;
}

.register-actions {
  display: grid;
  grid-template-columns: 130px 1fr;
  gap: 12px;
}

.register-submit {
  width: 100%;
  height: 48px;
  font-weight: 500;
  background: #0a0a0a;
  border-color: #0a0a0a;
}

.register-submit:hover {
  background: #1a1a1a;
  border-color: #1a1a1a;
}

.full-width {
  width: 100%;
}

@media (max-width: 768px) {
  .register-shell {
    align-items: start;
    padding: 20px 16px;
  }

  .register-panel {
    grid-template-columns: 1fr;
  }

  .register-panel__copy {
    padding: 36px 28px;
  }

  .register-panel__copy h1 {
    font-size: 26px;
  }

  .register-form {
    padding: 28px 20px;
  }

  .register-actions {
    grid-template-columns: 1fr;
  }
}
</style>
