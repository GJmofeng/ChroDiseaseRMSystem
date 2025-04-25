<template>
  <div class="home">
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
// 移除不需要的 Element Plus 图标导入
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
  background: linear-gradient(135deg, #4c84ff 0%, #2d5cfe 100%);
  color: white;
  border: none;
  transform-style: preserve-3d;
  perspective: 1000px;
}

.stat-card:hover {
  animation: borderGlow 2s infinite;
}

.stat-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  z-index: 1;
}

.stat-value {
  font-size: 28px;
  margin-top: 8px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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
}

.action-button:hover {
  transform: translateY(-2px);
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
</style> 