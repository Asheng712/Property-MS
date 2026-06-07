<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useComplaintStore } from '@/stores/complaint'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const complaintStore = useComplaintStore()
const userStore = useUserStore()

const categories = ['噪音扰民', '环境卫生', '安全隐患', '物业服务', '设施维修', '其他']

const form = ref({
  category: '',
  content: '',
})

const showCategoryPicker = ref(false)
const loading = ref(false)

function selectCategory(cat: string) {
  form.value.category = cat
  showCategoryPicker.value = false
}

async function onSubmit() {
  if (!form.value.category || !form.value.content) return

  loading.value = true
  try {
    await complaintStore.createComplaint({
      category: form.value.category,
      content: form.value.content.trim(),
      source: userStore.userInfo?.realName || userStore.userInfo?.username || 'APP',
    })
    router.back()
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="page-container--with-nav">
    <van-nav-bar title="提交投诉建议" left-arrow @click-left="router.back()" />

    <van-form @submit="onSubmit" style="margin-top: 12px">
      <van-cell-group title="投诉信息">
        <van-field
          v-model="form.category"
          name="category"
          label="分类"
          placeholder="请选择分类"
          readonly
          is-link
          :rules="[{ required: true, message: '请选择分类' }]"
          @click="showCategoryPicker = true"
        />
        <van-field
          v-model="form.content"
          name="content"
          label="内容"
          type="textarea"
          rows="4"
          placeholder="请描述您的意见或建议..."
          :rules="[{ required: true, message: '请填写投诉内容' }]"
        />
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
          提交投诉
        </van-button>
      </div>
    </van-form>

    <van-action-sheet
      v-model:show="showCategoryPicker"
      title="选择分类"
      :actions="categories.map(c => ({ name: c }))"
      @select="({ name }) => selectCategory(name)"
    />
  </div>
</template>
