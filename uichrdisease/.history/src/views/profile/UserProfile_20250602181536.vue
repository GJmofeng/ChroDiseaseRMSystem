<template>
  <div class="user-profile">
    <!-- 顶部用户信息卡片 -->
    <el-card class="profile-header">
      <div class="user-info">
        <div class="avatar-container">
          <el-avatar :size="80" :src="userInfo.avatar" />
          <div class="user-status" :class="{ online: userInfo.online }"></div>
        </div>
        <div class="user-details">
          <h2>{{ userInfo.name }}</h2>
          <p class="user-role">{{ userInfo.role }}</p>
          <div class="user-stats">
            <div class="stat-item">
              <span class="stat-value">{{ userInfo.taskCount }}</span>
              <span class="stat-label">待办任务</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ userInfo.meetingCount }}</span>
              <span class="stat-label">今日会议</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ userInfo.projectCount }}</span>
              <span class="stat-label">进行中项目</span>
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 主要内容区域 -->
    <div class="profile-content">
      <!-- 左侧列 -->
      <div class="left-column">
        <!-- 日程卡片 -->
        <el-card class="schedule-card">
          <template #header>
            <div class="card-header">
              <h3>今日日程</h3>
              <el-button type="primary" text>查看全部</el-button>
            </div>
          </template>
          <div class="schedule-list">
            <div v-for="schedule in schedules" :key="schedule.id" class="schedule-item">
              <div class="schedule-time">{{ schedule.time }}</div>
              <div class="schedule-content">
                <h4>{{ schedule.title }}</h4>
                <p>{{ schedule.description }}</p>
                <div class="schedule-participants">
                  <el-avatar 
                    v-for="participant in schedule.participants" 
                    :key="participant.id"
                    :size="24"
                    :src="participant.avatar"
                  />
                </div>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 任务卡片 -->
        <el-card class="task-card">
          <template #header>
            <div class="card-header">
              <h3>我的任务</h3>
              <el-button type="primary" text>新建任务</el-button>
            </div>
          </template>
          <div class="task-list">
            <div v-for="task in tasks" :key="task.id" class="task-item">
              <el-checkbox v-model="task.completed" />
              <div class="task-content">
                <h4>{{ task.title }}</h4>
                <p>{{ task.description }}</p>
                <div class="task-meta">
                  <el-tag 
                    :type="getPriorityType(task.priority)"
                    size="small"
                  >
                    {{ task.priority }}
                  </el-tag>
                  <span class="task-due">{{ task.dueDate }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 右侧列 -->
      <div class="right-column">
        <!-- 项目进度卡片 -->
        <el-card class="project-card">
          <template #header>
            <div class="card-header">
              <h3>项目进度</h3>
              <el-button type="primary" text>项目概览</el-button>
            </div>
          </template>
          <div class="project-timeline">
            <div v-for="project in projects" :key="project.id" class="project-item">
              <div class="project-info">
                <h4>{{ project.name }}</h4>
                <p>{{ project.description }}</p>
              </div>
              <div class="project-progress">
                <el-progress 
                  :percentage="project.progress"
                  :color="getProgressColor(project.progress)"
                />
                <div class="project-members">
                  <el-avatar 
                    v-for="member in project.members" 
                    :key="member.id"
                    :size="24"
                    :src="member.avatar"
                  />
                </div>
              </div>
            </div>
          </div>
        </el-card>

        <!-- AI 建议卡片 -->
        <el-card class="ai-suggestion-card">
          <template #header>
            <div class="card-header">
              <h3>AI 助手建议</h3>
              <el-button type="primary" text>更多建议</el-button>
            </div>
          </template>
          <div class="suggestion-list">
            <div v-for="suggestion in aiSuggestions" :key="suggestion.id" class="suggestion-item">
              <div class="suggestion-content">
                <p>{{ suggestion.content }}</p>
                <div class="suggestion-actions">
                  <el-button type="success" size="small" round>好的</el-button>
                  <el-button type="info" size="small" round plain>稍后</el-button>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

// 用户信息
const userInfo = ref({
  name: '张三',
  role: '高级开发工程师',
  avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
  online: true,
  taskCount: 5,
  meetingCount: 3,
  projectCount: 2
})

// 日程数据
const schedules = ref([
  {
    id: 1,
    time: '09:00',
    title: '项目启动会议',
    description: '讨论新项目的具体实施方案',
    participants: [
      { id: 1, avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' },
      { id: 2, avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' }
    ]
  },
  {
    id: 2,
    time: '14:30',
    title: '代码审查',
    description: '审查新功能模块的代码实现',
    participants: [
      { id: 3, avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' }
    ]
  }
])

// 任务数据
const tasks = ref([
  {
    id: 1,
    title: '完成用户认证模块',
    description: '实现基于JWT的用户认证功能',
    priority: 'Urgent',
    completed: false,
    dueDate: '今天 18:00'
  },
  {
    id: 2,
    title: '优化数据库查询',
    description: '优化慢查询，添加必要的索引',
    priority: 'Essential',
    completed: false,
    dueDate: '明天 12:00'
  }
])

// 项目数据
const projects = ref([
  {
    id: 1,
    name: '慢性病管理系统',
    description: '实现慢性病患者的全流程管理',
    progress: 75,
    members: [
      { id: 1, avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' },
      { id: 2, avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' }
    ]
  },
  {
    id: 2,
    name: '医疗数据分析平台',
    description: '构建医疗数据分析和可视化平台',
    progress: 45,
    members: [
      { id: 3, avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' }
    ]
  }
])

// AI 建议数据
const aiSuggestions = ref([
  {
    id: 1,
    content: '您有一个即将到期的任务，需要我帮您安排时间吗？'
  },
  {
    id: 2,
    content: '检测到您的工作效率高峰期，建议现在处理重要任务。'
  }
])

// 获取优先级对应的类型
const getPriorityType = (priority) => {
  const types = {
    'Blocking': 'danger',
    'Urgent': 'warning',
    'Essential': 'success'
  }
  return types[priority] || 'info'
}

// 获取进度条颜色
const getProgressColor = (progress) => {
  if (progress >= 80) return '#67C23A'
  if (progress >= 50) return '#E6A23C'
  return '#F56C6C'
}

// 获取用户数据
const fetchUserData = async () => {
  try {
    // TODO: 调用API获取用户数据
    // const response = await getUserProfile()
    // userInfo.value = response.data
  } catch (error) {
    ElMessage.error('获取用户数据失败：' + error.message)
  }
}

onMounted(() => {
  fetchUserData()
})
</script>

<style scoped>
.user-profile {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

.profile-header {
  margin-bottom: 20px;
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 24px;
}

.avatar-container {
  position: relative;
}

.user-status {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: #909399;
  border: 2px solid #fff;
}

.user-status.online {
  background: #67C23A;
}

.user-details h2 {
  margin: 0;
  font-size: 24px;
  color: #303133;
}

.user-role {
  margin: 4px 0 16px;
  color: #909399;
}

.user-stats {
  display: flex;
  gap: 24px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 20px;
  font-weight: bold;
  color: #409EFF;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.profile-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
  color: #303133;
}

.schedule-list, .task-list, .project-timeline, .suggestion-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.schedule-item, .task-item, .project-item, .suggestion-item {
  padding: 12px;
  border-radius: 8px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.schedule-time {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.schedule-content h4, .task-content h4, .project-info h4 {
  margin: 0 0 8px;
  font-size: 16px;
  color: #303133;
}

.schedule-content p, .task-content p, .project-info p {
  margin: 0 0 8px;
  font-size: 14px;
  color: #606266;
}

.schedule-participants, .project-members {
  display: flex;
  gap: 8px;
}

.task-meta {
  display: flex;
  align-items: center;
  gap: 12px;
}

.task-due {
  font-size: 12px;
  color: #909399;
}

.project-progress {
  margin-top: 12px;
}

.suggestion-content {
  background: #1a1a1a;
  color: #fff;
  padding: 16px;
  border-radius: 8px;
}

.suggestion-actions {
  display: flex;
  gap: 8px;
  margin-top: 12px;
}

/* 响应式布局 */
@media (max-width: 1200px) {
  .profile-content {
    grid-template-columns: 1fr;
  }
}
</style> 