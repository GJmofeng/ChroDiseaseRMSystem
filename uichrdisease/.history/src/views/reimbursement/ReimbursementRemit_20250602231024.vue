<template>
  <div class="reimbursement-remit">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <span class="title">报销汇款</span>
            <el-radio-group v-model="remitStatus" @change="handleStatusChange" size="small">
              <el-radio-button label="all">全部</el-radio-button>
              <el-radio-button label="pending">待汇款</el-radio-button>
              <el-radio-button label="processed">已汇款</el-radio-button>
            </el-radio-group>
          </div>
          <el-button type="primary" @click="refreshList">刷新</el-button>
        </div>
      </template>

      <el-table :data="filteredTableData" style="width: 100%" v-loading="loading">
        <el-table-column prop="cardId" label="卡号" width="120" />
        <el-table-column prop="insuredName" label="参保人姓名" width="120" />
        <el-table-column prop="medicalName" label="医疗机构名称" width="180" />
        <el-table-column prop="address" label="地址" width="200" />
        <el-table-column prop="totalCost" label="总费用" width="120">
          <template #default="scope">
            {{ scope.row.totalCost?.toFixed(2) || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="reimbursementAmount" label="报销金额" width="120">
          <template #default="scope">
            {{ scope.row.reimbursementAmount?.toFixed(2) || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="invoiceNo" label="发票号码" width="120" />
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="isRemit" label="汇款状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.isRemit)">
              {{ getStatusText(scope.row.isRemit) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remitTime" label="汇款时间" width="180">
          <template #default="scope">
            {{ scope.row.remitTime || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button
              v-if="scope.row.isRemit === '未汇款'"
              type="success"
              size="small"
              @click="handleRemit(scope.row)"
            >
              确认汇款
            </el-button>
            <el-button
              v-if="scope.row.isRemit === '已汇款'"
              type="primary"
              size="small"
              @click="handleViewDetail(scope.row)"
            >
              查看详情
            </el-button>
          </template>
        </el-table-column>
        <template #empty>
          <div class="empty-block">
            <el-empty description="暂无报销汇款数据" />
          </div>
        </template>
      </el-table>
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="汇款详情"
      width="50%"
    >
      <div v-if="currentDetail" class="detail-content">
        <div class="detail-item">
          <span class="label">参保人：</span>
          <span class="value">{{ currentDetail.insuredName }}</span>
        </div>
        <div class="detail-item">
          <span class="label">医疗机构：</span>
          <span class="value">{{ currentDetail.medicalName }}</span>
        </div>
        <div class="detail-item">
          <span class="label">总费用：</span>
          <span class="value">¥{{ currentDetail.totalCost?.toFixed(2) || '-' }}</span>
        </div>
        <div class="detail-item">
          <span class="label">报销金额：</span>
          <span class="value">¥{{ currentDetail.reimbursementAmount?.toFixed(2) || '-' }}</span>
        </div>
        <div class="detail-item">
          <span class="label">汇款状态：</span>
          <el-tag :type="getStatusType(currentDetail.isRemit)">
            {{ getStatusText(currentDetail.isRemit) }}
          </el-tag>
        </div>
        <div class="detail-item">
          <span class="label">汇款时间：</span>
          <span class="value">{{ currentDetail.remitTime || '-' }}</span>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRemitReimbursements, updateReimbursement } from '@/api/reimbursement'

export default {
  name: 'ReimbursementRemit',
  setup() {
    const tableData = ref([])
    const loading = ref(false)
    const remitStatus = ref('all')
    const detailDialogVisible = ref(false)
    const currentDetail = ref(null)

    // 根据状态筛选数据
    const filteredTableData = computed(() => {
      if (remitStatus.value === 'all') {
        return tableData.value
      } else if (remitStatus.value === 'pending') {
        return tableData.value.filter(item => item.isRemit === '未汇款')
      } else if (remitStatus.value === 'processed') {
        return tableData.value.filter(item => item.isRemit === '已汇款')
      }
      return tableData.value
    })

    // 获取状态类型
    const getStatusType = (status) => {
      const types = {
        '未汇款': 'info',
        '已汇款': 'success'
      }
      return types[status] || 'info'
    }

    // 获取状态文本
    const getStatusText = (status) => {
      return status || '未汇款'
    }

    const loadData = async () => {
      loading.value = true
      try {
        const response = await getRemitReimbursements()
        if (response.code === 200) {
          // 确保数据中包含汇款状态
          tableData.value = response.data.map(item => ({
            ...item,
            isRemit: item.isRemit || '未汇款',
            remitTime: item.remitTime || null
          }))
        }
      } catch (error) {
        ElMessage.error('加载数据失败：' + error.message)
      } finally {
        loading.value = false
      }
    }

    const handleRemit = async (row) => {
      try {
        await ElMessageBox.confirm(
          `确认已完成${row.insuredName}的报销汇款吗？`,
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )

        const updatedRow = {
          ...row,
          isRemit: '已汇款',
          remitTime: new Date().toLocaleString()
        }

        const response = await updateReimbursement(updatedRow)
        if (response.code === 200) {
          ElMessage.success('汇款确认成功')
          loadData()
        } else {
          ElMessage.error(response.message || '汇款确认失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('操作失败：' + error.message)
        }
      }
    }

    const handleViewDetail = (row) => {
      currentDetail.value = row
      detailDialogVisible.value = true
    }

    const handleStatusChange = () => {
      // 状态切换时不需要重新加载数据，直接使用计算属性过滤
    }

    const refreshList = () => {
      loadData()
    }

    onMounted(() => {
      loadData()
    })

    return {
      tableData,
      loading,
      remitStatus,
      detailDialogVisible,
      currentDetail,
      filteredTableData,
      handleRemit,
      handleViewDetail,
      handleStatusChange,
      refreshList,
      getStatusType,
      getStatusText
    }
  }
}
</script>

<style scoped>
.reimbursement-remit {
  margin: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

:deep(.el-tag) {
  border-radius: 4px;
  padding: 0 8px;
  height: 24px;
  line-height: 22px;
}

:deep(.el-button--small) {
  padding: 6px 12px;
  margin-left: 8px;
}

:deep(.el-button--small:first-child) {
  margin-left: 0;
}

.empty-block {
  padding: 40px 0;
  text-align: center;
}

.detail-content {
  padding: 20px;
}

.detail-item {
  display: flex;
  margin-bottom: 16px;
  line-height: 24px;
}

.detail-item .label {
  width: 100px;
  color: #606266;
  font-weight: 500;
}

.detail-item .value {
  flex: 1;
  color: #303133;
}
</style> 