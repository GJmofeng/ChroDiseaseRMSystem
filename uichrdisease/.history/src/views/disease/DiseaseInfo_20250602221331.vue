<template>
  <div class="disease-info-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <el-button type="primary" @click="handleAdd">新增慢病信息</el-button>
        </div>
      </template>
      
      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="疾病名称">
          <el-input v-model="searchForm.diseaseName" placeholder="请输入疾病名称" clearable />
        </el-form-item>
        <el-form-item label="疾病编码">
          <el-input v-model="searchForm.diseaseCode" placeholder="请输入疾病编码" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格区域 -->
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="diseaseCode" label="疾病编码" width="120" />
        <el-table-column prop="diseaseName" label="疾病名称" width="180" />
        <el-table-column prop="diseaseType" label="疾病类型" width="120" />
        <el-table-column prop="description" label="疾病描述" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增慢病信息' : '编辑慢病信息'"
      width="50%"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="疾病编码" prop="diseaseCode">
          <el-input v-model="form.diseaseCode" placeholder="请输入疾病编码" />
        </el-form-item>
        <el-form-item label="疾病名称" prop="diseaseName">
          <el-input v-model="form.diseaseName" placeholder="请输入疾病名称" />
        </el-form-item>
        <el-form-item label="疾病类型" prop="diseaseType">
          <el-select v-model="form.diseaseType" placeholder="请选择疾病类型">
            <el-option label="慢性病" value="慢性病" />
            <el-option label="特殊病" value="特殊病" />
            <el-option label="罕见病" value="罕见病" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="疾病描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="4"
            placeholder="请输入疾病描述"
          />
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDiseaseList, addDisease, updateDisease, deleteDisease } from '@/api/disease'

// 搜索表单
const searchForm = reactive({
  diseaseName: '',
  diseaseCode: ''
})

// 表格数据
const tableData = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 对话框相关
const dialogVisible = ref(false)
const dialogType = ref('add')
const formRef = ref(null)
const form = reactive({
  id: null,
  diseaseCode: '',
  diseaseName: '',
  diseaseType: '',
  description: ''
})

// 表单验证规则
const rules = {
  diseaseCode: [
    { required: true, message: '请输入疾病编码', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  diseaseName: [
    { required: true, message: '请输入疾病名称', trigger: 'blur' }
  ],
  diseaseType: [
    { required: true, message: '请选择疾病类型', trigger: 'change' }
  ]
}

// 获取表格数据
const getTableData = async () => {
  try {
    const res = await getDiseaseList({
      page: currentPage.value,
      pageSize: pageSize.value,
      diseaseName: searchForm.diseaseName?.trim(),
      diseaseCode: searchForm.diseaseCode?.trim()
    })

    if (res.code === 200) {
      tableData.value = res.data.list || []
      total.value = res.data.total || 0
      if (tableData.value.length === 0) {
        ElMessage.info('未查询到相关数据')
      }
    } else {
      ElMessage.error(res.msg || '获取数据失败')
    }
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error(error.message || '获取数据失败')
    tableData.value = []
    total.value = 0
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  getTableData()
}

// 重置搜索
const resetSearch = () => {
  searchForm.diseaseName = ''
  searchForm.diseaseCode = ''
  handleSearch()
}

// 新增
const handleAdd = () => {
  dialogType.value = 'add'
  dialogVisible.value = true
  Object.keys(form).forEach(key => {
    form[key] = ''
  })
}

// 编辑
const handleEdit = (row) => {
  dialogType.value = 'edit'
  dialogVisible.value = true
  Object.keys(form).forEach(key => {
    form[key] = row[key]
  })
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm(`确认删除慢病"${row.diseaseName}"吗？`, '提示', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteDisease(row.id)
      ElMessage.success(`慢病"${row.diseaseName}"删除成功`)
      getTableData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const submitData = {
          ...form,
          diseaseName: form.diseaseName?.trim(),
          diseaseCode: form.diseaseCode?.trim(),
          description: form.description?.trim()
        }

        let res
        if (dialogType.value === 'add') {
          res = await addDisease(submitData)
          if (res.code === 200) {
            ElMessage.success(`慢病"${submitData.diseaseName}"新增成功`)
            dialogVisible.value = false
            getTableData()
          } else if (res.code === 205) {
            ElMessage.warning(`慢病"${submitData.diseaseName}"已存在，请勿重复添加`)
          } else {
            ElMessage.error(res.msg || '新增失败')
          }
        } else {
          res = await updateDisease(submitData)
          if (res.code === 200) {
            ElMessage.success(`慢病"${submitData.diseaseName}"修改成功`)
            dialogVisible.value = false
            getTableData()
          } else {
            ElMessage.error(res.msg || '修改失败')
          }
        }
      } catch (error) {
        console.error('提交失败:', error)
        ElMessage.error(dialogType.value === 'add' ? '新增失败' : '修改失败')
      }
    }
  })
}

// 分页相关
const handleSizeChange = (val) => {
  pageSize.value = val
  getTableData()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  getTableData()
}

onMounted(() => {
  getTableData()
})
</script>

<style scoped>
.disease-info-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 