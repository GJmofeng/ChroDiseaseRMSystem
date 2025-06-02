<template>
  <div class="reimbursement-audit">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>报销审核</span>
          <el-button type="primary" @click="refreshList">刷新</el-button>
        </div>
      </template>

      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column prop="cardId" label="卡号" width="120" />
        <el-table-column prop="insuredName" label="参保人姓名" width="120" />
        <el-table-column prop="medicalName" label="医疗机构名称" width="180" />
        <el-table-column prop="address" label="地址" width="200" />
        <el-table-column prop="totalCost" label="总费用" width="120">
          <template #default="scope">
            {{ scope.row.totalCost.toFixed(2) }}
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
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAuditReimbursements, updateReimbursement } from '@/api/reimbursement'

export default {
  name: 'ReimbursementAudit',
  setup() {
    const tableData = ref([])
    const loading = ref(false)

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
          tableData.value = response.data
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
          isReimbursement: type === '通过' ? '已审核' : '审核不通过'
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

    const refreshList = () => {
      loadData()
    }

    onMounted(() => {
      loadData()
    })

    return {
      tableData,
      loading,
      handleAudit,
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
</style> 