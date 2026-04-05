<template>
  <div class="register-shell">
    <div class="register-panel glass-card">
      <div class="register-panel__copy">
        <h1>创建物业管理账户</h1>
        <p>支持管理账号注册、园区信息初始化和默认角色配置，帮助你快速搭起第一版运营后台。</p>
        <ul>
          <li>注册后自动写入本地演示用户池</li>
          <li>默认支持跳转回登录并带入账号信息</li>
        </ul>
      </div>
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top" class="register-form" @submit.prevent>
        <el-form-item label="公司/项目名称" prop="company">
          <el-input v-model="form.company" placeholder="例如：智慧物业管理系统" />
        </el-form-item>
        <el-form-item label="管理员姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="form.mobile" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="默认角色" prop="role">
          <el-select v-model="form.role">
            <el-option label="超级管理员" value="超级管理员" />
            <el-option label="财务经理" value="财务经理" />
            <el-option label="前台客服" value="前台客服" />
            <el-option label="维修工程部" value="维修工程部" />
          </el-select>
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

type RegisterRole = '超级管理员' | '财务经理' | '前台客服' | '维修工程部'

const router = useRouter()
const appStore = useAppStore()
const formRef = ref<FormInstance>()
const submitting = ref(false)

const form = reactive({
  company: '',
  name: '',
  mobile: '',
  role: '超级管理员' as RegisterRole,
  password: '',
  confirmPassword: '',
  agree: false,
})

const rules: FormRules<typeof form> = {
  company: [{ required: true, message: '请输入项目或公司名称', trigger: 'blur' }],
  name: [{ required: true, message: '请输入管理员姓名', trigger: 'blur' }],
  mobile: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1\d{10}$/, message: '请输入正确的 11 位手机号', trigger: 'blur' },
  ],
  role: [{ required: true, message: '请选择默认角色', trigger: 'change' }],
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
  const result = appStore.registerUser({
    company: form.company,
    name: form.name,
    mobile: form.mobile,
    password: form.password,
    role: form.role,
  })
  submitting.value = false

  if (!result.ok) {
    ElMessage.error(result.message)
    return
  }

  ElMessage.success('注册成功，请登录新账户')
  router.push({
    path: '/login',
    query: {
      account: result.user.phone,
      role: result.user.role,
    },
  })
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

.register-panel__copy li + li {
  margin-top: 10px;
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

@media (max-width: 768px) {
  .register-panel {
    grid-template-columns: 1fr;
  }

  .register-actions {
    grid-template-columns: 1fr;
  }
}
</style>
