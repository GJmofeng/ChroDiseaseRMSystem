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
            <span class="region-name">
              <i :class="['fas', scope.row.children && scope.row.children.length ? 'fa-folder' : 'fa-folder-open']" 
                style="margin-right: 8px; color: #ffd04b;"></i>
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
    <el-dialog v-model="showDialog" :title="dialogTitle" width="400px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="名称" prop="dname">
          <el-input v-model="form.dname" />
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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const searchName = ref('')
const treeData = ref([])
const showDialog = ref(false)
const dialogTitle = ref('新建行政区')
const form = ref({ id: null, dname: '', level: '', sort: 0, parent: 0 })
const formRef = ref()
const isEdit = ref(false)

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
    treeData.value = res.data.data
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
  showDialog.value = true
}

function onAddChild(row) {
  isEdit.value = false
  dialogTitle.value = '添加下级行政区'
  form.value = { id: null, dname: '', level: '', sort: 0, parent: row.id }
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
  display: flex;
  align-items: center;
  font-weight: 500;
}
:deep(.el-table__row) {
  cursor: pointer;
}
:deep(.el-table__expand-icon) {
  height: 20px;
  width: 20px;
  margin-right: 8px;
}
:deep(.el-table__expand-icon .el-icon) {
  font-size: 12px;
}
:deep(.el-tag) {
  border-radius: 4px;
}
</style>