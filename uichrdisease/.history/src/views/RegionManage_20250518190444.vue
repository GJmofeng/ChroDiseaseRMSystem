<template>
  <div class="region-manage-tree">
    <div class="top-bar">
      <el-input v-model="searchName" placeholder="请输入行政区名称" style="width: 200px; margin-right: 10px;" />
      <el-button type="primary" @click="fetchTree">查询</el-button>
      <el-button @click="resetSearch">重置</el-button>
      <el-button type="primary" @click="onAddRoot">新建</el-button>
    </div>
    <div class="main-content">
      <el-table
        :data="treeData"
        style="width: 100%"
        row-key="id"
        border
        default-expand-all
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      >
        <el-table-column prop="dname" label="行政区名称" min-width="200">
          <template #default="scope">
            <span class="region-name" :style="{ paddingLeft: scope.row._level ? (scope.row._level * 20) + 'px' : '0px' }">
              {{ scope.row.dname }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="level" label="级别" width="120">
          <template #default="scope">
            <el-tag :type="getLevelType(scope.row.level)" size="small">
              {{ getLevelLabel(scope.row.level) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="80" align="center" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="updateTime" label="更新时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" link @click="onAddChild(scope.row)">添加下级</el-button>
            <el-button type="primary" link @click="onEdit(scope.row)">编辑</el-button>
            <el-button type="danger" link @click="onDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 新建/编辑弹窗 -->
    <el-dialog v-model="showDialog" :title="dialogTitle" width="500px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="上级部门" prop="parent" v-if="!isEdit">
          <div class="parent-select" @click="showParentSelect = true">
            <el-input 
              v-model="selectedParentName" 
              readonly 
              placeholder="请选择上级部门"
              :class="{ 'is-required': !isEdit && !form.parent }"
            >
              <template #suffix>
                <i class="el-icon-arrow-down"></i>
              </template>
            </el-input>
          </div>
        </el-form-item>
        <el-form-item label="部门排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" style="width: 100%" placeholder="值越大越靠前！" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="onSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 选择上级部门弹窗 -->
    <el-dialog v-model="showParentSelect" title="选择上级部门" width="500px" :close-on-click-modal="false">
      <div class="parent-tree-container">
        <el-input
          v-model="parentSearchKey"
          placeholder="请输入部门名称"
          clearable
          class="parent-search"
        >
          <template #prefix>
            <i class="el-icon-search"></i>
          </template>
        </el-input>
        <el-tree
          ref="parentTreeRef"
          :data="treeData"
          node-key="id"
          :props="{ children: 'children', label: 'dname' }"
          highlight-current
          :expand-on-click-node="false"
          :filter-node-method="filterNode"
          @node-click="handleParentSelect"
        />
      </div>
      <template #footer>
        <el-button @click="showParentSelect = false">取消</el-button>
        <el-button type="primary" @click="confirmParentSelect">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const searchName = ref('')
const treeData = ref([])
const showDialog = ref(false)
const dialogTitle = ref('新建行政区')
const form = ref({ id: null, dname: '', level: '', sort: 0, parent: 0 })
const formRef = ref()
const isEdit = ref(false)
const showParentSelect = ref(false)
const parentSearchKey = ref('')
const selectedParentName = ref('')
const selectedParent = ref(null)
const parentTreeRef = ref(null)

const rules = {
  dname: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  level: [{ required: true, message: '请选择级别', trigger: 'change' }]
}

function getLevelType(level) {
  const types = {
    'province': 'success',
    'city': 'warning',
    'district': 'info'
  }
  return types[level] || 'info'
}

function getLevelLabel(level) {
  const labels = {
    'province': '省级',
    'city': '市级',
    'district': '区县级'
  }
  return labels[level] || level
}

function fetchTree() {
  axios.get('/division/tree', { params: { name: searchName.value } }).then(res => {
    // 添加层级信息
    const addLevel = (data, level = 0) => {
      return data.map(item => {
        item._level = level
        if (item.children && item.children.length) {
          item.children = addLevel(item.children, level + 1)
        }
        return item
      })
    }
    treeData.value = addLevel(res.data.data)
  })
}

function resetSearch() {
  searchName.value = ''
  fetchTree()
}

function onAddRoot() {
  isEdit.value = false
  dialogTitle.value = '添加部门'
  form.value = { id: null, dname: '', level: '', sort: 0, parent: 0 }
  selectedParentName.value = ''
  selectedParent.value = null
  showDialog.value = true
}

function onAddChild(row) {
  isEdit.value = false
  dialogTitle.value = '添加部门'
  form.value = { id: null, dname: '', level: '', sort: 0, parent: row.id }
  selectedParentName.value = row.dname
  selectedParent.value = row
  showDialog.value = true
}

function onEdit(row) {
  isEdit.value = true
  dialogTitle.value = '编辑行政区'
  form.value = { ...row }
  showDialog.value = true
}

function onDelete(row) {
  ElMessageBox.confirm('确定要删除该行政区吗？', '提示', { type: 'warning' }).then(async () => {
    await axios.post('/division/delete', { id: row.id })
    ElMessage.success('删除成功')
    fetchTree()
  })
}

function onSubmit() {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    if (isEdit.value) {
      await axios.post('/division/update', form.value)
      ElMessage.success('编辑成功')
    } else {
      await axios.post('/division/add', form.value)
      ElMessage.success('添加成功')
    }
    showDialog.value = false
    fetchTree()
  })
}

// 监听父级搜索关键字变化
watch(parentSearchKey, (val) => {
  parentTreeRef.value?.filter(val)
})

// 过滤节点方法
function filterNode(value, data) {
  if (!value) return true
  return data.dname.toLowerCase().includes(value.toLowerCase())
}

// 处理父级选择
function handleParentSelect(data) {
  selectedParent.value = data
  selectedParentName.value = data.dname
}

// 确认父级选择
function confirmParentSelect() {
  if (selectedParent.value) {
    form.value.parent = selectedParent.value.id
  }
  showParentSelect.value = false
}

onMounted(fetchTree)
</script>

<style scoped>
.region-manage-tree {
  padding: 20px;
}
.top-bar {
  margin-bottom: 16px;
}
.main-content {
  background: #fff;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}
.region-name {
  display: inline-flex;
  align-items: center;
  font-weight: 500;
  transition: padding-left 0.3s ease;
}
:deep(.el-table__row) {
  cursor: pointer;
}
:deep(.el-table__indent) {
  padding-left: 0 !important;
}
:deep(.el-table__expand-icon) {
  width: 20px;
  height: 20px;
  margin-right: 8px;
  position: relative;
  cursor: pointer;
  transition: transform 0.2s ease-in-out;
}
:deep(.el-table__expand-icon .el-icon) {
  font-size: 12px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  color: #606266;
}
:deep(.el-table__expand-icon--expanded) {
  transform: rotate(90deg);
}
:deep(.el-table .cell) {
  white-space: nowrap;
}
:deep(.el-tag) {
  border-radius: 4px;
}
.parent-select {
  cursor: pointer;
}
.parent-select :deep(.el-input__inner) {
  cursor: pointer;
}
.parent-tree-container {
  max-height: 400px;
  overflow-y: auto;
}
.parent-search {
  margin-bottom: 15px;
}
.is-required :deep(.el-input__inner) {
  border-color: #f56c6c;
}
.is-required :deep(.el-input__inner:hover) {
  border-color: #f56c6c;
}
.is-required :deep(.el-input__inner:focus) {
  border-color: #f56c6c;
  box-shadow: 0 0 0 1px rgba(245, 108, 108, 0.2);
}
</style>