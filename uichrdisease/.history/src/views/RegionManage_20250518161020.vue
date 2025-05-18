<template>
  <div class="region-manage">
    <div class="header">
      <h2>行政区管理</h2>
      <el-button type="primary" @click="showAddDivision = true">添加行政区</el-button>
    </div>

    <!-- 行政区列表 -->
    <el-table :data="divisions" style="width: 100%" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="dname" label="行政区名称" />
      <el-table-column prop="parent" label="上级行政区ID" />
      <el-table-column prop="level" label="行政区级别" />
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加行政区弹窗 -->
    <el-dialog v-model="showAddDivision" title="添加行政区" width="500px">
      <el-form :model="addDivisionForm" :rules="rules" ref="addDivisionFormRef" label-width="100px">
        <el-form-item label="行政区名称" prop="dname">
          <el-input v-model="addDivisionForm.dname" placeholder="请输入行政区名称" />
        </el-form-item>
        <el-form-item label="上级行政区" prop="parent">
          <el-select v-model="addDivisionForm.parent" placeholder="请选择上级行政区">
            <el-option label="无" :value="0" />
            <el-option v-for="item in divisions" :key="item.id" :label="item.dname" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="行政区级别" prop="level">
          <el-select v-model="addDivisionForm.level" placeholder="请选择行政区级别">
            <el-option label="省级" value="province" />
            <el-option label="市级" value="city" />
            <el-option label="区县级" value="district" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAddDivision = false">取消</el-button>
          <el-button type="primary" @click="handleAddDivision">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑行政区弹窗 -->
    <el-dialog v-model="showEditDivision" title="编辑行政区" width="500px">
      <el-form :model="editDivisionForm" :rules="rules" ref="editDivisionFormRef" label-width="100px">
        <el-form-item label="行政区名称" prop="dname">
          <el-input v-model="editDivisionForm.dname" placeholder="请输入行政区名称" />
        </el-form-item>
        <el-form-item label="上级行政区" prop="parent">
          <el-select v-model="editDivisionForm.parent" placeholder="请选择上级行政区">
            <el-option label="无" :value="0" />
            <el-option v-for="item in divisions" :key="item.id" :label="item.dname" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="行政区级别" prop="level">
          <el-select v-model="editDivisionForm.level" placeholder="请选择行政区级别">
            <el-option label="省级" value="province" />
            <el-option label="市级" value="city" />
            <el-option label="区县级" value="district" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showEditDivision = false">取消</el-button>
          <el-button type="primary" @click="handleEditDivision">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

// 数据列表
const divisions = ref([])

// 表单显示控制
const showAddDivision = ref(false)
const showEditDivision = ref(false)

// 表单数据
const addDivisionForm = ref({
  dname: '',
  parent: 0,
  level: ''
})

const editDivisionForm = ref({
  id: '',
  dname: '',
  parent: 0,
  level: ''
})

// 表单校验规则
const rules = {
  dname: [{ required: true, message: '请输入行政区名称', trigger: 'blur' }],
  level: [{ required: true, message: '请选择行政区级别', trigger: 'change' }]
}

// 表单引用
const addDivisionFormRef = ref(null)
const editDivisionFormRef = ref(null)

// 获取行政区列表
async function fetchDivisions() {
  try {
    const res = await axios.get('/division/list')
    divisions.value = res.data.data
  } catch (e) {
    ElMessage.error('获取行政区列表失败')
  }
}

// 添加行政区
async function handleAddDivision() {
  addDivisionFormRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      await axios.post('/division/add', addDivisionForm.value)
      ElMessage.success('添加成功')
      showAddDivision.value = false
      fetchDivisions()
      // 重置表单
      addDivisionForm.value = {
        dname: '',
        parent: 0,
        level: ''
      }
    } catch (e) {
      ElMessage.error('添加失败')
    }
  })
}

// 编辑行政区
function handleEdit(row) {
  editDivisionForm.value = { ...row }
  showEditDivision.value = true
}

async function handleEditDivision() {
  editDivisionFormRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      await axios.post('/division/update', editDivisionForm.value)
      ElMessage.success('编辑成功')
      showEditDivision.value = false
      fetchDivisions()
    } catch (e) {
      ElMessage.error('编辑失败')
    }
  })
}

// 删除行政区
function handleDelete(row) {
  ElMessageBox.confirm('确定要删除该行政区吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await axios.post('/division/delete', { id: row.id })
      ElMessage.success('删除成功')
      fetchDivisions()
    } catch (e) {
      ElMessage.error('删除失败')
    }
  })
}

// 页面加载时获取数据
onMounted(() => {
  fetchDivisions()
})
</script>

<style scoped>
.region-manage {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style> 