<template>
  <div class="top-navbar neumorphic">
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
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'

const router = useRouter()

const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      // 处理查看个人信息
      break
    case 'logout':
      // 处理退出登录
      ElMessageBox.confirm(
        '确定要退出登录吗？',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        // TODO: 清除用户登录状态
        // 跳转到登录页
        router.push('/login')
      }).catch(() => {
        // 用户取消退出
      })
      break
  }
}
</script>

<style scoped>
.top-navbar {
  padding: 16px 24px;
  margin-bottom: 24px;
  border-radius: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  overflow: hidden;
}

.top-navbar::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(
    125deg,
    rgba(255,255,255,0.3) 0%,
    rgba(255,255,255,0.1) 40%,
    transparent 60%
  );
  transform: translateZ(20px);
  transition: all 0.3s ease;
}

.top-navbar:hover::before {
  transform: translateZ(30px) rotate(5deg);
  opacity: 0.8;
}

.left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.left h2 {
  margin: 0;
  color: #1a1a1a;
  font-weight: 600;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.notification-badge {
  cursor: pointer;
  transition: all 0.3s ease;
}

.notification-badge:hover {
  transform: scale(1.1);
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: all 0.3s ease;
  background: linear-gradient(145deg, rgba(255,255,255,0.1), rgba(255,255,255,0.05));
  box-shadow: 
    2px 2px 4px rgba(0,0,0,0.1),
    -2px -2px 4px rgba(255,255,255,0.1);
}

.user-dropdown:hover {
  background: rgba(76, 132, 255, 0.05);
  transform: translateY(-1px);
}

:deep(.el-dropdown-menu) {
  background: linear-gradient(145deg, var(--surface-color), #f5f7fa);
  box-shadow: 
    8px 8px 16px var(--shadow-color),
    -8px -8px 16px var(--highlight-color);
  border: none;
  border-radius: 8px;
  padding: 4px;
}

:deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border-radius: 4px;
  transition: all 0.3s ease;
}

:deep(.el-dropdown-menu__item:hover) {
  background: rgba(76, 132, 255, 0.05);
  color: #4c84ff;
}

:deep(.el-dropdown-menu__item--divided) {
  border-top: 1px solid rgba(0, 0, 0, 0.05);
  margin: 4px 0;
}
</style> 