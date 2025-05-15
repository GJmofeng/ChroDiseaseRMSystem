<template>
  <div class="user-manage-page">
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
          <el-button icon="el-icon-setting" circle @click="showColumnSetting = true" />
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
        <el-table-column label="操作" fixed="right" min-width="260" align="center">
          <template #default="scope">
            <div class="action-row">
              <el-button class="action-btn edit" :class="{showText: hoverEdit===scope.$index}" round size="small"
                @mouseenter="hoverEdit=scope.$index" @mouseleave="hoverEdit=null" @click="onEdit(scope.row)">
                <i class="fas fa-edit"></i>
                <span v-if="hoverEdit===scope.$index" class="action-text">编辑</span>
              </el-button>
              <el-button class="action-btn reset" :class="{showText: hoverReset===scope.$index}" round size="small"
                @mouseenter="hoverReset=scope.$index" @mouseleave="hoverReset=null" @click="onResetPwd(scope.row)">
                <i class="fas fa-key"></i>
                <span v-if="hoverReset===scope.$index" class="action-text">重置密码</span>
              </el-button>
              <el-button class="action-btn disable" :class="{showText: hoverDisable===scope.$index}" round size="small"
                @mouseenter="hoverDisable=scope.$index" @mouseleave="hoverDisable=null" @click="onDisable(scope.row)">
                <i class="fas fa-ban"></i>
                <span v-if="hoverDisable===scope.$index" class="action-text">禁用</span>
              </el-button>
            </div>
          </template>
        </el-table-column>
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
    <!-- 列设置弹窗 -->
    <el-dialog v-model="showColumnSetting" title="设置列" width="700px" :close-on-click-modal="false">
      <div class="column-setting-tip" style="margin-bottom:10px;">
        <el-icon style="color:#409eff"><i class="el-icon-info"></i></el-icon>
        可以通过拖拽行直接修改顺序哦；（⨉为固定列，不可拖拽）
      </div>
      <el-table :data="columnSettings" border style="width:100%;margin-top:12px;">
        <el-table-column type="selection" width="50" />
        <el-table-column label="列" prop="label">
          <template #default="scope">
            <span style="cursor:move;">⠿</span>
            {{ scope.row.label }}
          </template>
        </el-table-column>
        <el-table-column label="宽度(像素)">
          <template #default="scope">
            <el-input-number v-model="scope.row.width" :min="50" :max="400" size="small" />
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-link type="primary" @click="moveUp(scope.$index)">上移</el-link>
            <el-link type="primary" @click="moveDown(scope.$index)">下移</el-link>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="showColumnSetting = false">取消</el-button>
        <el-button type="primary" @click="saveColumnSetting">保存</el-button>
        <el-button type="danger" @click="resetColumnSetting">恢复默认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import draggable from 'vuedraggable'
import TabMenu from '../components/TabMenu.vue'

const router = useRouter()

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

const tags = ref([
  { title: '首页', path: '/main', icon: 'fa-home', closable: false },
  { title: '用户管理', path: '/main/user-manage', icon: 'fa-users', closable: true }
])
const activePath = ref('/main/user-manage')

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
function handleMenuCloseOther() {
  tags.value = tags.value.filter(tag => !tag.closable || tag.path === activePath.value)
}
function handleMenuCloseAll() {
  tags.value = tags.value.filter(tag => !tag.closable)
  activePath.value = '/main'
  router.push('/main')
}

function onEdit(row) {
  // 编辑逻辑
}
function onResetPwd(row) {
  // 重置密码逻辑
}
function onDisable(row) {
  // 禁用逻辑
}

const hoverEdit = ref(null)
const hoverReset = ref(null)
const hoverDisable = ref(null)
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
  overflow-x: auto;
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
.action-row {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 16px;
}
.action-btn {
  border: none;
  box-shadow: 0 2px 8px rgba(76,132,255,0.08);
  color: #fff;
  font-size: 16px;
  transition: box-shadow 0.2s, background 0.2s, width 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 18px;
  padding: 0 14px;
  min-width: 36px;
  height: 36px;
  position: relative;
  overflow: hidden;
}
.action-btn.edit {
  background: linear-gradient(135deg, #4c84ff 0%, #2d5cfe 100%);
}
.action-btn.edit:hover, .action-btn.edit.showText {
  background: linear-gradient(135deg, #2563eb 0%, #4c84ff 100%);
  box-shadow: 0 4px 16px rgba(76,132,255,0.18);
}
.action-btn.reset {
  background: linear-gradient(135deg, #ff9800 0%, #ffc107 100%);
}
.action-btn.reset:hover, .action-btn.reset.showText {
  background: linear-gradient(135deg, #ff9800 0%, #ffb300 100%);
  box-shadow: 0 4px 16px rgba(255,168,0,0.18);
}
.action-btn.disable {
  background: linear-gradient(135deg, #ff4d4f 0%, #ff7875 100%);
}
.action-btn.disable:hover, .action-btn.disable.showText {
  background: linear-gradient(135deg, #d32f2f 0%, #ff4d4f 100%);
  box-shadow: 0 4px 16px rgba(255,77,79,0.18);
}
.action-btn i {
  font-size: 15px;
  margin-right: 0;
  transition: margin 0.2s;
}
.action-btn .action-text {
  margin-left: 8px;
  font-size: 14px;
  font-weight: 500;
  white-space: nowrap;
  letter-spacing: 1px;
  transition: opacity 0.2s;
}
.card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  padding: 24px;
  overflow-x: auto;
}
.el-table {
  min-width: 900px;
}
</style> 