<template>
  <div class="top-navbar">
    <div class="left">
      <h2>系统首页</h2>
    </div>
    <div class="right">
      <el-badge :value="3" class="notification-badge">
        <el-icon><Bell /></el-icon>
      </el-badge>
      
      <el-dropdown trigger="click" @command="handleCommand">
        <div class="user-dropdown">
          <el-icon><User /></el-icon>
          <span>管理员</span>
          <el-icon><ArrowDown /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">
              <el-icon><User /></el-icon>
              <span>个人信息</span>
            </el-dropdown-item>
            <el-dropdown-item divided command="logout">
              <el-icon><SwitchButton /></el-icon>
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { Bell, User, ArrowDown, SwitchButton } from '@element-plus/icons-vue'

const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      // 处理查看个人信息
      break
    case 'logout':
      // 处理退出登录
      break
  }
}
</script>

<style scoped>
.top-navbar {
  background: white;
  padding: 16px 24px;
  margin-bottom: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.notification-badge {
  cursor: pointer;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.user-dropdown:hover {
  background: rgba(76, 132, 255, 0.05);
}

:deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style> 