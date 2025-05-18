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
    <el-dialog v-model="showDialog" :title="dialogTitle" width="400px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="上级部门" prop="parent" v-if="!isEdit">
          <el-select
            v-model="form.parent"
            placeholder="请选择上级部门"
            style="width: 100%"
            filterable
            :filter-method="filterParentOptions"
            @visible-change="handleDropdownVisible"
          >
            <el-option-group
              v-for="group in parentOptions"
              :key="group.label"
              :label="group.label"
            >
              <el-option
                v-for="item in group.options"
                :key="item.id"
                :label="item.dname"
                :value="item.id"
                :style="{ paddingLeft: (item._level * 20) + 'px' }"
              >
                <span>{{ item.dname }}</span>
              </el-option>
            </el-option-group>
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
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

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
  parent: [{ required: true, message: '请选择上级部门', trigger: 'change' }]
}

// 将树形数据转换为扁平数组，并添加层级信息
function flattenTreeData(data, level = 0) {
  let result = []
  data.forEach(item => {
    const newItem = { ...item, _level: level }
    result.push(newItem)
    if (item.children && item.children.length) {
      result = result.concat(flattenTreeData(item.children, level + 1))
    }
  })
  return result
}

// 按行政区级别对选项进行分组
const parentOptions = computed(() => {
  console.log('计算parentOptions, treeData:', treeData.value) // 添加日志
  const flatData = flattenTreeData(treeData.value || [])
  console.log('扁平化数据:', flatData) // 添加日志
  
  const groups = {
    province: { label: '省级', options: [] },
    city: { label: '市级', options: [] },
    district: { label: '区县级', options: [] }
  }
  
  flatData.forEach(item => {
    if (groups[item.level]) {
      if (filterKeyword.value) {
        if (item.dname.toLowerCase().includes(filterKeyword.value.toLowerCase())) {
          groups[item.level].options.push(item)
        }
      } else {
        groups[item.level].options.push(item)
      }
    }
  })

  const result = Object.values(groups).filter(group => group.options.length > 0)
  console.log('分组结果:', result) // 添加日志
  return result
})

// 处理下拉框显示/隐藏
function handleDropdownVisible(visible) {
  if (visible) {
    filterKeyword.value = ''
  }
}

// 过滤选项
function filterParentOptions(query) {
  filterKeyword.value = query
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
    console.log('树形数据:', treeData.value) // 添加日志查看数据
  })
}

function resetSearch() {
  searchName.value = ''
  fetchTree()
}

function onAddRoot() {
  isEdit.value = false
  dialogTitle.value = '新建行政区'
  form.value = { id: null, dname: '', level: '', sort: 0, parent: 0 }
  // 确保数据已加载
  if (!treeData.value.length) {
    fetchTree()
  }
  showDialog.value = true
}

function onAddChild(row) {
  isEdit.value = false
  dialogTitle.value = '添加下级行政区'
  form.value = { id: null, dname: '', level: '', sort: 0, parent: row.id }
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

// 监听对话框显示状态
watch(showDialog, (visible) => {
  if (visible && !treeData.value.length) {
    fetchTree()
  }
})

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
:deep(.el-input__inner[readonly]) {
  cursor: pointer;
  background-color: #fff;
}
:deep(.el-select-dropdown__item) {
  padding-left: 0 !important;
}
:deep(.el-select-group__wrap:not(:last-of-type)) {
  padding-bottom: 0;
}
:deep(.el-select-group__title) {
  font-weight: bold;
  background-color: #f5f7fa;
  padding: 8px 12px;
  font-size: 13px;
  color: #606266;
}
</style>