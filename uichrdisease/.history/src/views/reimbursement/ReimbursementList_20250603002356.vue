<template>
  <div class="reimbursement-list">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>报销记录列表</span>
          <div class="header-right">
            <el-button type="success" @click="handleExport">导出报表</el-button>
            <el-button type="primary" @click="refreshList">刷新</el-button>
          </div>
        </div>
      </template>

      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column prop="cardId" label="卡号" width="120" />
        <el-table-column prop="insuredName" label="参保人姓名" width="120" />
        <el-table-column prop="medicalName" label="医疗机构名称" width="180" />
        <el-table-column prop="address" label="地址" width="200" />
        <el-table-column prop="totalCost" label="总费用" width="120">
          <template #default="scope">
            {{ scope.row.totalCost ? scope.row.totalCost.toFixed(2) : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="reimbursementAmount" label="报销金额" width="120">
          <template #default="scope">
            {{ scope.row.reimbursementAmount ? scope.row.reimbursementAmount.toFixed(2) : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="invoiceNo" label="发票号码" width="120" />
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="isReimbursement" label="审核状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.isReimbursement === '已审核' ? 'success' : 'warning'">
              {{ scope.row.isReimbursement }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isRemit" label="汇款状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.isRemit === '已汇款' ? 'success' : 'warning'">
              {{ scope.row.isRemit }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>

      <div class="statistics" v-if="statistics">
        <el-descriptions title="统计信息" :column="2" border>
          <el-descriptions-item label="已审核">{{ statistics.已审核 }}</el-descriptions-item>
          <el-descriptions-item label="未审核">{{ statistics.未审核 }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getAllReimbursements, getReimbursementStatistics } from '@/api/reimbursement'
import * as XLSX from 'xlsx'

export default {
  name: 'ReimbursementList',
  setup() {
    const tableData = ref([])
    const loading = ref(false)
    const statistics = ref(null)

    const loadData = async () => {
      loading.value = true
      try {
        const [listResponse, statsResponse] = await Promise.all([
          getAllReimbursements(),
          getReimbursementStatistics()
        ])
        
        if (listResponse.code === 200) {
          tableData.value = listResponse.data
        }
        
        if (statsResponse.code === 200) {
          statistics.value = statsResponse.data
        }
      } catch (error) {
        ElMessage.error('加载数据失败：' + error.message)
      } finally {
        loading.value = false
      }
    }

    const refreshList = () => {
      loadData()
    }

    // 导出报表
    const handleExport = () => {
      try {
        const exportData = tableData.value.map(item => ({
          '卡号': item.cardId,
          '参保人姓名': item.insuredName,
          '医疗机构名称': item.medicalName,
          '地址': item.address,
          '总费用': item.totalCost?.toFixed(2) || '-',
          '报销金额': item.reimbursementAmount?.toFixed(2) || '-',
          '发票号码': item.invoiceNo,
          '日期': item.date,
          '审核状态': item.isReimbursement,
          '汇款状态': item.isRemit
        }))

        const ws = XLSX.utils.json_to_sheet(exportData)
        const wb = XLSX.utils.book_new()
        XLSX.utils.book_append_sheet(wb, ws, '报销记录数据')
        
        // 生成文件名
        const fileName = `报销记录数据_${new Date().toLocaleDateString()}.xlsx`
        XLSX.writeFile(wb, fileName)
        
        ElMessage.success('导出成功')
      } catch (error) {
        ElMessage.error('导出失败：' + error.message)
      }
    }

    onMounted(() => {
      loadData()
    })

    return {
      tableData,
      loading,
      statistics,
      refreshList,
      handleExport
    }
  }
}
</script>

<style scoped>
.reimbursement-list {
  margin: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.statistics {
  margin-top: 20px;
}

.header-right {
  display: flex;
  gap: 10px;
}
</style> 