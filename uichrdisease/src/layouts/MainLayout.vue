<template>
  <div class="layout">
    <Sidebar />
    <div class="main-content">
      <TopNavbar />
      <router-view></router-view>
    </div>
  </div>
</template>

<script setup>
import Sidebar from '../components/Sidebar.vue'
import TopNavbar from '../components/TopNavbar.vue'
</script>

<style scoped>
.layout {
  display: flex;
  min-height: 100vh;
}

.main-content {
  flex: 1;
  padding: 24px;
  background-color: #f8f9fc;
}
</style> 