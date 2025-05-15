<template>
  <div class="home">
    <div class="tab-nav-bar">
      <draggable v-model="tags" class="tab-list" item-key="path" :animation="200" :ghost-class="'tab-ghost'">
        <template #item="{element: tag, index}">
          <div class="tab-item" :class="{active: activePath === tag.path}" @click="handleTabClick(tag)">
            <span class="tab-icon"><i :class="['fas', tag.icon]"></i></span>
            {{ tag.title }}
            <span v-if="tag.closable" class="tab-close" @click.stop="handleTabClose(tag, index)"><i class="fas fa-times"></i></span>
          </div>
        </template>
      </draggable>
      <TabMenu :onCloseOther="handleMenuCloseOther" :onCloseAll="handleMenuCloseAll" />
    </div>
    <!-- 统计卡片区域 -->
    <div class="stat-cards">
      <el-card class="stat-card card-texture" shadow="hover">
        <div class="stat-content">
          <div>
            <h3>今日申请数</h3>
            <div class="stat-value">156</div>
          </div>
          <i class="fas fa-file-alt stat-icon"></i>
        </div>
      </el-card>
      
      <el-card class="stat-card card-texture" shadow="hover">
        <div class="stat-content">
          <div>
            <h3>待审核</h3>
            <div class="stat-value">45</div>
          </div>
          <i class="fas fa-clock stat-icon"></i>
        </div>
      </el-card>
      
      <el-card class="stat-card card-texture" shadow="hover">
        <div class="stat-content">
          <div>
            <h3>本月报销金额</h3>
            <div class="stat-value">¥89,650</div>
          </div>
          <i class="fas fa-money-bill-wave stat-icon"></i>
        </div>
      </el-card>
      
      <el-card class="stat-card card-texture" shadow="hover">
        <div class="stat-content">
          <div>
            <h3>累计报销人数</h3>
            <div class="stat-value">1,234</div>
          </div>
          <i class="fas fa-users stat-icon"></i>
        </div>
      </el-card>
    </div>

    <!-- 最近活动和快捷操作 -->
    <div class="content-grid">
      <!-- 最近活动 -->
      <el-card class="activity-card neumorphic" shadow="hover">
        <template #header>
          <div class="card-header">
            <span>最近活动</span>
          </div>
        </template>
        <div class="activity-list">
          <div class="activity-item">
            <div class="activity-info">
              <div class="activity-title">张三提交了报销申请</div>
              <div class="activity-time">2024-01-20 14:30</div>
            </div>
            <el-tag type="warning">待审核</el-tag>
          </div>
          <div class="activity-item">
            <div class="activity-info">
              <div class="activity-title">李四的报销申请已通过</div>
              <div class="activity-time">2024-01-20 13:15</div>
            </div>
            <el-tag type="success">已通过</el-tag>
          </div>
        </div>
      </el-card>

      <!-- 快捷操作 -->
      <el-card class="quick-actions-card neumorphic" shadow="hover">
        <template #header>
          <div class="card-header">
            <span>快捷操作</span>
          </div>
        </template>
        <div class="quick-actions-grid">
          <el-button type="primary" class="action-button btn">
            <i class="fas fa-plus"></i>
            <span>新建申请</span>
          </el-button>
          <el-button type="success" class="action-button btn">
            <i class="fas fa-check"></i>
            <span>批量审核</span>
          </el-button>
          <el-button type="danger" class="action-button btn">
            <i class="fas fa-download"></i>
            <span>导出报表</span>
          </el-button>
          <el-button type="info" class="action-button btn">
            <i class="fas fa-cog"></i>
            <span>系统设置</span>
          </el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import draggable from 'vuedraggable'
import TabMenu from '../components/TabMenu.vue'

const router = useRouter()

const tags = ref([
  { title: '首页', path: '/main', icon: 'fa-home', closable: false },
  { title: '用户管理', path: '/main/user-manage', icon: 'fa-users', closable: true }
])
const activePath = ref('/main')

function handleTabClick(tag) {
  activePath.value = tag.path
  router.push(tag.path)
}
function handleTabClose(tag, idx) {
  tags.value.splice(idx, 1)
  if (activePath.value === tag.path) {
    activePath.value = '/main'
    router.push('/main')
  }
}
function handleTabAdd() {
  tags.value.push({ title: '新标签', path: '/main/new', icon: 'fa-star', closable: true })
  activePath.value = '/main/new'
  router.push('/main/new')
}
function handleMenuCloseOther() {
  tags.value = tags.value.filter(tag => !tag.closable || tag.path === activePath.value)
}
function handleMenuCloseAll() {
  tags.value = tags.value.filter(tag => !tag.closable)
  activePath.value = '/main'
  router.push('/main')
}
function handleMenuClose() {
  menuVisible.value = false
}
document.addEventListener('click', handleMenuClose)
</script>

<style scoped>
.home {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.stat-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.stat-card {
  border: none;
  transform-style: preserve-3d;
  perspective: 1000px;
  transition: all 0.3s ease;
  overflow: hidden;
  border-radius: 16px;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.stat-card:nth-child(1) {
  background: linear-gradient(135deg, #4c84ff 0%, #2d5cfe 100%);
}

.stat-card:nth-child(2) {
  background: linear-gradient(135deg, #FF6B6B 0%, #FF8E53 100%);
}

.stat-card:nth-child(3) {
  background: linear-gradient(135deg, #00C9A7 0%, #00B4A7 100%);
}

.stat-card:nth-child(4) {
  background: linear-gradient(135deg, #9B51E0 0%, #6A3D9E 100%);
}

.stat-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  z-index: 1;
  padding: 24px;
  color: white;
  border-radius: 16px;
}

.stat-content::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(
    125deg,
    rgba(255,255,255,0.2) 0%,
    rgba(255,255,255,0.1) 40%,
    transparent 60%
  );
  transform: translateZ(20px);
  transition: all 0.3s ease;
}

.stat-card:hover .stat-content::before {
  transform: translateZ(30px) rotate(5deg);
  opacity: 0.8;
}

.stat-value {
  font-size: 28px;
  margin-top: 8px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  font-weight: bold;
}

.stat-icon {
  font-size: 24px;
  opacity: 0.8;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
}

.content-grid {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 24px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  color: #1a1a1a;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.activity-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 16px;
  border-bottom: 1px solid #eef0f5;
  transition: all 0.3s ease;
}

.activity-item:hover {
  transform: translateX(5px);
}

.activity-item:last-child {
  padding-bottom: 0;
  border-bottom: none;
}

.activity-title {
  font-weight: 500;
  color: #1a1a1a;
}

.activity-time {
  color: #666;
  font-size: 12px;
  margin-top: 4px;
}

.quick-actions-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.action-button {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  height: 40px;
  transition: all 0.3s ease;
  border: none;
  color: white;
  font-weight: 500;
  position: relative;
  overflow: hidden;
  border-radius: 12px;
}

.action-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(
    125deg,
    rgba(255,255,255,0.2) 0%,
    rgba(255,255,255,0.1) 40%,
    transparent 60%
  );
  transform: translateZ(20px);
  transition: all 0.3s ease;
  border-radius: 12px;
}

.action-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.action-button:hover::before {
  transform: translateZ(30px) rotate(5deg);
  opacity: 0.8;
}

.action-button:nth-child(1) {
  background: linear-gradient(135deg, #4c84ff 0%, #2d5cfe 100%);
}

.action-button:nth-child(2) {
  background: linear-gradient(135deg, #00C9A7 0%, #00B4A7 100%);
}

.action-button:nth-child(3) {
  background: linear-gradient(135deg, #FF6B6B 0%, #FF8E53 100%);
}

.action-button:nth-child(4) {
  background: linear-gradient(135deg, #9B51E0 0%, #6A3D9E 100%);
}

.action-button i {
  font-size: 16px;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
}

:deep(.el-card) {
  border: none;
  transition: all 0.3s ease;
}

:deep(.el-card:hover) {
  transform: translateY(-2px);
}

:deep(.el-tag) {
  border: none;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.tab-nav-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  padding: 0 0 8px 0;
  border-radius: 10px 10px 0 0;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 18px;
}
.tab-list {
  display: flex;
  align-items: flex-end;
  gap: 2px;
}
.tab-item {
  display: flex;
  align-items: center;
  background: #f7f7f7;
  border-radius: 10px 10px 0 0;
  padding: 0 18px 0 14px;
  height: 38px;
  font-size: 15px;
  color: #222;
  cursor: pointer;
  position: relative;
  margin-right: 2px;
  transition: background 0.2s, color 0.2s;
  user-select: none;
}
.tab-item.active {
  background: #fff;
  color: #2563eb;
  font-weight: 600;
  border-bottom: 2.5px solid #2563eb;
  box-shadow: 0 2px 8px rgba(76,132,255,0.04);
}
.tab-item:not(.active):hover {
  background: #f0f7ff;
  color: #2563eb;
}
.tab-icon {
  margin-right: 6px;
  font-size: 16px;
  display: flex;
  align-items: center;
}
.tab-close {
  margin-left: 10px;
  color: #bbb;
  font-size: 13px;
  cursor: pointer;
  transition: color 0.2s;
}
.tab-close:hover {
  color: #ff4d4f;
}
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
.tab-ghost {
  opacity: 0.5;
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