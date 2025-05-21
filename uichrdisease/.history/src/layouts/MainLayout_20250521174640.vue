<template>
  <div class="layout">
    <Sidebar />
    <div class="main-content">
      <TopNavbar />
      <TabNav />
      <div class="page-container">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script setup>
import Sidebar from '../components/Sidebar.vue'
import TopNavbar from '../components/TopNavbar.vue'
import TabNav from '../components/TabNav.vue'
</script>

<style scoped>
.layout {
  display: flex;
  min-height: 100vh;
  background: #f5f6fa;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  padding: 16px 24px;
}

.page-container {
  flex: 1;
  overflow: auto;
  position: relative;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
}

:deep(.tab-nav-container) {
  margin-bottom: 16px;
  background: transparent;
}

:deep(.tab-nav-bar) {
  background: #fff;
  border-radius: 12px;
  padding: 8px 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
}
</style> 