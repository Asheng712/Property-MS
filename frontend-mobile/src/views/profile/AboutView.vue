<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<template>
  <div class="page-container--with-nav">
    <van-nav-bar title="关于我们" left-arrow @click-left="router.back()" />

    <div style="text-align: center; padding: 40px 24px">
      <div style="width: 72px; height: 72px; border-radius: 16px; background: linear-gradient(135deg, #1989fa, #07c160); display: flex; align-items: center; justify-content: center; margin: 0 auto 16px">
        <van-icon name="manager" size="40" color="#fff" />
      </div>
      <h2 style="font-size: 22px; margin-bottom: 4px; color: var(--van-text-color)">智慧物业管理系统</h2>
      <p style="font-size: 13px; color: var(--van-text-color-weak); margin-bottom: 24px">Wisdom Property Management</p>

      <van-cell-group style="border-radius: 8px; overflow: hidden">
        <van-cell title="版本号" value="v1.1.6" />
        <van-cell title="技术栈" value="Vue3 + Vant + SpringBoot" />
        <van-cell title="联系我们" value="400-888-0000" />
      </van-cell-group>

      <p style="margin-top: 32px; font-size: 12px; color: var(--van-text-color-weak)">
        Copyright © 2025-2026 智慧物业<br />
        保留所有权利
      </p>
    </div>
  </div>
</template>
