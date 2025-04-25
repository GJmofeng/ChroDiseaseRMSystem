<template>
    <el-container class="layout-container">
        <!-- 左侧菜单栏 -->
        <el-aside :width="isCollapse ? '64px' : '240px'" class="aside">
            <div class="sidebar-logo">
                <el-icon color="#4c84ff"><HomeFilled /></el-icon>
                <span v-show="!isCollapse">慢性病报销系统</span>
            </div>
            
            <el-menu
                    :collapse="isCollapse"
                    :default-active="activeMenu"
                    class="el-menu-vertical"
                    background-color="#fff"
                    text-color="#666"
                    active-text-color="#4c84ff"
            >
                <!-- 系统管理 -->
                <el-sub-menu index="1">
                    <template #title>
                        <el-icon><Setting /></el-icon>
                        <span>系统管理</span>
                    </template>
                    <el-menu-item index="1-1">
                        <el-icon><User /></el-icon>
                        <span>用户管理</span>
                    </el-menu-item>
                    <el-menu-item index="1-2">
                        <el-icon><Lock /></el-icon>
                        <span>权限管理</span>
                    </el-menu-item>
                    <el-menu-item index="1-3">
                        <el-icon><Location /></el-icon>
                        <span>行政区管理</span>
                    </el-menu-item>
                    <el-menu-item index="1-4">
                        <el-icon><Bell /></el-icon>
                        <span>慢病消息管理</span>
                        <el-badge value="20" class="menu-badge" />
                    </el-menu-item>
                    <el-menu-item index="1-5">
                        <el-icon><Document /></el-icon>
                        <span>慢病政策管理</span>
                    </el-menu-item>
                </el-sub-menu>

                <!-- 业务管理 -->
                <el-sub-menu index="2">
                    <template #title>
                        <el-icon><Operation /></el-icon>
                        <span>业务管理</span>
                    </template>
                    <el-menu-item index="2-1">
                        <el-icon><UserPlus /></el-icon>
                        <span>参合信息录入</span>
                    </el-menu-item>
                    <el-menu-item index="2-2">
                        <el-icon><Postcard /></el-icon>
                        <span>慢性病证管理</span>
                    </el-menu-item>
                    <el-menu-item index="2-3">
                        <el-icon><Money /></el-icon>
                        <span>参合缴费登记</span>
                    </el-menu-item>
                    <el-menu-item index="2-4">
                        <el-icon><Tickets /></el-icon>
                        <span>报销申请</span>
                        <el-badge value="15" class="menu-badge" />
                    </el-menu-item>
                    <el-menu-item index="2-5">
                        <el-icon><List /></el-icon>
                        <span>报销审核</span>
                    </el-menu-item>
                    <el-menu-item index="2-6">
                        <el-icon><Money /></el-icon>
                        <span>报销汇款</span>
                    </el-menu-item>
                </el-sub-menu>

                <!-- 统计分析 -->
                <el-sub-menu index="3">
                    <template #title>
                        <el-icon><DataLine /></el-icon>
                        <span>统计分析</span>
                    </template>
                    <el-menu-item index="3-1">
                        <el-icon><Histogram /></el-icon>
                        <span>按年份统计</span>
                    </el-menu-item>
                    <el-menu-item index="3-2">
                        <el-icon><TrendCharts /></el-icon>
                        <span>按月份统计</span>
                    </el-menu-item>
                </el-sub-menu>
            </el-menu>

            <!-- 侧边栏切换按钮 -->
            <div class="sidebar-toggle" @click="toggleSidebar">
                <el-icon>
                    <component :is="isCollapse ? 'Expand' : 'Fold'" />
                </el-icon>
            </div>
        </el-aside>

        <el-container>
            <!-- 顶部导航栏 -->
            <el-header class="header">
                <div class="header-left">
                    <h2>系统首页</h2>
                </div>
                <div class="header-right">
                    <el-badge :value="5" class="notification">
                        <el-icon><Bell /></el-icon>
                    </el-badge>
                    <el-dropdown @command="handleCommand">
                        <span class="user-dropdown">
                            <el-avatar :size="32" :icon="UserFilled" />
                            <span>管理员</span>
                            <el-icon><CaretBottom /></el-icon>
                        </span>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item command="profile">
                                    <el-icon><User /></el-icon>个人信息
                                </el-dropdown-item>
                                <el-dropdown-item divided command="logout">
                                    <el-icon><SwitchButton /></el-icon>退出登录
                                </el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>
            </el-header>

            <!-- 主要内容区域 -->
            <el-main>
                <!-- 统计卡片 -->
                <el-row :gutter="24" class="stat-cards">
                    <el-col :span="6" v-for="(stat, index) in statistics" :key="index">
                        <div class="stat-card" :style="{ background: stat.gradient }">
                            <div class="stat-info">
                                <h3>{{ stat.title }}</h3>
                                <div class="stat-value">{{ stat.value }}</div>
                            </div>
                            <el-icon :size="32"><component :is="stat.icon" /></el-icon>
                        </div>
                    </el-col>
                </el-row>

                <!-- 最近活动和快捷操作 -->
                <el-row :gutter="24" class="content-section">
                    <el-col :span="16">
                        <el-card class="recent-activities">
                            <template #header>
                                <div class="card-header">
                                    <span>最近活动</span>
                                </div>
                            </template>
                            <div class="activities-list">
                                <div v-for="(activity, index) in recentActivities" 
                                     :key="index" 
                                     class="activity-item">
                                    <div class="activity-info">
                                        <div class="activity-title">{{ activity.title }}</div>
                                        <div class="activity-time">{{ activity.time }}</div>
                                    </div>
                                    <el-tag :type="activity.status">{{ activity.statusText }}</el-tag>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    
                    <el-col :span="8">
                        <el-card class="quick-actions">
                            <template #header>
                                <div class="card-header">
                                    <span>快捷操作</span>
                                </div>
                            </template>
                            <div class="actions-grid">
                                <el-button 
                                    v-for="(action, index) in quickActions"
                                    :key="index"
                                    :type="action.type"
                                    :icon="action.icon"
                                    @click="handleQuickAction(action.command)">
                                    {{ action.text }}
                                </el-button>
                            </div>
                        </el-card>
                    </el-col>
                </el-row>
            </el-main>
        </el-container>
    </el-container>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import {
    HomeFilled,
    Setting,
    User,
    Lock,
    Location,
    Bell,
    Document,
    Operation,
    UserPlus,
    Postcard,
    Money,
    Tickets,
    List,
    DataLine,
    Histogram,
    TrendCharts,
    CaretBottom,
    Fold,
    Expand,
    UserFilled,
    SwitchButton
} from '@element-plus/icons-vue'

const router = useRouter()
const isCollapse = ref(false)
const activeMenu = ref('1')

// 统计数据
const statistics = ref([
    {
        title: '今日申请数',
        value: '156',
        icon: 'Document',
        gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
    },
    {
        title: '待审核',
        value: '45',
        icon: 'Clock',
        gradient: 'linear-gradient(135deg, #FF6B6B 0%, #FF8E53 100%)'
    },
    {
        title: '本月报销金额',
        value: '¥89,650',
        icon: 'Money',
        gradient: 'linear-gradient(135deg, #4CAF50 0%, #45B649 100%)'
    },
    {
        title: '累计报销人数',
        value: '1,234',
        icon: 'User',
        gradient: 'linear-gradient(135deg, #2196F3 0%, #4CAF50 100%)'
    }
])

// 最近活动
const recentActivities = ref([
    {
        title: '张三提交了报销申请',
        time: '2024-01-20 14:30',
        status: 'warning',
        statusText: '待审核'
    },
    {
        title: '李四的报销申请已通过',
        time: '2024-01-20 13:15',
        status: 'success',
        statusText: '已通过'
    }
])

// 快捷操作
const quickActions = ref([
    {
        text: '新建申请',
        icon: 'Plus',
        type: 'primary',
        command: 'new'
    },
    {
        text: '批量审核',
        icon: 'Check',
        type: 'success',
        command: 'review'
    },
    {
        text: '导出报表',
        icon: 'Upload',
        type: 'danger',
        command: 'export'
    },
    {
        text: '系统设置',
        icon: 'Setting',
        type: 'info',
        command: 'settings'
    }
])

// 侧边栏切换
const toggleSidebar = () => {
    isCollapse.value = !isCollapse.value
}

// 用户操作处理
const handleCommand = (command) => {
    switch(command) {
        case 'profile':
            router.push('/profile')
            break
        case 'logout':
            ElMessageBox.confirm('确定要退出登录吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                router.push('/login')
            })
            break
    }
}

// 快捷操作处理
const handleQuickAction = (command) => {
    switch(command) {
        case 'new':
            router.push('/reimbursement/new')
            break
        case 'review':
            router.push('/reimbursement/review')
            break
        case 'export':
            // 处理导出报表
            break
        case 'settings':
            router.push('/settings')
            break
    }
}
</script>

<style scoped>
.layout-container {
    height: 100vh;
}

/* 侧边栏样式 */
.aside {
    background: #fff;
    transition: width 0.3s;
    box-shadow: 2px 0 6px rgba(0,21,41,.05);
    position: relative;
}

.sidebar-logo {
    height: 60px;
    display: flex;
    align-items: center;
    padding: 0 16px;
    font-size: 18px;
    font-weight: 600;
    color: #1a1a1a;
    border-bottom: 1px solid #f0f2f5;
}

.el-menu-vertical {
    border-right: none;
}

.menu-badge {
    margin-left: 8px;
}

/* 顶部导航栏样式 */
.header {
    background: #fff;
    box-shadow: 0 1px 4px rgba(0,21,41,.08);
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 24px;
}

.header-right {
    display: flex;
    align-items: center;
    gap: 24px;
}

.notification {
    cursor: pointer;
}

.user-dropdown {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
}

/* 主要内容区域样式 */
.el-main {
    background: #f8f9fc;
    padding: 24px;
}

/* 统计卡片样式 */
.stat-cards {
    margin-bottom: 24px;
}

.stat-card {
    padding: 24px;
    border-radius: 12px;
    color: #fff;
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 120px;
}

.stat-info h3 {
    font-size: 16px;
    opacity: 0.9;
    margin-bottom: 8px;
}

.stat-value {
    font-size: 28px;
    font-weight: 600;
}

/* 内容区域样式 */
.content-section {
    margin-top: 24px;
}

.activities-list {
    display: flex;
    flex-direction: column;
    gap: 16px;
}

.activity-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-bottom: 16px;
    border-bottom: 1px solid #f0f2f5;
}

.activity-time {
    font-size: 12px;
    color: #999;
    margin-top: 4px;
}

.actions-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
}

/* 响应式布局调整 */
@media screen and (max-width: 1200px) {
    .stat-cards .el-col {
        width: 50%;
    }
}

@media screen and (max-width: 768px) {
    .content-section .el-col {
        width: 100%;
    }
}
</style>