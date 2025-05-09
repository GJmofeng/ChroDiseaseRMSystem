<template>
  <div class="top-navbar bg-white shadow-sm">
    <div class="container mx-auto px-4 py-4">
      <div class="flex justify-between items-center">
        <div class="flex items-center space-x-3">
          <div class="logo-container relative">
            <svg class="w-8 h-8 text-blue-600 animate-pulse" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 2L2 7L12 12L22 7L12 2Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <path d="M2 17L12 22L22 17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <path d="M2 12L12 17L22 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <circle cx="12" cy="12" r="3" fill="currentColor"/>
            </svg>
            <div class="absolute inset-0 bg-blue-600 opacity-20 rounded-full animate-ping"></div>
          </div>
          <div class="text-2xl font-bold text-black">慢性病报销系统</div>
        </div>
        
        <!-- 用户信息下拉菜单 -->
        <div class="relative">
          <div class="flex items-center space-x-2 cursor-pointer" @click.stop="toggleDropdown">
            <span class="text-gray-700">{{ username }}</span>
            <i class="fas fa-chevron-down text-gray-500 text-sm transition-transform" :class="{ 'transform rotate-180': isDropdownOpen }"></i>
          </div>
          <!-- 下拉菜单 -->
          <div v-show="isDropdownOpen" class="absolute right-0 mt-2 w-32 bg-white rounded-lg shadow-lg py-2 z-50">
            <button @click.stop="handleLogout" class="w-full text-left px-4 py-2 text-gray-700 hover:bg-blue-50 hover:text-blue-600 transition">
              <i class="fas fa-sign-out-alt mr-2"></i>退出登录
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref('')
const isDropdownOpen = ref(false)

// 切换下拉菜单
const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value
}

// 点击页面其他地方关闭下拉菜单
const handleClickOutside = (event) => {
  if (isDropdownOpen.value) {
    isDropdownOpen.value = false
  }
}

// 检查登录状态
const checkLoginStatus = () => {
  const userInfo = localStorage.getItem('userInfo')
  if (userInfo) {
    const { username: storedUsername } = JSON.parse(userInfo)
    username.value = storedUsername
  }
}

// 处理退出登录
const handleLogout = () => {
  localStorage.removeItem('userInfo')
  isDropdownOpen.value = false
  router.push('/')
}

onMounted(() => {
  checkLoginStatus()
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
.top-navbar {
  position: sticky;
  top: 0;
  z-index: 50;
}

.logo-container {
  position: relative;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-container::before {
  content: '';
  position: absolute;
  width: 100%;
  height: 100%;
  border: 2px solid #2563EB;
  border-radius: 50%;
  animation: rotate 4s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style> 