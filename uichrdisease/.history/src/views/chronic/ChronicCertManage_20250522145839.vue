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
        <el-form-item label="患者姓名">
          <el-input v-model="searchForm.patientName" placeholder="请输入患者姓名" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="有效" value="1" />
            <el-option label="已过期" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column prop="certNo" label="证件号码" />
        <el-table-column prop="patientName" label="患者姓名" />
        <el-table-column prop="idCard" label="身份证号" />
        <el-table-column prop="diseaseType" label="疾病类型" />
        <el-table-column prop="issueDate" label="发证日期" />
        <el-table-column prop="validityPeriod" label="有效期" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === '1' ? 'success' : 'danger'">
              {{ scope.row.status === '1' ? '有效' : '已过期' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="primary" link @click="handleView(scope.row)">查看</el-button>
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
        label-width="100px"
      >
        <el-form-item label="患者姓名" prop="patientName">
          <el-input v-model="form.patientName" placeholder="请输入患者姓名" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="疾病类型" prop="diseaseType">
          <el-select v-model="form.diseaseType" placeholder="请选择疾病类型">
            <el-option label="高血压" value="hypertension" />
            <el-option label="糖尿病" value="diabetes" />
            <el-option label="冠心病" value="coronary" />
          </el-select>
        </el-form-item>
        <el-form-item label="发证日期" prop="issueDate">
          <el-date-picker
            v-model="form.issueDate"
            type="date"
            placeholder="请选择发证日期"
          />
        </el-form-item>
        <el-form-item label="有效期" prop="validityPeriod">
          <el-date-picker
            v-model="form.validityPeriod"
            type="date"
            placeholder="请选择有效期"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getChronicCertList,
  getChronicCertDetail,
  addChronicCert,
  updateChronicCert,
  deleteChronicCert
} from '@/api/chronic-cert'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增慢性病证')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 搜索表单数据
const searchForm = reactive({
  certNo: '',
  patientName: '',
  status: ''
})

// 表单数据
const form = reactive({
  patientName: '',
  idCard: '',
  diseaseType: '',
  issueDate: '',
  validityPeriod: ''
})

// 表单验证规则
const rules = {
  patientName: [{ required: true, message: '请输入患者姓名', trigger: 'blur' }],
  idCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
  diseaseType: [{ required: true, message: '请选择疾病类型', trigger: 'change' }],
  issueDate: [{ required: true, message: '请选择发证日期', trigger: 'change' }],
  validityPeriod: [{ required: true, message: '请选择有效期', trigger: 'change' }]
}

// 表格数据
const tableData = ref([])

// 获取表格数据
const getTableData = async () => {
  loading.value = true
  try {
    const params = {
      ...searchForm,
      page: currentPage.value,
      pageSize: pageSize.value
    }
    const res = await getChronicCertList(params)
    tableData.value = res.data.records
    total.value = res.data.total
    loading.value = false
  } catch (error) {
    console.error(error)
    loading.value = false
    ElMessage.error('获取数据失败')
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
  searchForm.patientName = ''
  searchForm.status = ''
  handleSearch()
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
      await deleteChronicCert(row.id)
      ElMessage.success('删除成功')
      getTableData()
    } catch (error) {
      console.error(error)
      ElMessage.error('删除失败')
    }
  })
}

// 提交表单
const submitForm = async () => {
  const formRef = ref(null)
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (form.id) {
          // 编辑
          await updateChronicCert(form.id, form)
          ElMessage.success('更新成功')
        } else {
          // 新增
          await addChronicCert(form)
          ElMessage.success('新增成功')
        }
        dialogVisible.value = false
        getTableData()
      } catch (error) {
        console.error(error)
        ElMessage.error(form.id ? '更新失败' : '新增失败')
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
  done()
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