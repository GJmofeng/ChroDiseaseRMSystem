<template>
  <div class="home">
    <!-- 统计卡片区域 -->
    <div class="stat-cards">
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div>
            <h3>今日申请数</h3>
            <div class="stat-value">156</div>
          </div>
          <el-icon class="stat-icon"><Document /></el-icon>
        </div>
      </el-card>
      
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div>
            <h3>待审核</h3>
            <div class="stat-value">45</div>
          </div>
          <el-icon class="stat-icon"><Clock /></el-icon>
        </div>
      </el-card>
      
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div>
            <h3>本月报销金额</h3>
            <div class="stat-value">¥89,650</div>
          </div>
          <el-icon class="stat-icon"><Money /></el-icon>
        </div>
      </el-card>
      
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div>
            <h3>累计报销人数</h3>
            <div class="stat-value">1,234</div>
          </div>
          <el-icon class="stat-icon"><User /></el-icon>
        </div>
      </el-card>
    </div>

    <!-- 最近活动和快捷操作 -->
    <div class="content-grid">
      <!-- 最近活动 -->
      <el-card class="activity-card" shadow="hover">
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
      <el-card class="quick-actions-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span>快捷操作</span>
          </div>
        </template>
        <div class="quick-actions-grid">
          <el-button type="primary" class="action-button">
            <el-icon><Plus /></el-icon>
            <span>新建申请</span>
          </el-button>
          <el-button type="success" class="action-button">
            <el-icon><Check /></el-icon>
            <span>批量审核</span>
          </el-button>
          <el-button type="danger" class="action-button">
            <el-icon><Download /></el-icon>
            <span>导出报表</span>
          </el-button>
          <el-button type="info" class="action-button">
            <el-icon><Setting /></el-icon>
            <span>系统设置</span>
          </el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import {
  Document,
  Clock,
  Money,
  User,
  Plus,
  Check,
  Download,
  Setting
} from '@element-plus/icons-vue'
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
}

.stat-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-value {
  font-size: 28px;
  margin-top: 8px;
}

.stat-icon {
  font-size: 24px;
  opacity: 0.8;
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
}

.activity-item:last-child {
  padding-bottom: 0;
  border-bottom: none;
}

.activity-title {
  font-weight: 500;
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
}
</style> 