<template>
  <div class="policy-management">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>慢性病政策管理</span>
          <el-button type="primary" @click="handleAdd">新增政策</el-button>
        </div>
      </template>
      
      <el-table :data="policyList" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="year" label="年度" width="120" />
        <el-table-column prop="max" label="封顶线(元)" width="150" />
        <el-table-column prop="percentage" label="报销比例(%)" width="150" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="updateTime" label="更新时间" width="180" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增政策' : '编辑政策'"
      width="500px"
    >
      <el-form :model="policyForm" :rules="rules" ref="policyFormRef" label-width="100px">
        <el-form-item label="年度" prop="year">
          <el-input v-model="policyForm.year" placeholder="请输入年度" />
        </el-form-item>
        <el-form-item label="封顶线" prop="max">
          <el-input-number v-model="policyForm.max" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="报销比例" prop="percentage">
          <el-input-number v-model="policyForm.percentage" :min="0" :max="100" :precision="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPolicyList, addPolicy, updatePolicy, deletePolicy } from '@/api/policy'

const loading = ref(false)
const policyList = ref([])
const dialogVisible = ref(false)
const dialogType = ref('add')
const policyFormRef = ref(null)

const policyForm = ref({
  id: null,
  year: new Date().getFullYear().toString(),
  max: 0,
  percentage: 0,
  createTime: '',
  updateTime: ''
})

const rules = {
  year: [{ required: true, message: '请输入年度', trigger: 'blur' }],
  max: [{ required: true, message: '请输入封顶线', trigger: 'blur' }],
  percentage: [{ required: true, message: '请输入报销比例', trigger: 'blur' }]
}

// 获取政策列表
const fetchPolicyList = async () => {
  loading.value = true
  try {
    const res = await getPolicyList()
    if (res.code === 200) {
      policyList.value = res.data
    } else {
      ElMessage.error(res.message || '获取政策列表失败')
    }
  } catch (error) {
    ElMessage.error('获取政策列表失败')
  } finally {
    loading.value = false
  }
}

// 新增政策
const handleAdd = () => {
  dialogType.value = 'add'
  policyForm.value = {
    id: null,
    year: new Date().getFullYear().toString(),
    max: 0,
    percentage: 0,
    createTime: '',
    updateTime: ''
  }
  dialogVisible.value = true
}

// 编辑政策
const handleEdit = (row) => {
  dialogType.value = 'edit'
  policyForm.value = { ...row }
  dialogVisible.value = true
}

// 删除政策
const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该政策吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deletePolicy(row.id)
      if (res.code === 200) {
        ElMessage.success(res.message || '删除成功')
        fetchPolicyList()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

// 提交表单
const handleSubmit = async () => {
  if (!policyFormRef.value) return
  await policyFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        let res
        if (dialogType.value === 'add') {
          res = await addPolicy(policyForm.value)
        } else {
          res = await updatePolicy(policyForm.value)
        }
        if (res.code === 200) {
          ElMessage.success(res.message || (dialogType.value === 'add' ? '添加成功' : '更新成功'))
          dialogVisible.value = false
          fetchPolicyList()
        } else {
          ElMessage.error(res.message || (dialogType.value === 'add' ? '添加失败' : '更新失败'))
        }
      } catch (error) {
        ElMessage.error(dialogType.value === 'add' ? '添加失败' : '更新失败')
      }
    }
  })
}

onMounted(() => {
  fetchPolicyList()
})
</script>

<style scoped>
.policy-management {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style> 