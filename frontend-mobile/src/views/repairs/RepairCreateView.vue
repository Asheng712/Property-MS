<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useRepairStore } from '@/stores/repair'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const repairStore = useRepairStore()
const userStore = useUserStore()

const form = ref({
  houseId: '',
  content: '',
  reporter: userStore.currentUser,
  priority: 1,
})

const loading = ref(false)

async function onSubmit() {
  if (!form.value.houseId || !form.value.content) return

  loading.value = true
  try {
    await repairStore.createRepair({
      houseId: Number(form.value.houseId),
      content: form.value.content.trim(),
      reporter: form.value.reporter.trim(),
      priority: form.value.priority,
    })
    router.back()
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="page-container--with-nav">
    <van-nav-bar title="提交报修" left-arrow @click-left="router.back()" />

    <van-form @submit="onSubmit" style="margin-top: 12px">
      <van-cell-group title="报修信息">
        <van-field
          v-model="form.houseId"
          name="houseId"
          label="房屋编号"
          placeholder="请输入房屋编号"
          type="number"
          :rules="[{ required: true, message: '请输入房屋编号' }]"
        />
        <van-field
          v-model="form.reporter"
          name="reporter"
          label="联系人"
          placeholder="请输入联系人姓名"
          :rules="[{ required: true, message: '请输入联系人' }]"
        />
        <van-field
          v-model="form.content"
          name="content"
          label="报修内容"
          type="textarea"
          rows="4"
          placeholder="请描述您遇到的问题..."
          :rules="[{ required: true, message: '请描述报修内容' }]"
        />
      </van-cell-group>

      <van-cell-group title="优先级" style="margin-top: 12px">
        <van-radio-group v-model="form.priority" direction="horizontal" style="padding: 12px 16px">
          <van-radio :name="1">普通</van-radio>
          <van-radio :name="2" style="margin-left: 24px">紧急</van-radio>
        </van-radio-group>
      </van-cell-group>

      <div style="margin: 24px 16px">
        <van-button
          type="primary"
          block
          round
          native-type="submit"
          :loading="loading"
          loading-text="提交中..."
          size="large"
        >
          提交报修
        </van-button>
      </div>
    </van-form>
  </div>
</template>
