<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const services = [
  {
    title: '我要报修',
    desc: '房屋设施故障？快速提交维修工单',
    icon: 'records-o',
    color: '#1989fa',
    path: '/repairs/create',
  },
  {
    title: '我要投诉',
    desc: '对物业服务有意见？提交投诉建议',
    icon: 'comment-o',
    color: '#ff976a',
    path: '/complaints/create',
  },
  {
    title: '我的报修',
    desc: '查看报修工单处理进度',
    icon: 'todo-list-o',
    color: '#07c160',
    path: '/repairs',
  },
  {
    title: '我的投诉',
    desc: '查看投诉建议处理结果',
    icon: 'envelop-o',
    color: '#ee0a24',
    path: '/complaints',
  },
]
</script>

<template>
  <div class="page-container">
    <van-nav-bar title="服务" fixed placeholder />

    <div style="padding: 16px 12px; display: flex; flex-direction: column; gap: 12px">
      <div
        v-for="item in services"
        :key="item.path"
        class="card-wrapper"
        style="padding: 16px; display: flex; align-items: center; gap: 14px; cursor: pointer"
        @click="router.push(item.path)"
      >
        <div
          :style="{
            width: '48px',
            height: '48px',
            borderRadius: '12px',
            background: item.color,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            flexShrink: '0',
          }"
        >
          <van-icon :name="item.icon" size="24" color="#fff" />
        </div>
        <div style="flex: 1">
          <h4 style="font-size: 16px; margin-bottom: 4px">{{ item.title }}</h4>
          <p style="font-size: 12px; color: var(--van-text-color-weak)">{{ item.desc }}</p>
        </div>
        <van-icon name="arrow" color="var(--van-text-color-weak)" />
      </div>
    </div>
  </div>
</template>
