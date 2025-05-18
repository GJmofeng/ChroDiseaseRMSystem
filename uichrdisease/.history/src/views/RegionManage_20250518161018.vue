<template>
  <div class="region-manage">
    <div class="header">
      <h2>行政区管理</h2>
      <el-button type="primary" @click="showAddRegion = true">添加行政区</el-button>
    </div>

    <!-- 行政区列表 -->
    <el-table :data="regions" style="width: 100%" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="行政区名称" />
      <el-table-column prop="code" label="行政区编码" />
      <el-table-column prop="parentId" label="上级行政区ID" />
      <el-table-column prop="level" label="行政区级别" />
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加行政区弹窗 -->
    <el-dialog v-model="showAddRegion" title="添加行政区" width="500px">
      <el-form :model="addRegionForm" :rules="rules" ref="addRegionFormRef" label-width="100px">
        <el-form-item label="行政区名称" prop="name">
          <el-input v-model="addRegionForm.name" placeholder="请输入行政区名称" />
        </el-form-item>
        <el-form-item label="行政区编码" prop="code">
          <el-input v-model="addRegionForm.code" placeholder="请输入行政区编码" />
        </el-form-item>
        <el-form-item label="上级行政区" prop="parentId">
          <el-select v-model="addRegionForm.parentId" placeholder="请选择上级行政区">
            <el-option label="无" :value="0" />
            <el-option v-for="item in regions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="行政区级别" prop="level">
          <el-select v-model="addRegionForm.level" placeholder="请选择行政区级别">
            <el-option label="省级" value="province" />
            <el-option label="市级" value="city" />
            <el-option label="区县级" value="district" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAddRegion = false">取消</el-button>
          <el-button type="primary" @click="handleAddRegion">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑行政区弹窗 -->
    <el-dialog v-model="showEditRegion" title="编辑行政区" width="500px">
      <el-form :model="editRegionForm" :rules="rules" ref="editRegionFormRef" label-width="100px">
        <el-form-item label="行政区名称" prop="name">
          <el-input v-model="editRegionForm.name" placeholder="请输入行政区名称" />
        </el-form-item>
        <el-form-item label="行政区编码" prop="code">
          <el-input v-model="editRegionForm.code" placeholder="请输入行政区编码" />
        </el-form-item>
        <el-form-item label="上级行政区" prop="parentId">
          <el-select v-model="editRegionForm.parentId" placeholder="请选择上级行政区">
            <el-option label="无" :value="0" />
            <el-option v-for="item in regions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="行政区级别" prop="level">
          <el-select v-model="editRegionForm.level" placeholder="请选择行政区级别">
            <el-option label="省级" value="province" />
            <el-option label="市级" value="city" />
            <el-option label="区县级" value="district" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showEditRegion = false">取消</el-button>
          <el-button type="primary" @click="handleEditRegion">确定</el-button>
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
const regions = ref([])

// 表单显示控制
const showAddRegion = ref(false)
const showEditRegion = ref(false)

// 表单数据
const addRegionForm = ref({
  name: '',
  code: '',
  parentId: 0,
  level: ''
})

const editRegionForm = ref({
  id: '',
  name: '',
  code: '',
  parentId: 0,
  level: ''
})

// 表单校验规则
const rules = {
  name: [{ required: true, message: '请输入行政区名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入行政区编码', trigger: 'blur' }],
  level: [{ required: true, message: '请选择行政区级别', trigger: 'change' }]
}

// 表单引用
const addRegionFormRef = ref(null)
const editRegionFormRef = ref(null)

// 获取行政区列表
async function fetchRegions() {
  try {
    const res = await axios.get('/region/list')
    regions.value = res.data.data
  } catch (e) {
    ElMessage.error('获取行政区列表失败')
  }
}

// 添加行政区
async function handleAddRegion() {
  addRegionFormRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      await axios.post('/region/add', addRegionForm.value)
      ElMessage.success('添加成功')
      showAddRegion.value = false
      fetchRegions()
      // 重置表单
      addRegionForm.value = {
        name: '',
        code: '',
        parentId: 0,
        level: ''
      }
    } catch (e) {
      ElMessage.error('添加失败')
    }
  })
}

// 编辑行政区
function handleEdit(row) {
  editRegionForm.value = { ...row }
  showEditRegion.value = true
}

async function handleEditRegion() {
  editRegionFormRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      await axios.post('/region/update', editRegionForm.value)
      ElMessage.success('编辑成功')
      showEditRegion.value = false
      fetchRegions()
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
      await axios.post('/region/delete', { id: row.id })
      ElMessage.success('删除成功')
      fetchRegions()
    } catch (e) {
      ElMessage.error('删除失败')
    }
  })
}

// 页面加载时获取数据
onMounted(() => {
  fetchRegions()
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