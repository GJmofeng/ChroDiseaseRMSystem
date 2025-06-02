<template>
  <div class="chronic-cert-manage">
    <div class="page-header">
      <el-button type="primary" @click="handleAdd">
        <i class="fas fa-plus"></i> 新增慢性病证
      </el-button>
    </div>

    <!-- 搜索表单 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="卡号">
        <el-input v-model="searchForm.cardId" placeholder="请输入卡号" clearable />
      </el-form-item>
      <el-form-item label="参保人姓名">
        <el-input v-model="searchForm.insuredName" placeholder="请输入参保人姓名" clearable />
      </el-form-item>
      <el-form-item label="病种名称">
        <el-input v-model="searchForm.medicalName" placeholder="请输入病种名称" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">
          <i class="fas fa-search"></i> 搜索
        </el-button>
        <el-button @click="resetSearch">
          <i class="fas fa-redo"></i> 重置
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table :data="tableData" border style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="cardId" label="卡号" min-width="150" />
      <el-table-column prop="insuredName" label="参保人姓名" min-width="120" />
      <el-table-column prop="medicalName" label="病种名称" min-width="150" />
      <el-table-column prop="startTime" label="开始时间" min-width="120">
        <template #default="scope">
          {{ formatDate(scope.row.startTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="endTime" label="结束时间" min-width="120">
        <template #default="scope">
          {{ formatDate(scope.row.endTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="120">
        <template #default="scope">
          <el-button type="primary" link @click="handleEdit(scope.row)">
            <i class="fas fa-edit"></i> 编辑
          </el-button>
          <el-button type="danger" link @click="handleDelete(scope.row)">
            <i class="fas fa-trash"></i> 删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
      <div class="total-text">共 {{ total }} 条</div>
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        layout="sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        class="dialog-form"
      >
        <el-form-item label="卡号" prop="cardId">
          <el-input v-model="form.cardId" placeholder="请输入卡号" />
        </el-form-item>
        <el-form-item label="参保人姓名" prop="insuredName">
          <el-input v-model="form.insuredName" placeholder="请输入参保人姓名" />
        </el-form-item>
        <el-form-item label="病种名称" prop="medicalName">
          <el-select
            v-model="form.medicalName"
            placeholder="请选择病种名称"
            style="width: 100%"
            filterable
            clearable
          >
            <el-option
              v-for="item in diseaseOptions"
              :key="item.diseaseName"
              :label="item.diseaseName"
              :value="item.diseaseName"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="form.startTime"
            type="date"
            placeholder="选择开始时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            v-model="form.endTime"
            type="date"
            placeholder="选择结束时间"
            style="width: 100%"
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
import { formatDate } from '@/utils/date'
import { getChronicCertList, getMedicalByCardId, addChronicCert, updateChronicCert, deleteChronicCert } from '@/api/chronic'
import { getDiseaseList } from '@/api/disease'

// 搜索表单
const searchForm = reactive({
  cardId: '',
  insuredName: '',
  medicalName: ''
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = reactive({
  id: null,
  cardId: '',
  insuredName: '',
  medicalName: '',
  startTime: '',
  endTime: ''
})

// 疾病选项列表
const diseaseOptions = ref([])

// 获取疾病列表
const fetchDiseaseOptions = async () => {
  try {
    const res = await getDiseaseList({
      page: 1,
      pageSize: 1000 // 获取所有疾病数据
    })
    if (res.code === 200) {
      diseaseOptions.value = res.data.list || []
    } else {
      ElMessage.error('获取疾病列表失败')
    }
  } catch (error) {
    console.error('获取疾病列表失败:', error)
    ElMessage.error('获取疾病列表失败')
  }
}

// 表单验证规则
const rules = {
  cardId: [
    { required: true, message: '请输入卡号', trigger: 'blur' },
    { pattern: /^\d{17}[\dXx]$/, message: '请输入正确的18位卡号', trigger: 'blur' }
  ],
  insuredName: [
    { required: true, message: '请输入参保人姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  medicalName: [
    { required: true, message: '请选择病种名称', trigger: 'change' }
  ],
  startTime: [
    { required: true, message: '请选择开始时间', trigger: 'change' },
    { validator: (rule, value, callback) => {
      if (value && form.endTime && new Date(value) > new Date(form.endTime)) {
        callback(new Error('开始时间不能晚于结束时间'))
      } else {
        callback()
      }
    }, trigger: 'change' }
  ],
  endTime: [
    { required: true, message: '请选择结束时间', trigger: 'change' },
    { validator: (rule, value, callback) => {
      if (value && form.startTime && new Date(value) < new Date(form.startTime)) {
        callback(new Error('结束时间不能早于开始时间'))
      } else {
        callback()
      }
    }, trigger: 'change' }
  ]
}

// 获取列表数据
const fetchData = async () => {
  loading.value = true
  try {
    let res
    if (searchForm.cardId?.trim()) {
      // 如果输入了卡号，优先使用卡号查询
      res = await getMedicalByCardId(searchForm.cardId.trim())
      if (res.code === 200) {
        tableData.value = Array.isArray(res.data) ? res.data : [res.data]
        total.value = tableData.value.length
      }
    } else {
      // 使用分页查询
      res = await getChronicCertList({
        page: currentPage.value,
        pageSize: pageSize.value,
        insuredName: searchForm.insuredName?.trim(),
        medicalName: searchForm.medicalName?.trim()
      })
      
      if (res.code === 200) {
        tableData.value = res.data.list || []
        total.value = res.data.total || 0
      }
    }
    
    if (res.code === 200) {
      if (tableData.value.length === 0) {
        ElMessage.info('未查询到相关数据')
      }
    } else {
      ElMessage.error(res.msg || '获取数据失败')
    }
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  if (!searchForm.cardId?.trim() && !searchForm.insuredName?.trim() && !searchForm.medicalName?.trim()) {
    ElMessage.warning('请输入卡号、参保人姓名或病种名称进行查询')
    return
  }
  currentPage.value = 1
  fetchData()
}

// 重置搜索
const resetSearch = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = ''
  })
  currentPage.value = 1
  fetchData()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增慢性病证'
  Object.keys(form).forEach(key => {
    form[key] = key === 'id' ? null : ''
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑慢性病证'
  Object.keys(form).forEach(key => {
    form[key] = row[key]
  })
  dialogVisible.value = true
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该慢性病证吗？', '提示', {
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(async () => {
    try {
      const res = await deleteChronicCert(row.id)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        fetchData()
      } else {
        ElMessage.error(res.msg || '删除失败')
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 用户取消删除操作
  })
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const submitData = {
          id: form.id || null,
          cardId: form.cardId.trim(),
          insuredName: form.insuredName.trim(),
          medicalName: form.medicalName.trim(),
          startTime: form.startTime ? new Date(form.startTime).toISOString().split('T')[0] : null,
          endTime: form.endTime ? new Date(form.endTime).toISOString().split('T')[0] : null
        }

        let res
        if (form.id) {
          res = await updateChronicCert(submitData)
        } else {
          res = await addChronicCert(submitData)
        }
        
        if (res.code === 200) {
          ElMessage.success(form.id ? '更新成功' : '添加成功')
          dialogVisible.value = false
          fetchData()
        } else if (res.code === 400) {
          ElMessage.warning(res.msg || '数据验证失败')
        } else if (res.code === 404) {
          ElMessage.warning('记录不存在')
        } else {
          ElMessage.error(res.msg || '操作失败')
        }
      } catch (error) {
        console.error('提交失败:', error)
        ElMessage.error('提交失败')
      }
    }
  })
}

// 分页相关
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchData()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchData()
}

onMounted(() => {
  fetchData()
  fetchDiseaseOptions()
})
</script>

<style scoped>
.chronic-cert-manage {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-form {
  margin-bottom: 20px;
  padding: 20px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.total-text {
  font-size: 14px;
  color: #606266;
}

.dialog-form {
  padding: 20px;
}
</style>