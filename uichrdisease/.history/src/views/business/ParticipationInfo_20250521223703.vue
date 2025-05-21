<template>
  <div class="participation-info-page">
    <!-- 添加标签导航 -->
    <TabNav />
    
    <div class="card">
      <template #header>
        <div class="card-header">
          <span>参合信息录入</span>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <div class="toolbar">
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
      </div>

      <!-- 数据表格 -->
      <div class="table-container">
        <el-table
          :data="tableData"
          v-loading="loading"
          border
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
            prop="insuredName"
            label="姓名"
            width="120"
          />
          <el-table-column
            prop="gender"
            label="性别"
            width="80"
          />
          <el-table-column
            prop="nation"
            label="民族"
            width="100"
          />
          <el-table-column
            prop="phone"
            label="联系电话"
            width="120"
          />
          <el-table-column
            prop="address"
            label="地址"
            min-width="200"
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
      </div>

      <!-- 分页组件 -->
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

      <!-- 新增/编辑弹窗 -->
      <el-dialog
        v-model="modalVisible"
        :title="modalTitle"
        width="500px"
        :close-on-click-modal="false"
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
          <el-form-item label="姓名" prop="insuredName">
            <el-input v-model="form.insuredName" placeholder="请输入姓名" />
          </el-form-item>
          <el-form-item label="性别" prop="gender">
            <el-select v-model="form.gender" placeholder="请选择性别">
              <el-option label="男" value="男" />
              <el-option label="女" value="女" />
            </el-select>
          </el-form-item>
          <el-form-item label="民族" prop="nation">
            <el-input v-model="form.nation" placeholder="请输入民族" />
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
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import TabNav from '@/components/TabNav.vue'
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
  insuredName: '',
  gender: '',
  nation: '',
  phone: '',
  address: ''
})

// 表单验证规则
const rules = {
  cardId: [
    { required: true, message: '请输入参合证号', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  insuredName: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  nation: [
    { required: true, message: '请输入民族', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  address: [
    { required: true, message: '请输入地址', trigger: 'blur' }
  ]
}

// 分页相关数据
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 获取所有数据
const fetchData = async () => {
  loading.value = true
  try {
    const res = await searchParticipationInfo({
      pageNum: currentPage.value,
      pageSize: pageSize.value
    })
    if (res.code === 200) {
      tableData.value = res.data.records
      total.value = res.data.total
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

const handleSearch = async () => {
  if (!searchForm.cardId || !searchForm.cardId.trim()) {
    ElMessage.warning('请输入参合证号')
    return
  }
  
  const cardId = searchForm.cardId.trim()
  // 验证参合证号格式
  if (!/^\d+$/.test(cardId)) {
    ElMessage.warning('参合证号必须是数字')
    return
  }
  
  loading.value = true
  try {
    const res = await getInsuredByCardId(cardId)
    if (res.code === 200) {
      if (res.data) {
        // 确保数据字段匹配
        const formattedData = {
          id: res.data.id,
          cardId: res.data.cardId,
          insuredName: res.data.insuredName,
          gender: res.data.gender,
          nation: res.data.nation,
          phone: res.data.phone,
          address: res.data.address
        }
        tableData.value = [formattedData]
      } else {
        tableData.value = []
        ElMessage.info(res.msg || '未找到相关参合人员信息')
      }
    } else {
      tableData.value = []
      if (res.code === 400) {
        ElMessage.warning(res.msg || '参合证号格式不正确')
      } else {
        ElMessage.error(res.msg || '查询失败')
      }
    }
  } catch (error) {
    console.error('查询出错:', error)
    tableData.value = []
    ElMessage.error('查询失败，请稍后重试')
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
  resetForm()
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

const resetForm = () => {
  form.id = ''
  form.cardId = ''
  form.insuredName = ''
  form.gender = ''
  form.nation = ''
  form.phone = ''
  form.address = ''
}

// 处理每页显示数量变化
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchData()
}

// 处理页码变化
const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchData()
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.participation-info-page {
  padding: 24px;
  background: #f5f6fa;
  min-height: 100vh;
}

.card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  padding: 24px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.card-header span {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
}

.toolbar {
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.table-container {
  width: 100%;
  overflow-x: auto;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

/* 表格样式优化 */
:deep(.el-table) {
  --el-table-border-color: #f0f0f0;
  --el-table-header-bg-color: #fafbfc;
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table th) {
  font-weight: 600;
  color: #1f2937;
  background: var(--el-table-header-bg-color);
}

:deep(.el-table td) {
  color: #4b5563;
}

:deep(.el-button) {
  border-radius: 6px;
  font-weight: 500;
}

:deep(.el-input__wrapper) {
  border-radius: 6px;
}

/* 表单样式优化 */
:deep(.el-form-item__label) {
  font-weight: 500;
  color: #374151;
}

/* 弹窗样式优化 */
:deep(.el-dialog) {
  border-radius: 12px;
  overflow: hidden;
}

:deep(.el-dialog__header) {
  margin: 0;
  padding: 20px 24px;
  border-bottom: 1px solid #f0f0f0;
}

:deep(.el-dialog__body) {
  padding: 24px;
}

:deep(.el-dialog__footer) {
  padding: 16px 24px;
  border-top: 1px solid #f0f0f0;
}

/* TabNav 相关样式 */
:deep(.tab-nav-bar) {
  margin-bottom: 16px;
  background: transparent;
}

:deep(.tab-item) {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(8px);
}

:deep(.tab-item.active) {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}
</style>