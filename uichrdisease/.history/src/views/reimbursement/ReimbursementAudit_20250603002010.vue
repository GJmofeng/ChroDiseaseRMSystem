<template>
  <div class="reimbursement-audit">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <span class="title">报销审核</span>
            <el-radio-group v-model="auditStatus" @change="handleStatusChange" size="small">
              <el-radio-button label="all">全部</el-radio-button>
              <el-radio-button label="pending">待审核</el-radio-button>
              <el-radio-button label="processed">已处理</el-radio-button>
            </el-radio-group>
          </div>
          <div class="header-right">
            <el-button type="success" @click="handleExport">导出报表</el-button>
            <el-button type="primary" @click="refreshList">刷新</el-button>
          </div>
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
        <el-table-column prop="isReimbursement" label="审核状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.isReimbursement)">
              {{ getStatusText(scope.row.isReimbursement) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="auditTime" label="审核时间" width="180">
          <template #default="scope">
            {{ scope.row.auditTime || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button
              v-if="scope.row.isReimbursement === '未审核'"
              type="success"
              size="small"
              @click="handleAudit(scope.row, '通过')"
            >
              通过审核
            </el-button>
            <el-button
              v-if="scope.row.isReimbursement === '未审核'"
              type="danger"
              size="small"
              @click="handleAudit(scope.row, '不通过')"
            >
              不通过审核
            </el-button>
            <el-button
              v-if="scope.row.isReimbursement !== '未审核'"
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
            <el-empty description="暂无报销审核数据" />
          </div>
        </template>
      </el-table>
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="报销详情"
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
          <span class="value">¥{{ currentDetail.totalCost.toFixed(2) }}</span>
        </div>
        <div class="detail-item">
          <span class="label">报销金额：</span>
          <span class="value">¥{{ currentDetail.reimbursementAmount?.toFixed(2) || '-' }}</span>
        </div>
        <div class="detail-item">
          <span class="label">审核状态：</span>
          <el-tag :type="getStatusType(currentDetail.isReimbursement)">
            {{ getStatusText(currentDetail.isReimbursement) }}
          </el-tag>
        </div>
        <div class="detail-item">
          <span class="label">审核时间：</span>
          <span class="value">{{ currentDetail.auditTime || '-' }}</span>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAuditReimbursements, updateReimbursement } from '@/api/reimbursement'
import * as XLSX from 'xlsx'

export default {
  name: 'ReimbursementAudit',
  setup() {
    const tableData = ref([])
    const loading = ref(false)
    const auditStatus = ref('all')
    const detailDialogVisible = ref(false)
    const currentDetail = ref(null)

    // 根据状态筛选数据
    const filteredTableData = computed(() => {
      if (auditStatus.value === 'all') {
        return tableData.value
      } else if (auditStatus.value === 'pending') {
        return tableData.value.filter(item => item.isReimbursement === '未审核')
      } else if (auditStatus.value === 'processed') {
        return tableData.value.filter(item => item.isReimbursement === '已审核' || item.isReimbursement === '审核不通过')
      }
      return tableData.value
    })

    // 获取状态类型
    const getStatusType = (status) => {
      const types = {
        '未审核': 'info',
        '已审核': 'success',
        '审核不通过': 'danger'
      }
      return types[status] || 'info'
    }

    // 获取状态文本
    const getStatusText = (status) => {
      return status || '未审核'
    }

    const loadData = async () => {
      loading.value = true
      try {
        const response = await getAuditReimbursements()
        if (response.code === 200) {
          // 确保数据中包含审核状态
          tableData.value = response.data.map(item => ({
            ...item,
            isReimbursement: item.isReimbursement || '未审核',
            auditTime: item.auditTime || null
          }))
        }
      } catch (error) {
        ElMessage.error('加载数据失败：' + error.message)
      } finally {
        loading.value = false
      }
    }

    const handleAudit = async (row, type) => {
      try {
        const confirmMessage = type === '通过' 
          ? `确认通过${row.insuredName}的报销申请审核吗？`
          : `确认不通过${row.insuredName}的报销申请审核吗？`
        
        await ElMessageBox.confirm(
          confirmMessage,
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: type === '通过' ? 'warning' : 'error'
          }
        )

        const updatedRow = {
          ...row,
          isReimbursement: type === '通过' ? '已审核' : '审核不通过',
          auditTime: new Date().toLocaleString()
        }

        const response = await updateReimbursement(updatedRow)
        if (response.code === 200) {
          ElMessage.success(type === '通过' ? '审核通过成功' : '已标记为审核不通过')
          loadData()
        } else {
          ElMessage.error(response.message || '审核操作失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('操作失败：' + error.message)
        }
      }
    }

    const handleStatusChange = () => {
      // 状态切换时不需要重新加载数据，直接使用计算属性过滤
      // loadData()
    }

    const handleViewDetail = (row) => {
      currentDetail.value = row
      detailDialogVisible.value = true
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
      auditStatus,
      detailDialogVisible,
      currentDetail,
      filteredTableData,
      handleAudit,
      handleStatusChange,
      handleViewDetail,
      refreshList,
      getStatusType,
      getStatusText
    }
  }
}
</script>

<style scoped>
.reimbursement-audit {
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

.empty-block {
  padding: 40px 0;
  text-align: center;
}
</style> 