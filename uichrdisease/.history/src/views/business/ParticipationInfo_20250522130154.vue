<template>
  <div class="participation-info">
    <el-card class="box-card">
      
      <!-- 搜索表单 -->
      <el-form :model="searchForm" inline>
        <el-form-item label="参合证号">
          <el-input v-model="searchForm.cardId" placeholder="请输入参合证号" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="primary" @click="showAddModal">新增参合人员</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table
        :data="tableData"
        v-loading="loading"
        style="width: 100%"
      >
        <el-table-column
          prop="id"
          label="ID"
          width="80"
        />
        <el-table-column
          prop="cardId"
          label="参合证号"
          width="150"
        />
        <el-table-column
          prop="name"
          label="姓名"
          width="120"
        />
        <el-table-column
          prop="idNumber"
          label="身份证号"
          width="180"
        />
        <el-table-column
          prop="phone"
          label="联系电话"
          width="120"
        />
        <el-table-column
          prop="address"
          label="地址"
          width="200"
        />
        <el-table-column label="操作" fixed="right" width="150">
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
          <el-form-item label="姓名" prop="name">
            <el-input v-model="form.name" placeholder="请输入姓名" />
          </el-form-item>
          <el-form-item label="身份证号" prop="idNumber">
            <el-input v-model="form.idNumber" placeholder="请输入身份证号" />
          </el-form-item>
          <el-form-item label="联系电话" prop="phone">
            <el-input v-model="form.phone" placeholder="请输入联系电话" />
          </el-form-item>
          <el-form-item label="地址" prop="address">
            <el-input v-model="form.address" placeholder="请输入地址" />
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
import { searchParticipationInfo, getInsuredByCardId, addParticipationInfo, updateParticipationInfo, deleteParticipationInfo } from '@/api/participation'

// 搜索表单数据
const searchForm = reactive({
  cardId: ''
})

// 表格数据
const tableData = ref([])
const loading = ref(false)

// 表单相关
const modalVisible = ref(false)
const modalTitle = ref('新增参合人员')
const formRef = ref()
const form = reactive({
  id: '',
  cardId: '',
  name: '',
  idNumber: '',
  phone: '',
  address: ''
})

// 表单验证规则
const rules = {
  cardId: [
    { required: true, message: '请输入参合证号', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  idNumber: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入正确的身份证号', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  address: [
    { required: true, message: '请输入地址', trigger: 'blur' }
  ]
}

// 方法定义
const fetchData = async () => {
  loading.value = true
  try {
    const res = await searchParticipationInfo()
    if (res.code === 200) {
      tableData.value = res.data
    } else {
      ElMessage.error(res.msg || '获取数据失败')
    }
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = async () => {
  if (!searchForm.cardId) {
    fetchData()
    return
  }
  
  loading.value = true
  try {
    const res = await getInsuredByCardId(searchForm.cardId)
    if (res.code === 200) {
      tableData.value = res.data ? [res.data] : []
    } else {
      ElMessage.error(res.msg || '查询失败')
    }
  } catch (error) {
    ElMessage.error('查询失败')
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  searchForm.cardId = ''
  fetchData()
}

const showAddModal = () => {
  modalTitle.value = '新增参合人员'
  modalVisible.value = true
  form.id = ''
  form.cardId = ''
  form.name = ''
  form.idNumber = ''
  form.phone = ''
  form.address = ''
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

const handleEdit = (row) => {
  modalTitle.value = '编辑参合人员'
  modalVisible.value = true
  Object.assign(form, row)
}

const handleModalOk = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid, fields) => {
    if (valid) {
      try {
        const api = modalTitle.value.includes('新增') ? addParticipationInfo : updateParticipationInfo
        const res = await api(form)
        if (res.code === 200) {
          ElMessage.success(res.msg || `${modalTitle.value}成功`)
          modalVisible.value = false
          fetchData()
        } else {
          ElMessage.error(res.msg || `${modalTitle.value}失败`)
        }
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
    const res = await deleteParticipationInfo(row.id)
    if (res.code === 200) {
      ElMessage.success(res.msg || '删除成功')
      fetchData()
    } else {
      ElMessage.error(res.msg || '删除失败')
    }
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

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

:deep(.el-form-item__label) {
  font-weight: bold;
}
</style>