import { createRouter, createWebHashHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/',
      redirect: '/home',
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/auth/LoginView.vue'),
      meta: { title: '登录' },
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/auth/RegisterView.vue'),
      meta: { title: '注册' },
    },
    {
      path: '/home',
      name: 'home',
      component: () => import('@/views/home/HomeView.vue'),
      meta: { title: '首页', tab: 0 },
    },
    {
      path: '/bills',
      name: 'bills',
      component: () => import('@/views/bills/BillListView.vue'),
      meta: { title: '我的账单', tab: 1 },
    },
    {
      path: '/bills/pay/:id',
      name: 'bill-pay',
      component: () => import('@/views/bills/PaymentView.vue'),
      meta: { title: '确认缴费' },
    },
    {
      path: '/services',
      name: 'services',
      component: () => import('@/views/home/ServiceHubView.vue'),
      meta: { title: '服务', tab: 2 },
    },
    {
      path: '/repairs',
      name: 'repairs',
      component: () => import('@/views/repairs/RepairListView.vue'),
      meta: { title: '我的报修' },
    },
    {
      path: '/repairs/create',
      name: 'repair-create',
      component: () => import('@/views/repairs/RepairCreateView.vue'),
      meta: { title: '提交报修' },
    },
    {
      path: '/repairs/:id',
      name: 'repair-detail',
      component: () => import('@/views/repairs/RepairDetailView.vue'),
      meta: { title: '报修详情' },
    },
    {
      path: '/complaints',
      name: 'complaints',
      component: () => import('@/views/complaints/ComplaintListView.vue'),
      meta: { title: '我的投诉' },
    },
    {
      path: '/complaints/create',
      name: 'complaint-create',
      component: () => import('@/views/complaints/ComplaintCreateView.vue'),
      meta: { title: '提交投诉' },
    },
    {
      path: '/complaints/:id',
      name: 'complaint-detail',
      component: () => import('@/views/complaints/ComplaintDetailView.vue'),
      meta: { title: '投诉详情' },
    },
    {
      path: '/notices',
      name: 'notices',
      component: () => import('@/views/notices/NoticeListView.vue'),
      meta: { title: '社区公告', tab: 3 },
    },
    {
      path: '/notices/:id',
      name: 'notice-detail',
      component: () => import('@/views/notices/NoticeDetailView.vue'),
      meta: { title: '公告详情' },
    },
    {
      path: '/contracts',
      name: 'contracts',
      component: () => import('@/views/contracts/ContractListView.vue'),
      meta: { title: '我的合同' },
    },
    {
      path: '/contracts/:id',
      name: 'contract-detail',
      component: () => import('@/views/contracts/ContractDetailView.vue'),
      meta: { title: '合同详情' },
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('@/views/profile/ProfileView.vue'),
      meta: { title: '个人中心', tab: 4 },
    },
    {
      path: '/profile/edit',
      name: 'profile-edit',
      component: () => import('@/views/profile/ProfileEditView.vue'),
      meta: { title: '编辑资料' },
    },
  ],
})

router.beforeEach((to, _from) => {
  const userStore = useUserStore()
  const publicPages = ['/login', '/register']

  if (!userStore.isLoggedIn && !publicPages.includes(to.path)) {
    return '/login'
  }

  if (userStore.isLoggedIn && publicPages.includes(to.path)) {
    return '/home'
  }

  document.title = `${String(to.meta.title ?? '智慧物业')} | 智慧物业`
})

export default router
