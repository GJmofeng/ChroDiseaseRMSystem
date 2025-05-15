<template>
  <div class="user-manage-page">
    <div class="tab-nav-bar">
      <div class="tab-list">
        <div class="tab-item" @click="$router.push('/main')" :class="{active: $route.path==='/main'}">
          <span class="tab-icon"><i class="fas fa-home"></i></span>
          首页
        </div>
        <div class="tab-item active">
          用户管理
          <span class="tab-close" @click="$router.push('/main')"><i class="fas fa-times"></i></span>
        </div>
      </div>
      <div class="tab-actions">
        <i class="fas fa-th grid-icon"></i>
      </div>
    </div>
    <div class="card">
      <!-- 顶部操作区 -->
      <div class="toolbar flex flex-wrap items-center mb-4 gap-2">
        <el-button type="primary" @click="onAddUser">添加成员</el-button>
        <el-button @click="onAdjustDept">调整部门</el-button>
        <el-button type="danger" :disabled="!multipleSelection.length" @click="onBatchDelete">批量删除</el-button>
        <el-divider direction="vertical"></el-divider>
        <el-radio-group v-model="statusFilter" class="ml-2">
          <el-radio-button label="全部" />
          <el-radio-button label="启用" />
          <el-radio-button label="禁用" />
        </el-radio-group>
        <el-input v-model="searchKey" placeholder="姓名/手机号/登录账号" class="ml-2 w-64" clearable />
        <el-button type="primary" icon="el-icon-search" @click="onSearch">查询</el-button>
        <el-button @click="onReset">重置</el-button>
        <div class="ml-auto flex items-center gap-2">
          <el-button icon="el-icon-refresh" circle @click="onRefresh" />
          <el-button icon="el-icon-setting" circle />
        </div>
      </div>
      <!-- 表格区 -->
      <el-table
        :data="filteredUsers"
        border
        style="width: 100%"
        @selection-change="handleSelectionChange"
        height="420"
      >
        <el-table-column type="selection" width="50" />
        <el-table-column prop="name" label="姓名" min-width="80" />
        <el-table-column prop="gender" label="性别" min-width="60" />
        <el-table-column prop="account" label="登录账号" min-width="100" />
        <el-table-column prop="phone" label="手机号" min-width="120" />
        <el-table-column prop="email" label="邮箱" min-width="140" />
        <el-table-column label="超管" min-width="70">
          <template #default="scope">
            <el-tag v-if="scope.row.isSuper" type="danger" effect="plain">超管</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" min-width="80">
          <template #default="scope">
            <el-tag v-if="scope.row.status==='启用'" type="primary" effect="plain">启用</el-tag>
            <el-tag v-else type="info" effect="plain">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="job" label="职务" min-width="100" />
        <el-table-column prop="role" label="角色" min-width="100" />
      </el-table>
      <!-- 分页 -->
      <div class="flex justify-between items-center mt-4">
        <div>共{{ filteredUsers.length }}条</div>
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="filteredUsers.length"
          layout="prev, pager, next, sizes"
          :page-sizes="[10, 20, 50]"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 静态用户数据
const users = ref([
  { name: '管理员', gender: '未知', account: 'admin', phone: '13500000000', email: '', isSuper: true, status: '启用', job: '技术P7', role: '' },
  { name: '胡克', gender: '未知', account: 'huke', phone: '13123123121', email: '', isSuper: true, status: '启用', job: '', role: '' },
  { name: '卓大', gender: '男', account: 'zhuoda', phone: '18637925892', email: '', isSuper: true, status: '启用', job: '技术总监', role: '' },
  { name: '善逸', gender: '男', account: 'shanyi', phone: '17630506613', email: '', isSuper: false, status: '启用', job: '技术总监', role: '' },
  { name: '琴酒', gender: '女', account: 'qinjiu', phone: '14112343212', email: '', isSuper: false, status: '启用', job: '', role: '' },
  { name: '清野', gender: '男', account: 'qingye', phone: '13123123111', email: '', isSuper: false, status: '启用', job: '', role: '' },
  { name: '飞叶', gender: '男', account: 'feiye', phone: '13123123112', email: '', isSuper: false, status: '启用', job: '', role: '' },
  { name: '玄朋', gender: '男', account: 'xuanpeng', phone: '13123123124', email: '', isSuper: false, status: '启用', job: '', role: '' },
  { name: '玄朋', gender: '男', account: 'peixian', phone: '18377482773', email: '', isSuper: false, status: '启用', job: '', role: '' },
])

const statusFilter = ref('全部')
const searchKey = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const multipleSelection = ref([])

const filteredUsers = computed(() => {
  let data = users.value
  if (statusFilter.value !== '全部') {
    data = data.filter(u => u.status === statusFilter.value)
  }
  if (searchKey.value) {
    data = data.filter(u =>
      u.name.includes(searchKey.value) ||
      u.phone.includes(searchKey.value) ||
      u.account.includes(searchKey.value)
    )
  }
  // 分页
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return data.slice(start, end)
})

function handleSelectionChange(val) {
  multipleSelection.value = val
}
function onAddUser() {
  ElMessage.info('添加成员功能待实现')
}
function onAdjustDept() {
  ElMessage.info('调整部门功能待实现')
}
function onBatchDelete() {
  ElMessageBox.confirm('确定要删除选中的成员吗？', '提示', {
    type: 'warning',
  }).then(() => {
    users.value = users.value.filter(u => !multipleSelection.value.includes(u))
    ElMessage.success('删除成功')
    multipleSelection.value = []
  })
}
function onSearch() {
  currentPage.value = 1
}
function onReset() {
  statusFilter.value = '全部'
  searchKey.value = ''
  currentPage.value = 1
}
function onRefresh() {
  ElMessage.success('已刷新')
}
function handleSizeChange(size) {
  pageSize.value = size
  currentPage.value = 1
}
function handlePageChange(page) {
  currentPage.value = page
}
</script>

<style scoped>
.user-manage-page {
  padding: 24px;
  background: #f5f6fa;
  min-height: 100vh;
}
.card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  padding: 24px;
}
.toolbar {
  margin-bottom: 16px;
}
.el-table th {
  font-weight: bold;
  background: #fafbfc;
}
.el-table .el-tag {
  font-size: 13px;
  border-radius: 6px;
  padding: 2px 12px;
}
.el-table .el-tag[effect='plain'] {
  background: #fff;
}
.el-table .el-tag[type='danger'] {
  color: #ff4d4f;
  border-color: #ff4d4f;
  background: #fff0f0;
}
.el-table .el-tag[type='primary'] {
  color: #409eff;
  border-color: #409eff;
  background: #f0f7ff;
}
.el-table .el-tag[type='info'] {
  color: #909399;
  border-color: #d3d4d6;
  background: #f4f4f5;
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
  font-size: 18px;
  color: #888;
  cursor: pointer;
  display: flex;
  align-items: center;
}
.grid-icon {
  font-size: 18px;
}
</style> 