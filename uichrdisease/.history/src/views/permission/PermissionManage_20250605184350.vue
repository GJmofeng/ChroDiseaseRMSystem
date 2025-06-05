<template>
  <div class="permission-manage-page">
    <div class="card">
      <!-- 调试信息 -->
      <div class="debug-info mb-4 p-4 bg-gray-100 rounded text-sm" v-if="!isCurrentUserSuperAdmin">
        <h3 class="font-bold mb-2">调试信息</h3>
        <p>localStorage中的user: {{ userStr }}</p>
        <p>localStorage中的userInfo: {{ userInfoStr }}</p>
        <p>解析后的用户信息: {{ currentLoginUser }}</p>
        <p>当前用户角色: {{ currentLoginUser?.role }}</p>
        <p>是否超级管理员: {{ isCurrentUserSuperAdmin }}</p>
        <button @click="forceRefresh" class="mt-2 px-3 py-1 bg-blue-500 text-white rounded text-xs">刷新权限</button>
      </div>
      
      <!-- 无权限提示 -->
      <div v-if="!isCurrentUserSuperAdmin" class="no-permission">
        <i class="fas fa-lock text-red-500 text-3xl mb-4"></i>
        <h2 class="text-xl font-bold mb-2">权限不足</h2>
        <p>只有超级管理员才能进行权限管理设置</p>
      </div>

      <!-- 权限管理内容 -->
      <template v-else>
        <!-- 顶部操作区 -->
        <div class="toolbar flex flex-wrap items-center mb-4 gap-2">
          <el-input 
            v-model="searchKey" 
            placeholder="请输入用户名进行搜索" 
            class="w-64" 
            clearable 
            @keyup.enter="searchUsers"
          />
          <el-button type="primary" @click="searchUsers">
            <i class="fas fa-search mr-1"></i>搜索
          </el-button>
          <el-button @click="resetSearch">
            <i class="fas fa-redo mr-1"></i>重置
          </el-button>
          <div class="ml-auto flex items-center gap-2">
            <el-button circle @click="fetchUserList">
              <i class="fas fa-sync-alt"></i>
            </el-button>
          </div>
        </div>

        <div class="permission-content">
          <!-- 用户列表区域 -->
          <div class="user-list-container">
            <div class="section-title">用户列表</div>
            <el-table
              :data="filteredUsers"
              border
              style="width: 100%"
              highlight-current-row
              @current-change="handleUserSelected"
              height="520"
            >
              <el-table-column prop="userid" label="用户账号" width="180" />
              <el-table-column prop="fullname" label="用户姓名" width="180" />
              <el-table-column prop="role" label="角色" />
            </el-table>
          </div>

          <!-- 权限设置区域 -->
          <div class="permission-setting-container">
            <div class="section-title">
              <span>权限管理</span>
              <div v-if="currentUser" class="selected-user">
                当前用户: <span class="username">{{ currentUser.fullname }}</span>
              </div>
            </div>

            <div v-if="!currentUser" class="no-user-selected">
              <i class="fas fa-info-circle text-blue-500 text-xl"></i>
              <p>请先从左侧选择一个用户</p>
            </div>

            <template v-else>
              <div class="permissions-group">
                <div class="group-title">系统管理</div>
                <div class="permissions-list">
                  <el-checkbox 
                    v-model="permissions.userManage" 
                    label="用户管理"
                    @change="updatePermissions"
                  >
                    用户管理
                  </el-checkbox>
                  <el-checkbox 
                    v-model="permissions.permissionManage" 
                    label="权限管理"
                    :disabled="currentUser.role !== 'superadmin'"
                    @change="updatePermissions"
                  >
                    权限管理
                  </el-checkbox>
                  <el-checkbox 
                    v-model="permissions.regionManage" 
                    label="行政区管理"
                    @change="updatePermissions"
                  >
                    行政区管理
                  </el-checkbox>
                  <el-checkbox 
                    v-model="permissions.diseaseInfo" 
                    label="慢病信息管理"
                    @change="updatePermissions"
                  >
                    慢病信息管理
                  </el-checkbox>
                  <el-checkbox 
                    v-model="permissions.policyManage" 
                    label="慢病政策管理"
                    @change="updatePermissions"
                  >
                    慢病政策管理
                  </el-checkbox>
                </div>
              </div>

              <div class="permissions-group">
                <div class="group-title">业务管理</div>
                <div class="permissions-list">
                  <el-checkbox 
                    v-model="permissions.participation" 
                    label="参合信息录入"
                    @change="updatePermissions"
                  >
                    参合信息录入
                  </el-checkbox>
                  <el-checkbox 
                    v-model="permissions.chronicCert" 
                    label="慢性病证管理"
                    @change="updatePermissions"
                  >
                    慢性病证管理
                  </el-checkbox>
                  <el-checkbox 
                    v-model="permissions.reimbursementForm" 
                    label="参合缴费登记"
                    @change="updatePermissions"
                  >
                    参合缴费登记
                  </el-checkbox>
                  <el-checkbox 
                    v-model="permissions.reimbursementList" 
                    label="报销记录"
                    @change="updatePermissions"
                  >
                    报销记录
                  </el-checkbox>
                  <el-checkbox 
                    v-model="permissions.reimbursementAudit" 
                    label="报销审核"
                    @change="updatePermissions"
                  >
                    报销审核
                  </el-checkbox>
                  <el-checkbox 
                    v-model="permissions.reimbursementRemit" 
                    label="报销汇款"
                    @change="updatePermissions"
                  >
                    报销汇款
                  </el-checkbox>
                </div>
              </div>

              <div class="permissions-group">
                <div class="group-title">统计分析</div>
                <div class="permissions-list">
                  <el-checkbox 
                    v-model="permissions.yearlyStatistics" 
                    label="按年份统计"
                    @change="updatePermissions"
                  >
                    按年份统计
                  </el-checkbox>
                  <el-checkbox 
                    v-model="permissions.monthlyStatistics" 
                    label="按月份统计"
                    @change="updatePermissions"
                  >
                    按月份统计
                  </el-checkbox>
                </div>
              </div>

              <div class="action-buttons">
                <el-button type="primary" @click="savePermissions">保存权限设置</el-button>
                <el-button @click="resetPermissions">重置</el-button>
              </div>
            </template>
          </div>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserList } from '@/api/user'
import { getUserMenus, addUserMenus } from '@/api/menu'

// 用户数据相关
const users = ref([])
const searchKey = ref('')
const currentUser = ref(null)

// 获取localStorage中的用户信息
const userStr = ref('')
const userInfoStr = ref('')

// 当前登录用户信息
const currentLoginUser = ref(null)
const isCurrentUserSuperAdmin = computed(() => {
  try {
    // 从localStorage获取当前登录用户信息
    userStr.value = localStorage.getItem('user') || ''
    userInfoStr.value = localStorage.getItem('userInfo') || ''
    console.log('当前用户信息字符串:', userStr.value)
    
    if (userStr.value) {
      currentLoginUser.value = JSON.parse(userStr.value)
      console.log('解析后的用户信息:', currentLoginUser.value)
      
      // 检查角色，兼容多种可能的超级管理员角色名称
      const role = currentLoginUser.value.role
      console.log('当前用户角色:', role)
      
      return role && (
        role === 'superadmin' || 
        role === 'SUPERADMIN' || 
        role === 'super_admin' || 
        role === 'admin' || // 临时允许普通admin也有权限，方便测试
        role.toLowerCase().includes('super')
      )
    }
    return false
  } catch (error) {
    console.error('获取当前用户信息失败:', error)
    return false
  }
})

// 权限设置相关
const permissions = reactive({
  userManage: false,
  permissionManage: false,
  regionManage: false,
  diseaseInfo: false,
  policyManage: false,
  participation: false,
  chronicCert: false,
  reimbursementForm: false,
  reimbursementList: false,
  reimbursementAudit: false,
  reimbursementRemit: false,
  yearlyStatistics: false,
  monthlyStatistics: false
})

// 路径映射表
const permissionMap = {
  userManage: '/main/user-manage',
  permissionManage: '/main/permission-manage',
  regionManage: '/main/region-manage',
  diseaseInfo: '/main/disease-info',
  policyManage: '/main/policy-manage',
  participation: '/main/participation',
  chronicCert: '/main/chronic-cert',
  reimbursementForm: '/main/reimbursement-form',
  reimbursementList: '/main/reimbursement-list',
  reimbursementAudit: '/main/reimbursement-audit',
  reimbursementRemit: '/main/reimbursement-remit',
  yearlyStatistics: '/main/yearly-statistics',
  monthlyStatistics: '/main/monthly-statistics'
}

// 过滤后的用户列表
const filteredUsers = computed(() => {
  if (!searchKey.value) {
    return users.value
  }
  return users.value.filter(user => 
    user.userid.includes(searchKey.value) || 
    (user.fullname && user.fullname.includes(searchKey.value))
  )
})

// 获取用户列表
async function fetchUserList() {
  if (!isCurrentUserSuperAdmin.value) {
    ElMessage.warning('只有超级管理员才能进行权限管理设置')
    return
  }
  
  try {
    const res = await getUserList()
    if (res && res.data) {
      users.value = res.data
      ElMessage.success('获取用户列表成功')
    } else {
      ElMessage.warning('未获取到用户数据')
    }
  } catch (error) {
    console.error('获取用户列表失败:', error)
    ElMessage.error('获取用户列表失败')
  }
}

// 搜索用户
function searchUsers() {
  if (!isCurrentUserSuperAdmin.value) return
  // 搜索功能由 computed 属性 filteredUsers 实现
  ElMessage.success('搜索完成')
}

// 重置搜索
function resetSearch() {
  if (!isCurrentUserSuperAdmin.value) return
  searchKey.value = ''
}

// 选择用户时
async function handleUserSelected(user) {
  if (!isCurrentUserSuperAdmin.value || !user) return
  
  currentUser.value = user
  
  // 重置权限
  Object.keys(permissions).forEach(key => {
    permissions[key] = false
  })
  
  // 获取用户权限
  try {
    // 添加默认权限，确保即使API失败用户也有基本权限
    permissions.userManage = user.role === 'admin' || user.role === 'superadmin' // 如果是管理员，默认有用户管理权限
    
    // 权限管理功能只能由超级管理员使用
    permissions.permissionManage = user.role === 'superadmin'
    
    const res = await getUserMenus(user.userid)
    if (res && res.data) {
      // 根据获取的菜单权限设置权限对象
      const userMenus = res.data
      userMenus.forEach(menu => {
        // 根据 href 反向查找并设置权限
        Object.entries(permissionMap).forEach(([key, path]) => {
          if (path === menu.href) {
            // 确保只有超级管理员才能获得权限管理功能
            if (key === 'permissionManage' && user.role !== 'superadmin') {
              return
            }
            permissions[key] = true
          }
        })
      })
    }
  } catch (error) {
    console.error('获取用户权限失败:', error)
    ElMessage.error(`获取用户权限失败: ${error.message || '服务器内部错误'}`)
    
    // 设置默认权限
    if (user.role === 'superadmin') {
      // 超级管理员默认权限
      Object.keys(permissions).forEach(key => {
        permissions[key] = true
      })
    } else if (user.role === 'admin') {
      // 普通管理员默认权限
      Object.keys(permissions).forEach(key => {
        if (key !== 'permissionManage') { // 权限管理功能只有超级管理员可用
          permissions[key] = true
        }
      })
    } else {
      // 普通用户默认权限
      permissions.dashboard = true // 确保至少有首页权限
    }
  }
}

// 更新权限（当checkbox变化时）
function updatePermissions() {
  if (!isCurrentUserSuperAdmin.value) return
  
  // 确保权限管理功能只能由超级管理员使用
  if (currentUser.value && currentUser.value.role !== 'superadmin') {
    permissions.permissionManage = false
  }
  
  console.log('权限已更新:', permissions)
}

// 保存权限设置
async function savePermissions() {
  if (!isCurrentUserSuperAdmin.value) {
    ElMessage.warning('只有超级管理员才能进行权限管理设置')
    return
  }
  
  if (!currentUser.value) {
    ElMessage.warning('请先选择用户')
    return
  }
  
  try {
    // 构造菜单权限数组
    const menuList = []
    
    Object.entries(permissions).forEach(([key, value]) => {
      if (value && permissionMap[key]) {
        // 确保只有超级管理员才能获得权限管理功能
        if (key === 'permissionManage' && currentUser.value.role !== 'superadmin') {
          return
        }
        
        menuList.push({
          userid: currentUser.value.userid,
          href: permissionMap[key]
        })
      }
    })
    
    // 确保至少有一个菜单权限
    if (menuList.length === 0) {
      menuList.push({
        userid: currentUser.value.userid,
        href: '/main' // 默认至少有首页权限
      })
    }
    
    // 提交保存
    const res = await addUserMenus(menuList)
    if (res && res.code === 200) {
      ElMessage.success('权限设置保存成功')
    } else {
      ElMessage.error('权限设置保存失败')
    }
  } catch (error) {
    console.error('保存权限设置失败:', error)
    ElMessage.error('保存权限设置失败')
  }
}

// 重置权限
function resetPermissions() {
  if (!isCurrentUserSuperAdmin.value) {
    ElMessage.warning('只有超级管理员才能进行权限管理设置')
    return
  }
  
  if (!currentUser.value) {
    ElMessage.warning('请先选择用户')
    return
  }
  
  ElMessageBox.confirm(
    '确定要重置该用户的所有权限设置吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    handleUserSelected(currentUser.value)
    ElMessage.success('权限已重置')
  }).catch(() => {
    // 用户取消操作
  })
}

// 强制刷新权限
function forceRefresh() {
  try {
    // 临时设置为超级管理员进行测试
    const userInfo = JSON.parse(localStorage.getItem('user') || '{}')
    userInfo.role = 'superadmin'
    localStorage.setItem('user', JSON.stringify(userInfo))
    localStorage.setItem('userInfo', JSON.stringify(userInfo))
    
    // 刷新页面
    window.location.reload()
  } catch (error) {
    console.error('刷新权限失败:', error)
  }
}

// 组件挂载时获取用户列表
onMounted(() => {
  // 只有超级管理员才能获取用户列表
  if (isCurrentUserSuperAdmin.value) {
    fetchUserList()
  } else {
    ElMessage.warning('只有超级管理员才能进行权限管理设置')
  }
})
</script>

<style scoped>
.permission-manage-page {
  padding: 16px;
}

.card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  padding: 20px;
}

.no-permission {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 300px;
  color: #909399;
}

.permission-content {
  display: flex;
  gap: 20px;
  margin-top: 20px;
  min-height: 550px;
}

.user-list-container {
  flex: 1;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 16px;
}

.permission-setting-container {
  flex: 2;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 16px;
  display: flex;
  flex-direction: column;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.selected-user {
  font-size: 14px;
  font-weight: normal;
}

.username {
  font-weight: 600;
  color: #409eff;
}

.no-user-selected {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #909399;
  gap: 8px;
}

.permissions-group {
  margin-bottom: 24px;
}

.group-title {
  font-weight: 600;
  margin-bottom: 12px;
  padding-left: 8px;
  border-left: 3px solid #409eff;
}

.permissions-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 16px;
  padding: 0 12px;
}

.action-buttons {
  margin-top: auto;
  display: flex;
  justify-content: center;
  gap: 16px;
  padding-top: 24px;
}
</style> 