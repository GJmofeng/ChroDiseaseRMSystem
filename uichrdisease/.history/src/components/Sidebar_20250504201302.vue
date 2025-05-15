<template>
  <div class="sidebar neumorphic" :class="{ collapsed: isCollapsed }">
    <div class="sidebar-logo">
      <i class="fas fa-heartbeat"></i>
      <span v-show="!isCollapsed">慢性病报销系统</span>
    </div>
    
    <!-- 系统管理 -->
    <div class="menu-section">
      <div class="menu-section-title" @click="toggleMenuGroup('system')">
        <i class="fas fa-cog menu-arrow" :class="{ 'collapsed': !menuStates.system }"></i>
        <span v-show="!isCollapsed">系统管理</span>
      </div>
      <div class="menu-group" :class="{ collapsed: !menuStates.system }">
        <div class="menu-item">
          <i class="fas fa-users"></i>
          <span v-show="!isCollapsed">用户管理</span>
        </div>
        <div class="menu-item">
          <i class="fas fa-shield-alt"></i>
          <span v-show="!isCollapsed">权限管理</span>
        </div>
        <div class="menu-item">
          <i class="fas fa-map-marked-alt"></i>
          <span v-show="!isCollapsed">行政区管理</span>
        </div>
        <div class="menu-item">
          <i class="fas fa-bell"></i>
          <span v-show="!isCollapsed">慢病消息管理</span>
          <span class="badge" v-show="!isCollapsed">20</span>
        </div>
        <div class="menu-item">
          <i class="fas fa-file-medical"></i>
          <span v-show="!isCollapsed">慢病政策管理</span>
        </div>
      </div>
    </div>

    <!-- 业务管理 -->
    <div class="menu-section">
      <div class="menu-section-title" @click="toggleMenuGroup('business')">
        <i class="fas fa-briefcase menu-arrow" :class="{ 'collapsed': !menuStates.business }"></i>
        <span v-show="!isCollapsed">业务管理</span>
      </div>
      <div class="menu-group" :class="{ collapsed: !menuStates.business }">
        <div class="menu-item">
          <i class="fas fa-user-plus"></i>
          <span v-show="!isCollapsed">参合信息录入</span>
        </div>
        <div class="menu-item">
          <i class="fas fa-id-card"></i>
          <span v-show="!isCollapsed">慢性病证管理</span>
        </div>
        <div class="menu-item">
          <i class="fas fa-money-check-alt"></i>
          <span v-show="!isCollapsed">参合缴费登记</span>
        </div>
        <div class="menu-item">
          <i class="fas fa-file-invoice-dollar"></i>
          <span v-show="!isCollapsed">报销申请</span>
          <span class="badge" v-show="!isCollapsed">15</span>
        </div>
        <div class="menu-item">
          <i class="fas fa-clipboard-check"></i>
          <span v-show="!isCollapsed">报销审核</span>
        </div>
        <div class="menu-item">
          <i class="fas fa-money-bill-wave"></i>
          <span v-show="!isCollapsed">报销汇款</span>
        </div>
      </div>
    </div>

    <!-- 统计分析 -->
    <div class="menu-section">
      <div class="menu-section-title" @click="toggleMenuGroup('statistics')">
        <i class="fas fa-chart-line menu-arrow" :class="{ 'collapsed': !menuStates.statistics }"></i>
        <span v-show="!isCollapsed">统计分析</span>
      </div>
      <div class="menu-group" :class="{ collapsed: !menuStates.statistics }">
        <div class="menu-item">
          <i class="fas fa-chart-bar"></i>
          <span v-show="!isCollapsed">按年份统计</span>
        </div>
        <div class="menu-item">
          <i class="fas fa-chart-area"></i>
          <span v-show="!isCollapsed">按月份统计</span>
        </div>
      </div>
    </div>

    <!-- 侧边栏切换按钮 -->
    <el-button class="sidebar-toggle btn" @click="toggleSidebar" :title="isCollapsed ? '展开' : '收起'">
      <i class="fas" :class="isCollapsed ? 'fa-chevron-right' : 'fa-chevron-left'"></i>
    </el-button>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'

const isCollapsed = ref(false)
const menuStates = reactive({
  system: true,
  business: true,
  statistics: true
})

const toggleSidebar = () => {
  isCollapsed.value = !isCollapsed.value
}

const toggleMenuGroup = (group) => {
  menuStates[group] = !menuStates[group]
}
</script>

<style scoped>
.sidebar {
  width: 240px;
  padding: 16px 0;
  flex-shrink: 0;
  transition: all 0.3s ease;
  position: relative;
}

.sidebar.collapsed {
  width: 64px;
}

.sidebar-logo {
  padding: 0 16px 16px;
  margin-bottom: 16px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  font-size: 18px;
  font-weight: 600;
  color: #1a1a1a;
  display: flex;
  align-items: center;
  gap: 12px;
}

.sidebar-logo i {
  font-size: 24px;
  color: #4c84ff;
}

.menu-section {
  margin-bottom: 8px;
}

.menu-section-title {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  cursor: pointer;
  color: #1a1a1a;
  font-weight: 500;
}

.menu-arrow {
  margin-right: 8px;
  transition: transform 0.3s ease;
}

.menu-arrow.collapsed {
  transform: rotate(-90deg);
}

.menu-group {
  overflow: hidden;
  transition: all 0.3s ease;
}

.menu-group.collapsed {
  display: none;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 10px 16px 10px 40px;
  cursor: pointer;
  color: #666;
  position: relative;
}

.menu-item i {
  margin-right: 8px;
  font-size: 16px;
  width: 16px;
  text-align: center;
}

.menu-item .badge {
  position: absolute;
  right: 16px;
  background: #ff4d4f;
  color: white;
  padding: 2px 6px;
  border-radius: 10px;
  font-size: 12px;
}

.sidebar-toggle {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border: none;
  color: #666;
  background: #f5f5f5;
}

.sidebar-toggle i {
  font-size: 14px;
}
</style> 