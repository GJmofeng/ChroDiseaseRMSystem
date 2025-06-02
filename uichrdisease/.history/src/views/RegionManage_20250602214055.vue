<template>
  <div class="region-manage-page">
    <div class="card">
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
      <el-dialog v-model="showDialog" :title="dialogTitle" width="400px" :close-on-click-modal="false">
        <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
          <el-form-item label="上级行政区" prop="parent" v-if="!isEdit">
            <el-select
              v-model="form.parent"
              placeholder="请选择上级行政区"
              style="width: 100%"
              filterable
              :filter-method="filterParentOptions"
              @visible-change="handleDropdownVisible"
              @change="handleParentChange"
              popper-class="region-select-dropdown"
            >
              <el-option
                v-for="item in selectOptions"
                :key="item.id"
                :label="item.dname"
                :value="item.id"
                :disabled="!item._allowSelect"
              >
                <span :style="{ 
                  paddingLeft: (item._level * 20) + 'px',
                  color: item._allowSelect ? '' : '#999'
                }">{{ item.dname }}</span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="名称" prop="dname">
            <el-input v-model="form.dname" placeholder="请输入名称" />
          </el-form-item>
          <el-form-item label="级别" prop="level">
            <el-select v-model="form.level" placeholder="请选择">
              <el-option label="省级" value="province" />
              <el-option label="市级" value="city" />
              <el-option label="区县级" value="district" />
              <el-option label="乡镇级" value="town" />
              <el-option label="村级" value="village" />
              <el-option label="街区" value="street" />
              <el-option label="屯级" value="hamlet" />
            </el-select>
          </el-form-item>
          <el-form-item label="排序" prop="sort">
            <el-input-number v-model="form.sort" :min="0" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showDialog = false">取消</el-button>
          <el-button type="primary" @click="onSubmit">确定</el-button>
        </template>
      </el-dialog>

      <!-- 选择上级部门弹窗 -->
      <el-dialog v-model="showParentSelect" title="选择上级部门" width="400px" :close-on-click-modal="false">
        <el-input
          v-model="parentSearchKey"
          placeholder="请输入部门名称"
          clearable
          style="margin-bottom: 15px"
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
        <template #footer>
          <el-button @click="showParentSelect = false">取消</el-button>
          <el-button type="primary" @click="confirmParentSelect">确定</el-button>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDivisionTree, addDivision, updateDivision, deleteDivision } from '@/api/division'
import { useRouter } from 'vue-router'

const router = useRouter()

// 原有的代码
const searchName = ref('')
const treeData = ref([])
const showDialog = ref(false)
const dialogTitle = ref('新建行政区')
const form = ref({ id: null, dname: '', level: '', sort: 0, parent: 0 })
const formRef = ref()
const isEdit = ref(false)
const filterKeyword = ref('')

// 上级部门选择相关
const showParentSelect = ref(false)
const parentSearchKey = ref('')
const selectedParentName = ref('')
const selectedParent = ref(null)
const parentTreeRef = ref(null)

const rules = {
  dname: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  level: [{ required: true, message: '请选择级别', trigger: 'change' }],
  parent: [{ required: true, message: '请选择上级行政区', trigger: 'change' }]
}

function getLevelType(level) {
  const types = {
    'province': 'success',
    'city': 'warning',
    'district': 'info',
    'town': 'primary',
    'village': 'success',
    'street': 'warning',
    'hamlet': 'info'
  }
  return types[level] || 'info'
}

function getLevelLabel(level) {
  const labels = {
    'province': '省级',
    'city': '市级',
    'district': '区县级',
    'town': '乡镇级',
    'village': '村级',
    'street': '街区',
    'hamlet': '屯级'
  }
  return labels[level] || level
}

async function fetchTree() {
  try {
    const res = await getDivisionTree(searchName.value)
    if (res.code === 200) {
      // 添加层级信息
      const addLevel = (data, level = 0) => {
        if (!Array.isArray(data)) return []
        return data.map(item => ({
          ...item,
          _level: level,
          children: item.children ? addLevel(item.children, level + 1) : []
        }))
      }
      
      treeData.value = addLevel(res.data)
    } else {
      ElMessage.error(res.msg || '获取数据失败')
    }
  } catch (error) {
    console.error('获取树形数据失败:', error)
    ElMessage.error('获取数据失败')
  }
}

function resetSearch() {
  searchName.value = ''
  fetchTree()
}

async function onAddRoot() {
  isEdit.value = false
  dialogTitle.value = '新建行政区'
  form.value = { 
    id: null, 
    dname: '', 
    level: '', 
    sort: 0, 
    parent: 0 
  }
  selectedParentName.value = ''
  selectedParent.value = null
  filterKeyword.value = ''
  
  // 确保数据已加载
  if (!treeData.value || !treeData.value.length) {
    await fetchTree()
  }
  
  showDialog.value = true
}

function onAddChild(row) {
  isEdit.value = false
  dialogTitle.value = '添加下级行政区'
  form.value = { 
    id: null, 
    dname: '', 
    level: '', 
    sort: 0, 
    parent: row.id 
  }
  
  // 根据当前行的级别设置下级级别
  switch (row.level) {
    case 'province':
      form.value.level = 'city'
      break
    case 'city':
      form.value.level = 'district'
      break
    case 'district':
      form.value.level = 'town'
      break
    case 'town':
      form.value.level = 'village'
      break
    case 'village':
      form.value.level = 'hamlet'
      break
    case 'street':
      form.value.level = 'hamlet'
      break
  }
  
  selectedParentName.value = row.dname
  selectedParent.value = row
  
  // 确保数据已加载
  if (!treeData.value.length) {
    fetchTree()
  }
  showDialog.value = true
}

function onEdit(row) {
  isEdit.value = true
  dialogTitle.value = '编辑行政区'
  form.value = { ...row }
  selectedParentName.value = row.parent ? row.parent.dname : ''
  selectedParent.value = row.parent
  showDialog.value = true
}

async function onDelete(row) {
  try {
    await ElMessageBox.confirm('确定要删除该行政区吗？', '提示', {
      type: 'warning'
    })
    
    const res = await deleteDivision(row.id)
    if (res.code === 200) {
      ElMessage.success(res.msg)
      fetchTree()
    } else {
      ElMessage.error(res.msg || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

async function onSubmit() {
  try {
    await formRef.value.validate()
    
    const api = isEdit.value ? updateDivision : addDivision
    const res = await api(form.value)
    
    if (res.code === 200) {
      ElMessage.success(res.msg)
      showDialog.value = false
      fetchTree()
    } else if (res.code === 205) {
      ElMessage.warning('该区域已存在')
    } else {
      ElMessage.error(res.msg || (isEdit.value ? '编辑失败' : '添加失败'))
    }
  } catch (error) {
    console.error(isEdit.value ? '编辑失败:' : '添加失败:', error)
    ElMessage.error(isEdit.value ? '编辑失败' : '添加失败')
  }
}

// 监听对话框显示状态
watch(showDialog, async (visible) => {
  if (visible) {
    console.log('对话框显示，检查数据状态...')
    if (!treeData.value || !treeData.value.length) {
      console.log('treeData为空，开始获取数据...')
      await fetchTree()
    } else {
      console.log('已有数据，无需重新获取')
    }
  }
})

// 在组件挂载时获取数据
onMounted(() => {
  console.log('组件挂载，开始获取初始数据...')
  fetchTree()
})

// 监听搜索关键字变化
watch(searchName, (val) => {
  console.log('搜索关键字变化:', val)
  fetchTree()
})

// 修改 flattenTreeData 函数，优化层级关系处理
function flattenTreeData(data, level = 0, parentLevel = null, parentId = null) {
  if (!Array.isArray(data)) {
    console.warn('flattenTreeData接收到非数组数据:', data)
    return []
  }
  
  let result = []
  data.forEach(item => {
    if (!item) {
      console.warn('存在无效的数据项')
      return
    }
    
    // 根据父级类型确定当前项可以选择的级别
    let allowSelect = true
    if (parentLevel) {
      switch (parentLevel) {
        case 'province':
          if (item.level !== 'city') allowSelect = false
          break
        case 'city':
          if (item.level !== 'district') allowSelect = false
          break
        case 'district':
          if (item.level !== 'town') allowSelect = false
          break
        case 'town':
          if (item.level !== 'village') allowSelect = false
          break
        case 'village':
          if (item.level !== 'hamlet') allowSelect = false
          break
        case 'street':
          if (item.level !== 'hamlet') allowSelect = false
          break
        case 'hamlet':
          allowSelect = false
          break
      }
    }
    
    const newItem = { 
      ...item, 
      _level: level,
      _allowSelect: allowSelect,
      _parentId: parentId,
      _label: ''.padStart(level * 4, ' ') + item.dname
    }
    
    result.push(newItem)
    
    if (item.children && Array.isArray(item.children) && item.children.length) {
      result = result.concat(flattenTreeData(item.children, level + 1, item.level, item.id))
    }
  })
  return result
}

// 修改 selectOptions 计算属性
const selectOptions = computed(() => {
  console.log('开始计算selectOptions...')
  console.log('当前treeData:', treeData.value)
  
  if (!treeData.value || !Array.isArray(treeData.value)) {
    console.warn('treeData不是有效数组:', treeData.value)
    return []
  }

  const flatData = flattenTreeData(treeData.value)
  console.log('扁平化后的数据:', flatData)
  
  // 如果有搜索关键字，进行过滤
  return flatData
    .filter(item => {
      if (filterKeyword.value) {
        return item.dname.toLowerCase().includes(filterKeyword.value.toLowerCase())
      }
      return true
    })
    .sort((a, b) => {
      // 首先按层级排序
      if (a._level !== b._level) {
        return a._level - b._level
      }
      // 然后按排序值排序
      return (a.sort || 0) - (b.sort || 0)
    })
})

// 修改 handleParentChange 函数
function handleParentChange(value) {
  const selectedParent = findParentById(treeData.value, value)
  if (selectedParent) {
    // 根据上级部门级别设置当前部门级别
    switch (selectedParent.level) {
      case 'province':
        form.value.level = 'city'
        break
      case 'city':
        form.value.level = 'district'
        break
      case 'district':
        form.value.level = 'town'
        break
      case 'town':
        form.value.level = 'village'
        break
      case 'village':
        form.value.level = 'hamlet'
        break
      case 'street':
        form.value.level = 'hamlet'
        break
      default:
        form.value.level = ''
    }
    
    // 设置父级ID
    form.value.parent = selectedParent.id
  }
}

// 递归查找父级部门
function findParentById(data, id) {
  if (!Array.isArray(data)) return null
  for (const item of data) {
    if (item.id === id) return item
    if (item.children) {
      const found = findParentById(item.children, id)
      if (found) return found
    }
  }
  return null
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

// 处理下拉框显示/隐藏
async function handleDropdownVisible(visible) {
  console.log('下拉框显示状态改变:', visible)
  if (visible) {
    filterKeyword.value = ''
    // 确保有数据
    if (!treeData.value || !treeData.value.length) {
      console.log('下拉框打开时发现数据为空，开始获取数据...')
      await fetchTree()
    }
    console.log('当前树形数据:', treeData.value)
    console.log('当前selectOptions:', selectOptions.value)
  }
}

// 过滤选项
function filterParentOptions(query) {
  filterKeyword.value = query
}
</script>

<style scoped>
.region-manage-page {
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
:deep(.el-input__inner[readonly]) {
  cursor: pointer;
  background-color: #fff;
}

/* 下拉菜单样式优化 */
:deep(.el-select-dropdown) {
  max-height: 400px;
}

:deep(.el-select-dropdown.el-popper) {
  min-width: 300px !important;
}

:deep(.el-select-dropdown__item) {
  height: auto !important;
  line-height: 26px !important;
  padding: 5px 20px !important;
  position: relative !important;
}

:deep(.el-select-dropdown__item span) {
  display: block;
  padding-left: var(--indent, 0px);
}

:deep(.el-select-group__wrap) {
  margin: 0 !important;
  padding: 0 !important;
}

:deep(.el-select-group__title) {
  padding: 8px 20px !important;
  font-size: 13px;
  font-weight: bold;
  color: #606266;
  background-color: #f5f7fa;
  position: sticky;
  top: 0;
  z-index: 1;
}

:deep(.el-select-group) {
  margin: 0 !important;
  padding: 0 !important;
}

:deep(.el-select-group__wrap:not(:last-of-type)) {
  margin-bottom: 0 !important;
}

:deep(.el-select-group__wrap:not(:first-of-type)) {
  margin-top: 0 !important;
}

:deep(.el-popper.is-light) {
  border: 1px solid #dcdfe6;
}

:deep(.el-select-dropdown.is-multiple .el-select-dropdown__item) {
  padding-right: 40px !important;
}

:deep(.el-select-dropdown__list) {
  padding: 0 !important;
  margin: 0 !important;
}

:deep(.el-select-dropdown__item.selected) {
  font-weight: bold;
  color: var(--el-color-primary);
}

:deep(.el-select-dropdown__item.hover) {
  background-color: var(--el-fill-color-light);
}

:deep(.el-select-dropdown__empty) {
  padding: 10px 20px;
  margin: 0;
  text-align: center;
  color: #999;
  font-size: 14px;
}
</style>

<style>
/* 全局样式，确保下拉菜单样式生效 */
.region-select-dropdown {
  min-width: 300px !important;
}

.region-select-dropdown .el-select-dropdown__item {
  height: auto !important;
  line-height: 26px !important;
  padding: 5px 20px !important;
}

.region-select-dropdown .el-select-dropdown__item span {
  white-space: pre !important;
  font-family: monospace;
  display: block;
  line-height: 1.5 !important;
}

.region-select-dropdown .el-select-dropdown__item.is-disabled span {
  color: #999;
}
</style>