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
          <el-button circle @click="onRefresh">
            <IconRefresh style="width:22px;height:22px;color:#2563eb;" />
          </el-button>
          <el-button circle @click="showColumnSetting = true">
            <IconLayoutGridAdd style="width:22px;height:22px;color:#2563eb;" />
          </el-button>
        </div>
      </div>
      <!-- 表格区 -->
      <div class="table-container">
        <el-table
          :data="filteredUsers"
          border
          style="width: 100%"
          @selection-change="handleSelectionChange"
          height="420"
        >
          <el-table-column type="selection" width="50" fixed />
          <template v-for="col in columnSettings.filter(c => c.visible)" :key="col.prop">
            <el-table-column
              v-if="col.prop === 'id'"
              :prop="col.prop"
              :label="col.label"
              :width="col.width"
              :fixed="col.fixed"
            />
            <el-table-column
              v-else-if="col.prop === 'operation'"
              :label="col.label"
              :width="col.width"
              :fixed="col.fixed"
              align="center"
            >
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
            <el-table-column
              v-else
              :prop="col.prop"
              :label="col.label"
              :fixed="col.fixed"
            >
              <template #default="scope" v-if="col.prop === 'password'">
                <span>******</span>
              </template>
            </el-table-column>
          </template>
        </el-table>
      </div>
      <!-- 分页 -->
      <div class="flex justify-between items-center mt-4">
        <div>共{{ total }}条</div>
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="total"
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
      <draggable v-model="columnSettings" item-key="prop" :animation="200" handle=".drag-handle">
        <template #item="{ element, index }">
          <div class="setting-row" style="display:flex;align-items:center;padding:8px 0;border-bottom:1px solid #f5f5f5;">
            <el-checkbox v-model="element.visible" style="margin-right:8px;" />
            <span class="drag-handle" style="cursor:move;margin-right:8px;">⠿</span>
            <span style="flex:1;">{{ element.label }}</span>
            <el-input-number v-model="element.width" :min="50" :max="400" size="small" style="margin:0 8px;width:90px;" />
            <el-link type="primary" @click="moveUp(index)">上移</el-link>
            <el-link type="primary" @click="moveDown(index)">下移</el-link>
          </div>
        </template>
      </draggable>
      <template #footer>
        <el-button @click="showColumnSetting = false">取消</el-button>
        <el-button type="primary" @click="saveColumnSetting">保存</el-button>
        <el-button type="danger" @click="resetColumnSetting">恢复默认</el-button>
      </template>
    </el-dialog>
    <!-- 添加成员弹窗 -->
    <el-drawer
      v-model="showAddUser"
      title="添加成员"
      direction="rtl"
      size="400px"
      :close-on-click-modal="false"
      :with-header="true"
      :destroy-on-close="true"
    >
      <el-form
        :model="addUserForm"
        :rules="addUserRules"
        ref="addUserFormRef"
        label-width="80px"
        class="add-user-form"
      >
        <el-form-item label="登录账号" prop="userid">
          <el-input v-model="addUserForm.userid" placeholder="请输入登录账号" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="addUserForm.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="姓名" prop="fullname">
          <el-input v-model="addUserForm.fullname" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-input v-model="addUserForm.role" placeholder="请输入角色" />
        </el-form-item>
        <el-form-item>
          <el-button @click="showAddUser = false">取消</el-button>
          <el-button type="primary" @click="handleAddUser">确定</el-button>
        </el-form-item>
      </el-form>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import draggable from 'vuedraggable'
import TabMenu from '../components/TabMenu.vue'
import { IconRefresh, IconLayoutGridAdd } from '@tabler/icons-vue'
import axios from 'axios'

const router = useRouter()

const users = ref([])
const total = ref(0)

async function fetchUsers() {
  try {
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      search: searchKey.value,
      status: statusFilter.value === '全部' ? '' : statusFilter.value
    }
    const res = await axios.get('/user/list', { params })
    users.value = res.data.data || res.data
    total.value = res.data.total || users.value.length
  } catch (e) {
    ElMessage.error('获取用户数据失败')
  }
}

onMounted(() => {
  fetchUsers()
})

const statusFilter = ref('全部')
const searchKey = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const multipleSelection = ref([])

const filteredUsers = computed(() => users.value)

function handleSelectionChange(val) {
  multipleSelection.value = val
}

// 添加成员弹窗相关
const showAddUser = ref(false)
const addUserForm = ref({
  userid: '',
  password: '',
  fullname: '',
  role: ''
})
const addUserRules = {
  userid: [ { required: true, message: '请输入登录账号', trigger: 'blur' } ],
  password: [ { required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, message: '密码至少6位', trigger: 'blur' } ],
  fullname: [ { required: true, message: '请输入姓名', trigger: 'blur' } ],
  role: [ { required: true, message: '请输入角色', trigger: 'blur' } ]
}
const addUserFormRef = ref()

function onAddUser() {
  showAddUser.value = true
  addUserForm.value = { userid: '', password: '', fullname: '', role: '' }
}

async function handleAddUser() {
  addUserFormRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      await axios.post('/user/add', addUserForm.value)
      ElMessage.success('添加成功')
      showAddUser.value = false
      fetchUsers()
    } catch (e) {
      ElMessage.error('添加失败')
    }
  })
}

function onAdjustDept() {
  ElMessage.info('调整部门功能待实现')
}

function onBatchDelete() {
  if (!multipleSelection.value.length) {
    ElMessage.warning('请先选择要删除的成员')
    return
  }
  ElMessageBox.confirm('确定要删除选中的成员吗？', '提示', {
    type: 'warning',
  }).then(async () => {
    const ids = multipleSelection.value.map(u => u.id)
    console.log('删除这些id:', ids)
    try {
      await axios.post('/user/deleteBatch', ids)
      ElMessage.success('删除成功')
      fetchUsers()
      multipleSelection.value = []
    } catch (e) {
      ElMessage.error('删除失败')
    }
  })
}
function onSearch() {
  currentPage.value = 1
  fetchUsers()
}
function onReset() {
  statusFilter.value = '全部'
  searchKey.value = ''
  currentPage.value = 1
  fetchUsers()
}
function onRefresh() {
  fetchUsers()
  ElMessage.success('已刷新')
}
function handleSizeChange(size) {
  pageSize.value = size
  currentPage.value = 1
  fetchUsers()
}
function handlePageChange(page) {
  currentPage.value = page
  fetchUsers()
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

const showColumnSetting = ref(false)
const defaultColumnSettings = [
  { label: 'ID', prop: 'id', width: 60, visible: true },
  { label: '登录账号', prop: 'userid', visible: true },
  { label: '密码', prop: 'password', visible: true },
  { label: '姓名', prop: 'fullname', visible: true },
  { label: '角色', prop: 'role', visible: true },
  { 
    label: '操作', 
    prop: 'operation', 
    width: 200, 
    visible: true, 
    fixed: 'right'
  }
]

// 合并localStorage和defaultColumnSettings，确保新加的列能自动补全
function mergeColumnSettings(defaults, saved) {
  const map = new Map(saved.map(c => [c.prop, c]))
  // 保留顺序：先用已保存的顺序，再补充新加的列
  const merged = []
  for (const def of defaults) {
    if (map.has(def.prop)) {
      merged.push({ ...def, ...map.get(def.prop) })
      map.delete(def.prop)
    } else {
      merged.push({ ...def })
    }
  }
  return merged
}

const savedCols = JSON.parse(localStorage.getItem('userTableColumns') || '[]')
const columnSettings = ref(
  savedCols.length ? mergeColumnSettings(defaultColumnSettings, savedCols) : JSON.parse(JSON.stringify(defaultColumnSettings))
)

// 监听列设置变化，自动保存到localStorage
function saveColumnSetting() {
  localStorage.setItem('userTableColumns', JSON.stringify(columnSettings.value))
  ElMessage.success('已保存列设置')
  showColumnSetting.value = false
}

// 重置列设置时同时清除localStorage中的设置
function resetColumnSetting() {
  columnSettings.value = JSON.parse(JSON.stringify(defaultColumnSettings))
  localStorage.removeItem('userTableColumns')
  ElMessage.success('已恢复默认')
}

function moveUp(index) {
  if (index > 0) {
    const temp = columnSettings.value[index]
    columnSettings.value.splice(index, 1)
    columnSettings.value.splice(index - 1, 0, temp)
  }
}
function moveDown(index) {
  if (index < columnSettings.value.length - 1) {
    const temp = columnSettings.value[index]
    columnSettings.value.splice(index, 1)
    columnSettings.value.splice(index + 1, 0, temp)
  }
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
.table-container {
  width: 100%;
  overflow-x: auto;
}
.el-table {
  width: 100% !important;
  table-layout: fixed;
}
.el-table__body {
  width: 100% !important;
}
.el-table__header {
  width: 100% !important;
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
/* 移除右侧弹出对话框的自定义样式（el-drawer自带右侧滑出效果） */
.right-dialog, .right-dialog .el-dialog, .right-dialog .el-overlay-dialog, .right-dialog .el-dialog__header, .right-dialog .el-dialog__body, .right-dialog .el-dialog__footer, .right-dialog .el-dialog__title, .right-dialog .el-dialog__headerbtn, .right-dialog .el-overlay, .el-overlay-dialog, .el-overlay-dialog-leaving {
  all: unset;
}
.add-user-form {
  padding: 20px 0;
}

.add-user-form :deep(.el-form-item__label) {
  font-weight: 500;
  color: #333;
}

.add-user-form :deep(.el-input__wrapper) {
  box-shadow: 0 0 0 1px #dcdfe6 inset;
}

.add-user-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #409eff inset;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style> 