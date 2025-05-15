<template>
  <div class="tab-actions" style="position:relative;">
    <i class="fas fa-th grid-icon" @click.stop="handleGridClick"></i>
    <div v-if="menuVisible" class="tab-menu-popup" :style="menuStyle" @click.stop>
      <div class="tab-menu-item" @click="onCloseOther">关闭其他</div>
      <div class="tab-menu-item" @click="onCloseAll">关闭所有</div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onUnmounted } from 'vue'

const props = defineProps({
  onCloseOther: Function,
  onCloseAll: Function
})

const menuVisible = ref(false)
const menuStyle = ref({})

function handleGridClick(e) {
  menuVisible.value = true
  menuStyle.value = {
    position: 'absolute',
    left: '-32px',
    top: '38px',
    zIndex: 9999
  }
}
function handleMenuClose() {
  menuVisible.value = false
}

onUnmounted(() => {
  document.removeEventListener('click', handleMenuClose)
})

watch(menuVisible, (val) => {
  if (val) {
    setTimeout(() => {
      document.addEventListener('click', handleMenuClose)
    }, 0)
  } else {
    document.removeEventListener('click', handleMenuClose)
  }
})
</script>

<style scoped>
.tab-actions {
  margin-left: 16px;
  margin-right: 40px;
  font-size: 18px;
  color: #888;
  cursor: pointer;
  display: flex;
  align-items: center;
  position: relative;
}
.grid-icon {
  font-size: 18px;
}
.tab-menu-popup {
  min-width: 120px;
  background: #fff;
  box-shadow: 0 4px 16px rgba(0,0,0,0.12);
  border-radius: 8px;
  padding: 8px 0;
  position: absolute;
  right: 0;
  top: 38px;
  z-index: 9999;
  user-select: none;
}
.tab-menu-item {
  padding: 8px 20px;
  font-size: 15px;
  color: #222;
  cursor: pointer;
  transition: background 0.2s, color 0.2s;
}
.tab-menu-item:hover {
  background: #f0f7ff;
  color: #2563eb;
}
</style> 