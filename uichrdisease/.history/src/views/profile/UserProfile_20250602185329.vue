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
          <h2>用户名：{{ userInfo.name }}</h2>
          <p class="user-role">角色：{{ userInfo.role }}</p>
          <p class="user-account">登录账号：{{ userInfo.account }}</p>
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
            <div v-for="suggestion in aiSuggestions" 
                 :key="suggestion.id" 
                 class="suggestion-item"
                 :class="getPriorityStyle(suggestion.priority)">
              <div class="suggestion-header">
                <i :class="getSuggestionIcon(suggestion.type)" class="suggestion-icon"></i>
                <span class="suggestion-time">{{ formatTimestamp(suggestion.timestamp) }}</span>
              </div>
              <div class="suggestion-content">
                <p>{{ suggestion.content }}</p>
                <div class="suggestion-actions">
                  <el-button 
                    type="success" 
                    size="small" 
                    round
                    @click="handleAISuggestion(suggestion, 'accept')"
                    :disabled="suggestion.status !== 'pending'">
                    好的
                  </el-button>
                  <el-button 
                    type="info" 
                    size="small" 
                    round 
                    plain
                    @click="handleAISuggestion(suggestion, 'postpone')"
                    :disabled="suggestion.status !== 'pending'">
                    稍后
                  </el-button>
                  <el-button 
                    type="danger" 
                    size="small" 
                    round 
                    plain
                    @click="handleAISuggestion(suggestion, 'dismiss')"
                    :disabled="suggestion.status !== 'pending'">
                    忽略
                  </el-button>
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
import { useRouter } from 'vue-router'

const router = useRouter()

// 用户信息
const userInfo = ref({
  name: '',
  role: '',
  account: '',
  avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
  online: true,
  taskCount: 0,
  meetingCount: 0,
  projectCount: 0
})

// 获取用户数据
const fetchUserData = async () => {
  try {
    // 从localStorage获取用户信息
    const userInfoStr = localStorage.getItem('userInfo')
    if (userInfoStr) {
      const storedUserInfo = JSON.parse(userInfoStr)
      console.log('存储的用户信息:', storedUserInfo) // 添加调试日志
      userInfo.value = {
        ...userInfo.value,
        name: storedUserInfo.fullname || '', // 使用后端实体类的fullname字段
        role: storedUserInfo.role || '普通用户',
        account: storedUserInfo.userid || '', // 使用后端实体类的userid字段
      }
      console.log('更新后的用户信息:', userInfo.value) // 添加调试日志
    } else {
      ElMessage.warning('未检测到登录信息，请先登录')
      router.push('/login')
    }

    // TODO: 调用API获取用户的待办任务、会议和项目数据
    // const response = await getUserProfile()
    // const { taskCount, meetingCount, projectCount } = response.data
    // userInfo.value.taskCount = taskCount
    // userInfo.value.meetingCount = meetingCount
    // userInfo.value.projectCount = projectCount
  } catch (error) {
    console.error('获取用户信息失败:', error) // 添加错误日志
    ElMessage.error('获取用户数据失败：' + error.message)
  }
}

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
    type: 'task',
    content: '您有一个即将到期的任务，需要我帮您安排时间吗？',
    priority: 'high',
    timestamp: new Date().toISOString(),
    status: 'pending'
  },
  {
    id: 2,
    type: 'efficiency',
    content: '检测到您的工作效率高峰期，建议现在处理重要任务。',
    priority: 'medium',
    timestamp: new Date().toISOString(),
    status: 'pending'
  },
  {
    id: 3,
    type: 'health',
    content: '您已经连续工作2小时，建议休息一下，保护眼睛。',
    priority: 'high',
    timestamp: new Date().toISOString(),
    status: 'pending'
  },
  {
    id: 4,
    type: 'meeting',
    content: '下午3点有一个重要会议，需要我帮您准备会议材料吗？',
    priority: 'medium',
    timestamp: new Date().toISOString(),
    status: 'pending'
  }
])

// 处理AI建议
const handleAISuggestion = async (suggestion, action) => {
  try {
    switch (action) {
      case 'accept':
        // 接受建议
        suggestion.status = 'accepted'
        ElMessage.success('已接受建议')
        // TODO: 调用后端API处理建议
        break
      case 'postpone':
        // 稍后处理
        suggestion.status = 'postponed'
        ElMessage.info('已标记为稍后处理')
        // TODO: 调用后端API更新建议状态
        break
      case 'dismiss':
        // 忽略建议
        suggestion.status = 'dismissed'
        ElMessage.info('已忽略建议')
        // TODO: 调用后端API更新建议状态
        break
    }
  } catch (error) {
    console.error('处理AI建议失败:', error)
    ElMessage.error('操作失败，请稍后重试')
  }
}

// 获取建议类型对应的图标
const getSuggestionIcon = (type) => {
  const icons = {
    task: 'el-icon-s-order',
    efficiency: 'el-icon-data-line',
    health: 'el-icon-first-aid-kit',
    meeting: 'el-icon-date'
  }
  return icons[type] || 'el-icon-info'
}

// 获取建议优先级对应的样式
const getPriorityStyle = (priority) => {
  const styles = {
    high: 'priority-high',
    medium: 'priority-medium',
    low: 'priority-low'
  }
  return styles[priority] || ''
}

// 格式化时间戳
const formatTimestamp = (timestamp) => {
  const date = new Date(timestamp)
  return date.toLocaleString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit'
  })
}

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
  background: #fff9f0; /* 奶油白色 */
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.profile-header:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 24px;
  padding: 20px;
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
  color: #2c3e50;
}

.user-role {
  margin: 4px 0 8px;
  color: #34495e;
  font-size: 16px;
}

.user-account {
  margin: 0 0 16px;
  color: #2c3e50;
  font-size: 16px;
}

.user-stats {
  display: flex;
  gap: 24px;
}

.stat-item {
  text-align: center;
  padding: 12px 20px;
  background: #f8f9fa;
  border-radius: 8px;
  transition: transform 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-3px);
}

.stat-value {
  display: block;
  font-size: 20px;
  font-weight: bold;
  color: #2c3e50;
}

.stat-label {
  font-size: 14px;
  color: #7f8c8d;
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
  padding: 15px 20px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
  color: #2c3e50;
}

.schedule-card {
  background: #f0f7f4; /* 浅绿色 */
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.schedule-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.task-card {
  background: #f5f0e8; /* 浅棕色 */
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.task-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.project-card {
  background: #e8f4f0; /* 墨绿色 */
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.project-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.ai-suggestion-card {
  background: #f8f9fa;
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.ai-suggestion-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.schedule-list, .task-list, .project-timeline, .suggestion-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 20px;
}

.schedule-item, .task-item, .project-item, .suggestion-item {
  padding: 16px;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.8);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease;
}

.schedule-item:hover, .task-item:hover, .project-item:hover, .suggestion-item:hover {
  transform: translateY(-3px);
}

.schedule-time {
  font-size: 14px;
  color: #7f8c8d;
  margin-bottom: 8px;
}

.schedule-content h4, .task-content h4, .project-info h4 {
  margin: 0 0 8px;
  font-size: 16px;
  color: #2c3e50;
}

.schedule-content p, .task-content p, .project-info p {
  margin: 0 0 8px;
  font-size: 14px;
  color: #34495e;
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
  color: #7f8c8d;
}

.project-progress {
  margin-top: 12px;
}

.suggestion-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.suggestion-icon {
  font-size: 18px;
  color: #2c3e50;
}

.suggestion-time {
  font-size: 12px;
  color: #7f8c8d;
}

.suggestion-content {
  background: #2c3e50;
  color: #fff;
  padding: 16px;
  border-radius: 8px;
}

.suggestion-actions {
  display: flex;
  gap: 8px;
  margin-top: 12px;
}

.priority-high {
  border-left: 4px solid #e74c3c;
}

.priority-medium {
  border-left: 4px solid #f1c40f;
}

.priority-low {
  border-left: 4px solid #2ecc71;
}

.suggestion-item {
  opacity: 1;
  transition: opacity 0.3s ease;
}

.suggestion-item[data-status="accepted"],
.suggestion-item[data-status="dismissed"] {
  opacity: 0.6;
}

/* 添加动画效果 */
@keyframes slideIn {
  from {
    transform: translateX(-20px);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

.suggestion-item {
  animation: slideIn 0.3s ease-out;
}

/* 响应式布局 */
@media (max-width: 1200px) {
  .profile-content {
    grid-template-columns: 1fr;
  }
}
</style> 