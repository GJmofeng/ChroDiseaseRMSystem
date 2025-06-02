<template>
  <div class="scrollable-container" :class="{ 'mobile': isMobile }">
    <slot></slot>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const isMobile = ref(false)

// 检查是否为移动设备
const checkMobile = () => {
  isMobile.value = window.innerWidth <= 1200
}

// 监听窗口大小变化
onMounted(() => {
  checkMobile()
  window.addEventListener('resize', checkMobile)
})

onUnmounted(() => {
  window.removeEventListener('resize', checkMobile)
})
</script>

<style scoped>
.scrollable-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
  height: 100vh;
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: rgba(144, 147, 153, 0.3) transparent;
}

/* 自定义滚动条样式（Webkit浏览器） */
.scrollable-container::-webkit-scrollbar {
  width: 8px;
}

.scrollable-container::-webkit-scrollbar-track {
  background: transparent;
  border-radius: 4px;
}

.scrollable-container::-webkit-scrollbar-thumb {
  background: rgba(144, 147, 153, 0.3);
  border-radius: 4px;
  transition: all 0.3s ease;
}

.scrollable-container::-webkit-scrollbar-thumb:hover {
  background: rgba(144, 147, 153, 0.5);
}

/* 移动设备适配 */
.scrollable-container.mobile {
  height: auto;
  overflow-y: visible;
}
</style> 