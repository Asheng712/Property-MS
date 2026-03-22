import { computed, ref } from 'vue'
import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', () => {
  const sidebarCollapsed = ref(false)
  const sidebarDrawerOpen = ref(false)
  const currentUser = ref('管理员')

  const sidebarWidth = computed(() => (sidebarCollapsed.value ? '88px' : '236px'))

  function toggleSidebar() {
    sidebarCollapsed.value = !sidebarCollapsed.value
  }

  function openSidebarDrawer() {
    sidebarDrawerOpen.value = true
  }

  function closeSidebarDrawer() {
    sidebarDrawerOpen.value = false
  }

  function setCurrentUser(name: string) {
    currentUser.value = name
  }

  return {
    currentUser,
    sidebarCollapsed,
    sidebarDrawerOpen,
    sidebarWidth,
    toggleSidebar,
    openSidebarDrawer,
    closeSidebarDrawer,
    setCurrentUser,
  }
})
