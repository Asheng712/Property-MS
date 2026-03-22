<template>
  <PageContainer title="管理员运营看板" description="聚合收费、报修、投诉和资产经营数据，帮助管理人员快速了解园区整体运行状态。">
    <template #actions>
      <el-button plain>首页</el-button>
    </template>

    <div class="dashboard-grid">
      <StatCard v-for="stat in dashboardStats" :key="stat.id" :stat="stat" />
    </div>

    <div class="chart-grid">
      <PieLegendCard title="商铺租售情况" center-label="商铺租售比率" :segments="rentalChart" />
      <PieLegendCard title="车位租售情况" center-label="车位租售比率" :segments="parkingChart" />
    </div>
  </PageContainer>
</template>

<script setup lang="ts">
import PageContainer from '@/components/PageContainer.vue'
import PieLegendCard from '@/components/PieLegendCard.vue'
import StatCard from '@/components/StatCard.vue'
import { dashboardStats, parkingChart, rentalChart } from '@/mock/data'
</script>

<style scoped>
.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 20px;
}

.chart-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 20px;
  margin-top: 20px;
}

@media (max-width: 1200px) {
  .dashboard-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .chart-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .dashboard-grid {
    grid-template-columns: 1fr;
  }
}
</style>
