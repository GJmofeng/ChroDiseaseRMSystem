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
        <div class="menu-item" :class="{ active: isActive('/main/user-manage') }" @click="router.push('/main/user-manage')">
          <i class="fas fa-users"></i>
          <span v-show="!isCollapsed">用户管理</span>
        </div>
        <div class="menu-item">
          <i class="fas fa-shield-alt"></i>
          <span v-show="!isCollapsed">权限管理</span>
        </div>
        <div class="menu-item" :class="{ active: isActive('/main/region-manage') }" @click="router.push('/main/region-manage')">
          <i class="fas fa-map-marked-alt"></i>
          <span v-show="!isCollapsed">行政区管理</span>
        </div>
        <div class="menu-item" :class="{ active: isActive('/main/{
        path: 'DiseaseInfo',
        name: 'DiseaseInfo',
        component: DiseaseInfo
      }') }" @click="router.push('/main/disease-info')">
          <i class="fas fa-bell"></i>
          <span v-show="!isCollapsed">慢病信息管理</span>
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
        <div class="menu-item" :class="{ active: isActive('/main/participation') }" @click="router.push('/main/participation')">
          <i class="fas fa-user-plus"></i>
          <span v-show="!isCollapsed">参合信息录入</span>
        </div>
        <div class="menu-item" :class="{ active: isActive('/main/chronic') }" @click="router.push('/main/chronic-cert')">
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
import { ref, reactive, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
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

const isActive = (path) => {
  return route.path === path
}
</script>

<style scoped>
.sidebar {
  width: 240px;
  padding: 16px 0;
  flex-shrink: 0;
  transition: all 0.3s ease;
  position: relative;
  background: #f0f2f5;
  box-shadow: 
    8px 8px 16px rgba(0, 0, 0, 0.1),
    -8px -8px 16px rgba(255, 255, 255, 0.8);
  border-radius: 12px;
  overflow: hidden;
}

.sidebar::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(
    125deg,
    rgba(255,255,255,0.3) 0%,
    rgba(255,255,255,0.1) 40%,
    transparent 60%
  );
  pointer-events: none;
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
  transition: all 0.3s ease;
}

.sidebar-logo i {
  font-size: 24px;
  color: #4c84ff;
  background: linear-gradient(135deg, #4c84ff 0%, #2d5cfe 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  filter: drop-shadow(0 2px 4px rgba(76, 132, 255, 0.2));
}

.menu-section {
  margin-bottom: 8px;
  padding: 0 8px;
}

.menu-section-title {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #1a1a1a;
  font-weight: 500;
  border-radius: 8px;
  background: #f0f2f5;
  box-shadow: 
    inset 2px 2px 4px rgba(0, 0, 0, 0.1),
    inset -2px -2px 4px rgba(255, 255, 255, 0.8);
}

.menu-section-title:hover {
  background: #e6e9ef;
  box-shadow: 
    inset 3px 3px 6px rgba(0, 0, 0, 0.1),
    inset -3px -3px 6px rgba(255, 255, 255, 0.8);
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
  margin-top: 8px;
}

.menu-group.collapsed {
  display: none;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 10px 16px 10px 40px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #666;
  position: relative;
  margin: 4px 0;
  border-radius: 8px;
  background: #f0f2f5;
  box-shadow: 
    inset 1px 1px 2px rgba(0, 0, 0, 0.1),
    inset -1px -1px 2px rgba(255, 255, 255, 0.8);
}

.menu-item:hover {
  background: #e6e9ef;
  box-shadow: 
    inset 2px 2px 4px rgba(0, 0, 0, 0.1),
    inset -2px -2px 4px rgba(255, 255, 255, 0.8);
  color: #1a1a1a;
}

.menu-item i {
  margin-right: 8px;
  font-size: 16px;
  width: 16px;
  text-align: center;
  color: #4c84ff;
}

.menu-item .badge {
  position: absolute;
  right: 16px;
  background: linear-gradient(135deg, #ff4d4f 0%, #ff7875 100%);
  color: white;
  padding: 2px 6px;
  border-radius: 10px;
  font-size: 12px;
  box-shadow: 0 2px 4px rgba(255, 77, 79, 0.2);
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
  transition: all 0.3s ease;
  border: none;
  color: #666;
  background: #f0f2f5;
  box-shadow: 
    2px 2px 4px rgba(0, 0, 0, 0.1),
    -2px -2px 4px rgba(255, 255, 255, 0.8);
}

.sidebar-toggle:hover {
  transform: translateX(-50%) scale(1.1);
  color: #4c84ff;
  box-shadow: 
    3px 3px 6px rgba(0, 0, 0, 0.1),
    -3px -3px 6px rgba(255, 255, 255, 0.8);
}

.sidebar-toggle i {
  font-size: 14px;
}

.menu-item.active {
  background: linear-gradient(90deg, #4c84ff 0%, #2d5cfe 100%);
  color: #fff !important;
}

.menu-item.active i {
  color: #fff !important;
}
</style> 