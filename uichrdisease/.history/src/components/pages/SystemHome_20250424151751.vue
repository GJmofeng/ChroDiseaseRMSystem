<template>
    <el-container class="layout-container">
        <!-- 左侧菜单栏 -->
        <el-aside width="200px" class="aside">
            <el-menu
                    router
                    default-active="1"
                    class="el-menu-vertical"
                    background-color="#304156"
                    text-color="#bfcbd9"
                    active-text-color="#409EFF"
            >
                <el-sub-menu index="1">
                    <template #title>
                        <span>系统管理</span>
                    </template>
                    <el-menu-item index="/SystemHome/UserManage">
                        <span>用户管理</span>
                    </el-menu-item>
                    <el-menu-item index="/SystemHome/PermissionManage">
                        <span>权限管理</span>
                    </el-menu-item>
                    <el-menu-item index="/SystemHome/DivisionManage">
                        <span>行政区域管理</span>
                    </el-menu-item>
                    <el-menu-item index="/SystemHome/MedicalManage">
                        <span>慢病信息管理</span>
                    </el-menu-item>
                    <el-menu-item index="/SystemHome/PolicyManage">
                        <span>慢病政策管理</span>
                    </el-menu-item>
                </el-sub-menu>
                <el-sub-menu index="2">
                    <template #title>
                        <span>业务管理</span>
                    </template>
                    <el-menu-item index="/SystemHome/InsuredManage">
                        <span>参合信息录入</span>
                    </el-menu-item>
                    <el-menu-item index="/SystemHome/MedicalCardManage">
                        <span>慢性病证管理</span>
                    </el-menu-item>
                    <el-menu-item index="/SystemHome/PaymentManage">
                        <span>参合缴费登记</span>
                    </el-menu-item>
                    <el-menu-item index="/SystemHome/ApplyInfoManage">
                        <span>报销申请</span>
                    </el-menu-item>
                    <el-menu-item index="/SystemHome/AuditManage">
                        <span>报销审核</span>
                    </el-menu-item>
                    <el-menu-item index="/SystemHome/RemitManage">
                        <span>报销汇款</span>
                    </el-menu-item>
                </el-sub-menu>
                <el-sub-menu index="3">
                    <template #title>
                        <span>统计分析</span>
                    </template>
                    <el-menu-item index="/SystemHome/SStatics">
                        <span>按年份统计</span>
                    </el-menu-item>
                    <el-menu-item index="">
                        <span>按月份统计</span>
                    </el-menu-item>
                </el-sub-menu>
            </el-menu>
        </el-aside>

        <el-container>
            <!-- 顶部个人信息栏 -->
            <el-header class="header">
                <div class="header-left">
                    <el-icon class="toggle-sidebar" @click="toggleSidebar"><Fold /></el-icon>
                    <breadcrumb />
                </div>
                <div class="header-right">
                    <el-dropdown>
            <span class="el-dropdown-link">
              <el-avatar :size="32" src="/placeholder.svg?height=32&width=32" />
              管理员
              <el-icon><CaretBottom /></el-icon>
            </span>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item>个人信息</el-dropdown-item>
                                <el-dropdown-item>修改密码</el-dropdown-item>
                                <el-dropdown-item>退出登录</el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>
            </el-header>

            <!-- 右侧主要内容区域 -->
            <el-main>
                <router-view />
            </el-main>
        </el-container>
    </el-container>
</template>

<script setup>
import { ref } from 'vue'
import { House, Document, Setting, Fold, CaretBottom } from '@element-plus/icons-vue'

const isCollapse = ref(false)

const toggleSidebar = () => {
    isCollapse.value = !isCollapse.value
}
</script>

<style scoped>
.layout-container {
    height: 100vh;
}

.aside {
    background-color: #304156;
    color: #bfcbd9;
}

.logo {
    height: 60px;
    display: flex;
    align-items: center;
    padding-left: 20px;
    font-size: 18px;
    font-weight: bold;
}

.logo img {
    margin-right: 10px;
}

.el-menu-vertical {
    border-right: none;
}

.header {
    background-color: #fff;
    color: #333;
    line-height: 60px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;
    box-shadow: 0 1px 4px rgba(0,21,41,.08);
}

.header-left, .header-right {
    display: flex;
    align-items: center;
}

.toggle-sidebar {
    font-size: 20px;
    cursor: pointer;
    margin-right: 20px;
}

.el-dropdown-link {
    display: flex;
    align-items: center;
    cursor: pointer;
}

.el-dropdown-link .el-avatar {
    margin-right: 8px;
}

.el-main {
    background-color: #f0f2f5;
    padding: 20px;
}
</style>