<template>
  <div class="chronic-cert-manage">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>慢性病证管理</span>
          <el-button type="primary" @click="handleAdd">新增慢性病证</el-button>
        </div>
      </template>

      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="证件号码">
          <el-input v-model="searchForm.certNo" placeholder="请输入证件号码" clearable />
        </el-form-item>
        <el-form-item label="参保人姓名">
          <el-input v-model="searchForm.insuredName" placeholder="请输入参保人姓名" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getTableData">查询</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column prop="certNo" label="证件号码" />
        <el-table-column prop="insuredName" label="参保人姓名" />
        <el-table-column prop="medicalName" label="医疗机构名称" />
        <el-table-column prop="startTime" label="开始时间">
          <template #default="scope">
            {{ formatDate(scope.row.startTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="endTime" label="结束时间">
          <template #default="scope">
            {{ formatDate(scope.row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 30, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="50%"
      :before-close="handleClose"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="120px"
      >
        <el-form-item label="证件号码" prop="certNo">
          <el-input v-model="form.certNo" placeholder="请输入证件号码" />
        </el-form-item>
        <el-form-item label="参保人姓名" prop="insuredName">
          <el-input v-model="form.insuredName" placeholder="请输入参保人姓名" />
        </el-form-item>
        <el-form-item label="医疗机构名称" prop="medicalName">
          <el-input v-model="form.medicalName" placeholder="请输入医疗机构名称" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="form.startTime"
            type="date"
            placeholder="请选择开始时间"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            v-model="form.endTime"
            type="date"
            placeholder="请选择结束时间"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm(formRef)">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增慢性病证')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const formRef = ref(null)

// 搜索表单数据
const searchForm = reactive({
  certNo: '',
  insuredName: ''
})

// 表单数据
const form = reactive({
  id: '',
  certNo: '',
  insuredName: '',
  medicalName: '',
  startTime: '',
  endTime: ''
})

// 表单验证规则
const rules = {
  certNo: [{ required: true, message: '请输入证件号码', trigger: 'blur' }],
  insuredName: [{ required: true, message: '请输入参保人姓名', trigger: 'blur' }],
  medicalName: [{ required: true, message: '请输入医疗机构名称', trigger: 'blur' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }]
}

// 格式化日期
const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleDateString()
}

// 表格数据
const tableData = ref([])

// API请求方法
const getChronicCertList = () => {
  return request({
    url: '/chronicCert/getAll',
    method: 'get'
  })
}

const getChronicCertDetail = (id) => {
  return request({
    url: `/chronicCert/${id}`,
    method: 'get'
  })
}

const addChronicCert = (data) => {
  return request({
    url: '/chronicCert/add',
    method: 'post',
    data
  })
}

const updateChronicCert = (data) => {
  return request({
    url: '/chronicCert/update',
    method: 'post',
    data
  })
}

const deleteChronicCert = (id) => {
  return request({
    url: `/chronicCert/delete/${id}`,
    method: 'delete'
  })
}

const searchByCertNo = (certNo) => {
  return request({
    url: `/chronicCert/getCert/${certNo}`,
    method: 'get'
  })
}

const searchByPatientName = (patientName) => {
  return request({
    url: `/chronicCert/getByPatient/${patientName}`,
    method: 'get'
  })
}

// 获取表格数据
const getTableData = async () => {
  loading.value = true
  try {
    let res
    if (searchForm.certNo) {
      res = await searchByCertNo(searchForm.certNo)
    } else if (searchForm.insuredName) {
      res = await searchByPatientName(searchForm.insuredName)
    } else {
      res = await getChronicCertList()
    }
    
    if (res.code === 200) {
      tableData.value = res.data || []
      total.value = (res.data || []).length
      ElMessage.success(res.msg)
    } else {
      ElMessage.error(res.msg || '获取数据失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  getTableData()
}

// 重置表单
const resetForm = () => {
  searchForm.certNo = ''
  searchForm.insuredName = ''
  getTableData()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增慢性病证'
  dialogVisible.value = true
  Object.keys(form).forEach(key => {
    form[key] = ''
  })
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑慢性病证'
  dialogVisible.value = true
  Object.keys(form).forEach(key => {
    form[key] = row[key]
  })
}

// 查看
const handleView = async (row) => {
  try {
    const res = await getChronicCertDetail(row.id)
    // 这里可以添加查看详情的逻辑，比如打开一个新的对话框显示详细信息
    console.log(res.data)
  } catch (error) {
    console.error(error)
    ElMessage.error('获取详情失败')
  }
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该慢性病证记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteChronicCert(row.id)
      if (res.code === 200) {
        ElMessage.success(res.msg)
        getTableData()
      } else {
        ElMessage.error(res.msg || '删除失败')
      }
    } catch (error) {
      console.error(error)
      ElMessage.error('删除失败')
    }
  })
}

// 提交表单
const submitForm = async (formEl) => {
  if (!formEl) return
  
  await formEl.validate(async (valid) => {
    if (valid) {
      try {
        const res = form.id ? 
          await updateChronicCert(form) : 
          await addChronicCert(form)

        if (res.code === 200) {
          ElMessage.success(res.msg)
          dialogVisible.value = false
          getTableData()
        } else {
          ElMessage.error(res.msg || '操作失败')
        }
      } catch (error) {
        console.error(error)
        ElMessage.error('操作失败')
      }
    }
  })
}

// 分页大小改变
const handleSizeChange = (val) => {
  pageSize.value = val
  getTableData()
}

// 当前页改变
const handleCurrentChange = (val) => {
  currentPage.value = val
  getTableData()
}

// 对话框关闭前的回调
const handleClose = (done) => {
  ElMessageBox.confirm('确认关闭？未保存的数据将会丢失', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    done()
  }).catch(() => {})
}

onMounted(() => {
  getTableData()
})
</script>

<style scoped>
.chronic-cert-manage {
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

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>