<template>
  <div class="participation-info">
    <a-card title="参合信息录入" :bordered="false">
      <!-- 搜索表单 -->
      <a-form layout="inline" :model="searchForm">
        <a-form-item label="身份证号">
          <a-input v-model:value="searchForm.idCard" placeholder="请输入身份证号" />
        </a-form-item>
        <a-form-item label="姓名">
          <a-input v-model:value="searchForm.name" placeholder="请输入姓名" />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handleSearch">查询</a-button>
          <a-button style="margin-left: 8px" @click="handleReset">重置</a-button>
          <a-button type="primary" style="margin-left: 8px" @click="showAddModal">新增参合</a-button>
        </a-form-item>
      </a-form>

      <!-- 数据表格 -->
      <a-table
        :columns="columns"
        :data-source="tableData"
        :loading="loading"
        :pagination="pagination"
        @change="handleTableChange"
      >
        <template #action="{ record }">
          <a-space>
            <a @click="handleEdit(record)">编辑</a>
            <a-divider type="vertical" />
            <a-popconfirm
              title="确定要删除这条记录吗？"
              @confirm="handleDelete(record)"
            >
              <a>删除</a>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>

      <!-- 新增/编辑弹窗 -->
      <a-modal
        :visible="modalVisible"
        :title="modalTitle"
        @ok="handleModalOk"
        @cancel="handleModalCancel"
      >
        <a-form :model="form" :rules="rules" ref="formRef">
          <a-form-item label="身份证号" name="idCard">
            <a-input v-model:value="form.idCard" placeholder="请输入身份证号" />
          </a-form-item>
          <a-form-item label="姓名" name="name">
            <a-input v-model:value="form.name" placeholder="请输入姓名" />
          </a-form-item>
          <a-form-item label="参合年度" name="year">
            <a-date-picker
              v-model:value="form.year"
              picker="year"
              style="width: 100%"
            />
          </a-form-item>
          <a-form-item label="参合金额" name="amount">
            <a-input-number
              v-model:value="form.amount"
              :min="0"
              style="width: 100%"
              placeholder="请输入参合金额"
            />
          </a-form-item>
          <a-form-item label="参合状态" name="status">
            <a-select v-model:value="form.status" placeholder="请选择参合状态">
              <a-select-option value="1">已参合</a-select-option>
              <a-select-option value="0">未参合</a-select-option>
            </a-select>
          </a-form-item>
        </a-form>
      </a-modal>
    </a-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { searchParticipationInfo, addParticipationInfo, updateParticipationInfo, deleteParticipationInfo } from '@/api/participation'

// 搜索表单数据
const searchForm = reactive({
  idCard: '',
  name: ''
})

// 表格列定义
const columns = [
  {
    title: '身份证号',
    dataIndex: 'idCard',
    key: 'idCard'
  },
  {
    title: '姓名',
    dataIndex: 'name',
    key: 'name'
  },
  {
    title: '参合年度',
    dataIndex: 'year',
    key: 'year'
  },
  {
    title: '参合金额',
    dataIndex: 'amount',
    key: 'amount'
  },
  {
    title: '参合状态',
    dataIndex: 'status',
    key: 'status',
    customRender: ({ text }) => text === '1' ? '已参合' : '未参合'
  },
  {
    title: '操作',
    key: 'action',
    slots: { customRender: 'action' }
  }
]

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
  idCard: '',
  name: '',
  year: null,
  amount: null,
  status: '1'
})

const rules = {
  idCard: [{ required: true, message: '请输入身份证号' }],
  name: [{ required: true, message: '请输入姓名' }],
  year: [{ required: true, message: '请选择参合年度' }],
  amount: [{ required: true, message: '请输入参合金额' }],
  status: [{ required: true, message: '请选择参合状态' }]
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
    message.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.current = 1
  fetchData()
}

const handleReset = () => {
  searchForm.idCard = ''
  searchForm.name = ''
  handleSearch()
}

const handleTableChange = (pag) => {
  pagination.current = pag.current
  pagination.pageSize = pag.pageSize
  fetchData()
}

const showAddModal = () => {
  modalTitle.value = '新增参合信息'
  modalVisible.value = true
  Object.assign(form, {
    idCard: '',
    name: '',
    year: null,
    amount: null,
    status: '1'
  })
}

const handleEdit = (record) => {
  modalTitle.value = '编辑参合信息'
  modalVisible.value = true
  Object.assign(form, record)
}

const handleModalOk = async () => {
  try {
    await formRef.value.validate()
    const api = modalTitle.value.includes('新增') ? addParticipationInfo : updateParticipationInfo
    await api(form)
    message.success(`${modalTitle.value}成功`)
    modalVisible.value = false
    fetchData()
  } catch (error) {
    console.error(error)
  }
}

const handleModalCancel = () => {
  modalVisible.value = false
  formRef.value?.resetFields()
}

const handleDelete = async (record) => {
  try {
    await deleteParticipationInfo(record.id)
    message.success('删除成功')
    fetchData()
  } catch (error) {
    message.error('删除失败')
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
</style> 