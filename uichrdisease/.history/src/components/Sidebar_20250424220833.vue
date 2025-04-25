<template>
  <div class="sidebar" :class="{ collapsed: isCollapsed }">
    <div class="sidebar-logo">
      <el-icon><House /></el-icon>
      <span v-show="!isCollapsed">慢性病报销系统</span>
    </div>
    
    <!-- 系统管理 -->
    <div class="menu-section">
      <div class="menu-section-title" @click="toggleMenuGroup('system')">
        <el-icon><ArrowDown :class="{ 'menu-arrow': true, 'collapsed': !menuStates.system }" /></el-icon>
        <span v-show="!isCollapsed">系统管理</span>
      </div>
      <div class="menu-group" :class="{ collapsed: !menuStates.system }">
        <div class="menu-item">
          <el-icon><User /></el-icon>
          <span v-show="!isCollapsed">用户管理</span>
        </div>
        <div class="menu-item">
          <el-icon><Lock /></el-icon>
          <span v-show="!isCollapsed">权限管理</span>
        </div>
        <div class="menu-item">
          <el-icon><Location /></el-icon>
          <span v-show="!isCollapsed">行政区管理</span>
        </div>
        <div class="menu-item">
          <el-icon><Bell /></el-icon>
          <span v-show="!isCollapsed">慢病消息管理</span>
          <span class="badge" v-show="!isCollapsed">20</span>
        </div>
        <div class="menu-item">
          <el-icon><Document /></el-icon>
          <span v-show="!isCollapsed">慢病政策管理</span>
        </div>
      </div>
    </div>

    <!-- 业务管理 -->
    <div class="menu-section">
      <div class="menu-section-title" @click="toggleMenuGroup('business')">
        <el-icon><ArrowDown :class="{ 'menu-arrow': true, 'collapsed': !menuStates.business }" /></el-icon>
        <span v-show="!isCollapsed">业务管理</span>
      </div>
      <div class="menu-group" :class="{ collapsed: !menuStates.business }">
        <div class="menu-item">
          <el-icon><UserPlus /></el-icon>
          <span v-show="!isCollapsed">参合信息录入</span>
        </div>
        <div class="menu-item">
          <el-icon><IdCard /></el-icon>
          <span v-show="!isCollapsed">慢性病证管理</span>
        </div>
        <div class="menu-item">
          <el-icon><Money /></el-icon>
          <span v-show="!isCollapsed">参合缴费登记</span>
        </div>
        <div class="menu-item active">
          <el-icon><Document /></el-icon>
          <span v-show="!isCollapsed">报销申请</span>
          <span class="badge" v-show="!isCollapsed">15</span>
        </div>
        <div class="menu-item">
          <el-icon><List /></el-icon>
          <span v-show="!isCollapsed">报销审核</span>
        </div>
        <div class="menu-item">
          <el-icon><Switch /></el-icon>
          <span v-show="!isCollapsed">报销汇款</span>
        </div>
      </div>
    </div>

    <!-- 统计分析 -->
    <div class="menu-section">
      <div class="menu-section-title" @click="toggleMenuGroup('statistics')">
        <el-icon><ArrowDown :class="{ 'menu-arrow': true, 'collapsed': !menuStates.statistics }" /></el-icon>
        <span v-show="!isCollapsed">统计分析</span>
      </div>
      <div class="menu-group" :class="{ collapsed: !menuStates.statistics }">
        <div class="menu-item">
          <el-icon><Histogram /></el-icon>
          <span v-show="!isCollapsed">按年份统计</span>
        </div>
        <div class="menu-item">
          <el-icon><TrendCharts /></el-icon>
          <span v-show="!isCollapsed">按月份统计</span>
        </div>
      </div>
    </div>

    <!-- 侧边栏切换按钮 -->
    <el-button class="sidebar-toggle" @click="toggleSidebar" :title="isCollapsed ? '展开' : '收起'">
      <el-icon><ArrowLeft v-if="!isCollapsed" /><ArrowRight v-else /></el-icon>
    </el-button>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import {
  House,
  User,
  Lock,
  Location,
  Bell,
  Document,
  UserPlus,
  IdCard,
  Money,
  List,
  Switch,
  Histogram,
  TrendCharts,
  ArrowDown,
  ArrowLeft,
  ArrowRight
} from '@element-plus/icons-vue'

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
  background: white;
  border-right: 1px solid #eef0f5;
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
  border-bottom: 1px solid #eef0f5;
  font-size: 18px;
  font-weight: 600;
  color: #1a1a1a;
  display: flex;
  align-items: center;
  gap: 12px;
  transition: all 0.3s ease;
}

.menu-section {
  margin-bottom: 16px;
}

.menu-section-title {
  padding: 12px 16px;
  margin-bottom: 0;
  color: #666;
  font-size: 13px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  border-radius: 6px;
  margin: 0 8px;
}

.menu-section-title:hover {
  background: rgba(76, 132, 255, 0.05);
  color: #4c84ff;
}

.menu-arrow {
  transition: transform 0.3s ease;
}

.menu-arrow.collapsed {
  transform: rotate(-90deg);
}

.menu-group {
  overflow: hidden;
  transition: max-height 0.3s ease;
  max-height: 500px;
}

.menu-group.collapsed {
  max-height: 0;
}

.menu-item {
  padding: 12px 16px;
  color: #666;
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  margin: 4px 8px;
  border-radius: 6px;
}

.menu-item:hover {
  background: rgba(76, 132, 255, 0.05);
  color: #4c84ff;
}

.menu-item.active {
  background: #4c84ff;
  color: white;
}

.menu-item .badge {
  position: absolute;
  right: 16px;
  background: #ff4d4f;
  color: white;
  padding: 2px 6px;
  border-radius: 10px;
  font-size: 12px;
  font-weight: 500;
}

.sidebar-toggle {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
  color: #666;
}

.sidebar-toggle:hover {
  background: #e6e6e6;
}
</style> 