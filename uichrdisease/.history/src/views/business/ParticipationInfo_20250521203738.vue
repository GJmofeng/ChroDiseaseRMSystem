<template>
  <div class="participation-info">
    <a-card title="参合信息录入" :bordered="false">
      <!-- 搜索表单 -->
      <a-form layout="inline" :model="searchForm">
        <a-form-item label="参合证号">
          <a-input v-model:value="searchForm.cardId" placeholder="请输入参合证号" />
        </a-form-item>
        <a-form-item label="参合人姓名">
          <a-input v-model:value="searchForm.insuredName" placeholder="请输入参合人姓名" />
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
          <a-form-item label="参合证号" name="cardId">
            <a-input v-model:value="form.cardId" placeholder="请输入参合证号" />
          </a-form-item>
          <a-form-item label="参合人姓名" name="insuredName">
            <a-input v-model:value="form.insuredName" placeholder="请输入参合人姓名" />
          </a-form-item>
          <a-form-item label="医疗机构名称" name="medicalName">
            <a-input v-model:value="form.medicalName" placeholder="请输入医疗机构名称" />
          </a-form-item>
          <a-form-item label="生效时间" name="startTime">
            <a-date-picker
              v-model:value="form.startTime"
              style="width: 100%"
              :show-time="true"
              format="YYYY-MM-DD HH:mm:ss"
            />
          </a-form-item>
          <a-form-item label="失效时间" name="endTime">
            <a-date-picker
              v-model:value="form.endTime"
              style="width: 100%"
              :show-time="true"
              format="YYYY-MM-DD HH:mm:ss"
            />
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
  cardId: '',
  insuredName: ''
})

// 表格列定义
const columns = [
  {
    title: '参合证号',
    dataIndex: 'cardId',
    key: 'cardId'
  },
  {
    title: '参合人姓名',
    dataIndex: 'insuredName',
    key: 'insuredName'
  },
  {
    title: '医疗机构名称',
    dataIndex: 'medicalName',
    key: 'medicalName'
  },
  {
    title: '生效时间',
    dataIndex: 'startTime',
    key: 'startTime'
  },
  {
    title: '失效时间',
    dataIndex: 'endTime',
    key: 'endTime'
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
  cardId: '',
  insuredName: '',
  medicalName: '',
  startTime: null,
  endTime: null
})

const rules = {
  cardId: [{ required: true, message: '请输入参合证号' }],
  insuredName: [{ required: true, message: '请输入参合人姓名' }],
  medicalName: [{ required: true, message: '请输入医疗机构名称' }],
  startTime: [{ required: true, message: '请选择生效时间' }],
  endTime: [{ required: true, message: '请选择失效时间' }]
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
  searchForm.cardId = ''
  searchForm.insuredName = ''
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
    cardId: '',
    insuredName: '',
    medicalName: '',
    startTime: null,
    endTime: null
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
    message.success(`