import { createRouter, createWebHistory } from 'vue-router'
import AdminLayout from '@/layouts/AdminLayout.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/dashboard',
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/auth/LoginView.vue'),
      meta: { title: '登录系统' },
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/auth/RegisterView.vue'),
      meta: { title: '立即注册' },
    },
    {
      path: '/',
      component: AdminLayout,
      children: [
        { path: 'dashboard', name: 'dashboard', component: () => import('@/views/dashboard/DashboardView.vue'), meta: { title: '管理员运营看板' } },
        { path: 'assets', name: 'assets', component: () => import('@/views/assets/AssetsView.vue'), meta: { title: '资产全景管理' } },
        { path: 'contracts', name: 'contracts', component: () => import('@/views/contracts/ContractsView.vue'), meta: { title: '商业合同管理' } },
        { path: 'billing', name: 'billing', component: () => import('@/views/billing/BillingView.vue'), meta: { title: '智能账单引擎' } },
        { path: 'payments', name: 'payments', component: () => import('@/views/payments/PaymentsView.vue'), meta: { title: '缴费流水与核销' } },
        { path: 'repairs', name: 'repairs', component: () => import('@/views/repairs/RepairsView.vue'), meta: { title: '报修工单调度' } },
        { path: 'complaints', name: 'complaints', component: () => import('@/views/complaints/ComplaintsView.vue'), meta: { title: '投诉建议闭环' } },
        { path: 'announcements', name: 'announcements', component: () => import('@/views/announcements/AnnouncementsView.vue'), meta: { title: '定向公告发布' } },
        { path: 'roles', name: 'roles', component: () => import('@/views/roles/RolesView.vue'), meta: { title: '角色权限控制' } },
        { path: 'reports', name: 'reports', component: () => import('@/views/reports/ReportsView.vue'), meta: { title: '报表导出与导入' } },
      ],
    },
  ],
  scrollBehavior() {
    return { top: 0 }
  },
})

router.beforeEach((to) => {
  document.title = `${String(to.meta.title ?? '智慧物业管理系统')} | WisdomPM`
})

export default router
