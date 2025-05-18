<template>
  <div class="region-manage-tree">
    <div class="top-bar">
      <el-input v-model="searchName" placeholder="请输入行政区名称" style="width: 200px; margin-right: 10px;" />
      <el-button type="primary" @click="fetchTree">查询</el-button>
      <el-button @click="resetSearch">重置</el-button>
      <el-button type="primary" @click="onAddRoot">新建</el-button>
    </div>
    <div class="main-content">
      <el-tree
        :data="treeData"
        :props="treeProps"
        node-key="id"
        highlight-current
        @node-click="onTreeNodeClick"
        class="region-tree"
        style="width: 300px;"
      />
      <el-table :data="tableData" style="flex:1; margin-left: 24px;" border>
        <el-table-column prop="dname" label="行政区名称" />
        <el-table-column prop="level" label="级别" />
        <el-table-column prop="sort" label="排序" />
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column prop="updateTime" label="更新时间" />
        <el-table-column label="操作" width="200">
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
const tableData = ref([])
const currentNode = ref(null)
const showDialog = ref(false)
const dialogTitle = ref('新建行政区')
const form = ref({ id: null, dname: '', level: '', sort: 0, parent: 0 })
const formRef = ref()
const isEdit = ref(false)

const treeProps = { children: 'children', label: 'dname' }

const rules = {
  dname: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  level: [{ required: true, message: '请选择级别', trigger: 'change' }]
}

function fetchTree() {
  axios.get('/division/tree', { params: { name: searchName.value } }).then(res => {
    treeData.value = res.data.data
    // 默认选中第一个节点
    if (treeData.value.length) {
      onTreeNodeClick(treeData.value[0])
    } else {
      tableData.value = []
    }
  })
}

function onTreeNodeClick(node) {
  currentNode.value = node
  axios.get('/division/children', { params: { parent: node.id } }).then(res => {
    tableData.value = res.data.data
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
  display: flex;
  align-items: flex-start;
}
.region-tree {
  background: #fff;
  border-radius: 6px;
  padding: 12px;
  min-height: 400px;
}
</style>