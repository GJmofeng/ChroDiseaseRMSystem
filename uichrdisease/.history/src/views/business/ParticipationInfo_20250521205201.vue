<template>
  <div class="participation-info">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>参合信息录入</span>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <el-form :model="searchForm" inline>
        <el-form-item label="参合证号">
          <el-input v-model="searchForm.cardId" placeholder="请输入参合证号" />
        </el-form-item>
        <el-form-item label="参合人姓名">
          <el-input v-model="searchForm.insuredName" placeholder="请输入参合人姓名" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="primary" @click="showAddModal">新增参合</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table
        :data="tableData"
        v-loading="loading"
        style="width: 100%"
      >
        <el-table-column
          prop="cardId"
          label="参合证号"
          width="180"
        />
        <el-table-column
          prop="insuredName"
          label="参合人姓名"
          width="120"
        />
        <el-table-column
          prop="medicalName"
          label="医疗机构名称"
          width="180"
        />
        <el-table-column
          prop="startTime"
          label="生效时间"
          width="180"
        />
        <el-table-column
          prop="endTime"
          label="失效时间"
          width="180"
        />
        <el-table-column label="操作">
          <template #default="scope">
            <el-button
              size="small"
              type="primary"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-popconfirm
              title="确定要删除这条记录吗？"
              @confirm="handleDelete(scope.row)"
            >
              <template #reference>
                <el-button
                  size="small"
                  type="danger"
                >
                  删除
                </el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 30, 50]"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>

      <!-- 新增/编辑弹窗 -->
      <el-dialog
        v-model="modalVisible"
        :title="modalTitle"
        width="500px"
      >
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="100px"
        >
          <el-form-item label="参合证号" prop="cardId">
            <el-input v-model="form.cardId" placeholder="请输入参合证号" />
          </el-form-item>
          <el-form-item label="参合人姓名" prop="insuredName">
            <el-input v-model="form.insuredName" placeholder="请输入参合人姓名" />
          </el-form-item>
          <el-form-item label="医疗机构名称" prop="medicalName">
            <el-input v-model="form.medicalName" placeholder="请输入医疗机构名称" />
          </el-form-item>
          <el-form-item label="生效时间" prop="startTime">
            <el-date-picker
              v-model="form.startTime"
              type="datetime"
              placeholder="选择生效时间"
              style="width: 100%"
              value-format="YYYY-MM-DD HH:mm:ss"
            />
          </el-form-item>
          <el-form-item label="失效时间" prop="endTime">
            <el-date-picker
              v-model="form.endTime"
              type="datetime"
              placeholder="选择失效时间"
              style="width: 100%"
              value-format="YYYY-MM-DD HH:mm:ss"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="handleModalCancel">取消</el-button>
            <el-button type="primary" @click="handleModalOk">确定</el-button>
          </span>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { searchParticipationInfo, addParticipationInfo, updateParticipationInfo, deleteParticipationInfo } from '@/api/participation'

// 搜索表单数据
const searchForm = reactive({
  cardId: '',
  insuredName: ''
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0
})

// 表单相关
const modalVisible = ref(false)
const modalTitle = ref('新增参合信息')
const formRef = ref()
const form = reactive({
  cardId: '',
  insuredName: '',
  medicalName: '',
  startTime: '',
  endTime: ''
})

const rules = {
  cardId: [
    { required: true, message: '请输入参合证号', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  insuredName: [
    { required: true, message: '请输入参合人姓名', trigger: 'blur' }
  ],
  medicalName: [
    { required: true, message: '请输入医疗机构名称', trigger: 'blur' }
  ],
  startTime: [
    { required: true, message: '请选择生效时间', trigger: 'change' }
  ],
  endTime: [
    { required: true, message: '请选择失效时间', trigger: 'change' },
    {
      validator: (rule, value, callback) => {
        if (value && form.startTime && value <= form.startTime) {
          callback(new Error('失效时间必须晚于生效时间'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ]
}

// 方法定义
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      ...searchForm,
      page: pagination.current,
      pageSize: pagination.pageSize
    }
    const res = await searchParticipationInfo(params)
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.current = 1
  fetchData()
}

const handleReset = () => {
  searchForm.cardId = ''
  searchForm.insuredName = ''
  handleSearch()
}

const handleSizeChange = (val) => {
  pagination.pageSize = val
  fetchData()
}

const handleCurrentChange = (val) => {
  pagination.current = val
  fetchData()
}

const showAddModal = () => {
  modalTitle.value = '新增参合信息'
  modalVisible.value = true
  form.cardId = ''
  form.insuredName = ''
  form.medicalName = ''
  form.startTime = ''
  form.endTime = ''
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

const handleEdit = (row) => {
  modalTitle.value = '编辑参合信息'
  modalVisible.value = true
  Object.assign(form, row)
}

const handleModalOk = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid, fields) => {
    if (valid) {
      try {
        const api = modalTitle.value.includes('新增') ? addParticipationInfo : updateParticipationInfo
        await api(form)
        ElMessage.success(`${modalTitle.value}成功`)
        modalVisible.value = false
        fetchData()
      } catch (error) {
        ElMessage.error(`${modalTitle.value}失败`)
      }
    }
  })
}

const handleModalCancel = () => {
  modalVisible.value = false
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

const handleDelete = async (row) => {
  try {
    await deleteParticipationInfo(row.id)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.participation-info {
  padding: 24px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

:deep(.el-form-item__label) {
  font-weight: bold;
}
</style>