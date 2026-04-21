<template>
  <div class="register-shell">
    <div class="register-panel glass-card">
      <div class="register-panel__copy">
        <h1>创建物业管理账号</h1>
        <p>当前注册页已接入后端 `POST /api/v1/auth/register` 接口，注册成功后可直接跳回登录页。</p>
        <ul>
          <li>用户名必须唯一</li>
          <li>角色 ID 需要和后端角色表数据对应</li>
        </ul>
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
          <el-button type="primary" class="btn-primary-gradient register-submit" :loading="submitting" @click="handleRegister">
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
  padding: 24px;
  background:
    radial-gradient(circle at top left, rgba(37, 99, 235, 0.15), transparent 35%),
    linear-gradient(180deg, #f8fbff, #eff4fb);
}

.register-panel {
  width: min(100%, 980px);
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

.register-panel__copy p {
  margin: 0 0 18px;
  line-height: 1.8;
  color: rgba(255, 255, 255, 0.86);
}

.register-panel__copy ul {
  margin: 0;
  padding-left: 18px;
  color: rgba(255, 255, 255, 0.82);
}

.register-form {
  padding: 16px;
}

.register-actions {
  display: grid;
  grid-template-columns: 140px 1fr;
  gap: 12px;
}

.register-submit {
  width: 100%;
  height: 48px;
}

.full-width {
  width: 100%;
}

@media (max-width: 768px) {
  .register-panel {
    grid-template-columns: 1fr;
  }

  .register-actions {
    grid-template-columns: 1fr;
  }
}
</style>
