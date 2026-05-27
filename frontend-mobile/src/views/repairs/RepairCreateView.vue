<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useRepairStore } from '@/stores/repair'
import { assetApi } from '@/services/api'
import type { AssetRecord } from '@/types'

const router = useRouter()
const repairStore = useRepairStore()

const houses = ref<AssetRecord[]>([])
const showHousePicker = ref(false)
const selectedHouseName = ref('')

const form = ref({
  houseId: null as number | null,
  content: '',
  priority: 1,
})

const loading = ref(false)

onMounted(async () => {
  try {
    houses.value = await assetApi.getMyHouses()
  } catch {
    // silently fail
  }
})

const housePickerColumns = computed(() =>
  houses.value.map((h) => ({ text: h.name, id: h.id }))
)

function onHouseConfirm({ selectedOptions }: any) {
  const selected = selectedOptions[0]
  if (selected) {
    form.value.houseId = selected.id
    selectedHouseName.value = selected.text
  }
  showHousePicker.value = false
}

async function onSubmit() {
  if (!form.value.houseId || !form.value.content) return

  loading.value = true
  try {
    await repairStore.createRepair({
      houseId: form.value.houseId,
      content: form.value.content.trim(),
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
          v-model="selectedHouseName"
          name="houseId"
          label="房屋"
          placeholder="请选择房屋"
          readonly
          is-link
          :rules="[{ required: true, message: '请选择房屋' }]"
          @click="showHousePicker = true"
        />
        <van-popup v-model:show="showHousePicker" position="bottom" round>
          <van-picker
            :columns="housePickerColumns"
            title="选择房屋"
            @confirm="onHouseConfirm"
            @cancel="showHousePicker = false"
          />
        </van-popup>
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
