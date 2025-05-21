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
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #f8f9fc;
}

.page-container {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
}
</style> 