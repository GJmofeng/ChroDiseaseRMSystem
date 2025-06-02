<template>
  <div class="yearly-statistics">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span class="title">年度报销统计</span>
          <div class="year-selector">
            <el-select 
              v-model="selectedYear" 
              placeholder="选择年份" 
              @change="handleYearChange"
              class="year-select"
              popper-class="year-select-dropdown"
            >
              <el-option
                v-for="year in yearOptions"
                :key="year"
                :label="year + '年'"
                :value="year"
              >
                <span class="year-option">
                  <i class="fas fa-calendar-alt mr-2"></i>
                  {{ year }}年
                </span>
              </el-option>
            </el-select>
          </div>
        </div>
      </template>

      <div class="charts-container">
        <!-- 年度报销金额统计 -->
        <div class="chart-wrapper">
          <div ref="amountChartRef" class="chart"></div>
        </div>
        
        <!-- 年度报销数量统计 -->
        <div class="chart-wrapper">
          <div ref="countChartRef" class="chart"></div>
        </div>
      </div>

      <!-- 年度统计卡片 -->
      <div class="stat-cards">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card shadow="hover" class="stat-card">
              <template #header>
                <div class="stat-card-header">
                  <span>年总报销金额</span>
                  <i class="fas fa-yen-sign"></i>
                </div>
              </template>
              <div class="stat-card-value">{{ totalAmount.toFixed(2) }} 元</div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover" class="stat-card">
              <template #header>
                <div class="stat-card-header">
                  <span>年报销笔数</span>
                  <i class="fas fa-file-invoice"></i>
                </div>
              </template>
              <div class="stat-card-value">{{ totalCount }} 笔</div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover" class="stat-card">
              <template #header>
                <div class="stat-card-header">
                  <span>年平均报销金额</span>
                  <i class="fas fa-calculator"></i>
                </div>
              </template>
              <div class="stat-card-value">{{ averageAmount.toFixed(2) }} 元</div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import { getAllReimbursements } from '@/api/reimbursement'

export default {
  name: 'YearlyStatistics',
  setup() {
    const amountChartRef = ref(null)
    const countChartRef = ref(null)
    const selectedYear = ref(new Date().getFullYear().toString())
    const yearOptions = ref([])
    
    let amountChart = null
    let countChart = null

    // 统计数据
    const totalAmount = ref(0)
    const totalCount = ref(0)
    const averageAmount = computed(() => {
      return totalCount.value ? totalAmount.value / totalCount.value : 0
    })

    // 生成年份选项（近5年）
    const generateYearOptions = () => {
      const currentYear = new Date().getFullYear()
      yearOptions.value = Array.from({ length: 5 }, (_, i) => (currentYear - i).toString())
    }

    // 初始化图表
    const initCharts = () => {
      amountChart = echarts.init(amountChartRef.value)
      countChart = echarts.init(countChartRef.value)
      
      // 设置图表自适应
      window.addEventListener('resize', handleResize)
    }

    // 处理窗口大小变化
    const handleResize = () => {
      amountChart?.resize()
      countChart?.resize()
    }

    // 处理年份变化
    const handleYearChange = () => {
      loadData()
    }

    // 加载数据
    const loadData = async () => {
      try {
        const response = await getAllReimbursements()
        if (response.code === 200) {
          const data = response.data
          
          // 按月份统计报销金额
          const monthlyAmounts = Array(12).fill(0)
          // 按月份统计报销数量
          const monthlyCounts = Array(12).fill(0)
          
          // 重置统计数据
          totalAmount.value = 0
          totalCount.value = 0
          
          data.forEach(item => {
            const date = new Date(item.date)
            if (date.getFullYear().toString() === selectedYear.value) {
              const month = date.getMonth()
              const amount = item.reimbursementAmount || 0
              monthlyAmounts[month] += amount
              monthlyCounts[month]++
              
              // 累计总金额和总笔数
              totalAmount.value += amount
              totalCount.value++
            }
          })

          // 更新金额统计图表
          amountChart.setOption({
            title: {
              text: '年度报销金额统计',
              left: 'center'
            },
            tooltip: {
              trigger: 'axis',
              formatter: '{b}月: {c}元'
            },
            xAxis: {
              type: 'category',
              data: Array.from({ length: 12 }, (_, i) => i + 1 + '月')
            },
            yAxis: {
              type: 'value',
              name: '金额（元）'
            },
            series: [{
              data: monthlyAmounts,
              type: 'line',
              smooth: true,
              areaStyle: {
                opacity: 0.3
              },
              itemStyle: {
                color: '#409EFF'
              }
            }]
          })

          // 更新数量统计图表
          countChart.setOption({
            title: {
              text: '年度报销数量统计',
              left: 'center'
            },
            tooltip: {
              trigger: 'axis',
              formatter: '{b}: {c}笔'
            },
            xAxis: {
              type: 'category',
              data: Array.from({ length: 12 }, (_, i) => i + 1 + '月')
            },
            yAxis: {
              type: 'value',
              name: '数量（笔）'
            },
            series: [{
              data: monthlyCounts,
              type: 'bar',
              itemStyle: {
                color: '#67C23A'
              }
            }]
          })
        }
      } catch (error) {
        ElMessage.error('加载数据失败：' + error.message)
      }
    }

    onMounted(() => {
      generateYearOptions()
      initCharts()
      loadData()
    })

    onUnmounted(() => {
      window.removeEventListener('resize', handleResize)
      amountChart?.dispose()
      countChart?.dispose()
    })

    return {
      amountChartRef,
      countChartRef,
      selectedYear,
      yearOptions,
      totalAmount,
      totalCount,
      averageAmount,
      handleYearChange
    }
  }
}
</script>

<style scoped>
.yearly-statistics {
  margin: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 10px;
}

.title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  position: relative;
  padding-left: 12px;
}

.title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 16px;
  background: #409EFF;
  border-radius: 2px;
}

.year-selector {
  position: relative;
}

.year-select {
  width: 120px;
}

.year-select :deep(.el-input__wrapper) {
  background-color: #f5f7fa;
  box-shadow: none;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.year-select :deep(.el-input__wrapper:hover) {
  border-color: #409EFF;
  background-color: #fff;
}

.year-select :deep(.el-input__wrapper.is-focus) {
  border-color: #409EFF;
  background-color: #fff;
  box-shadow: 0 0 0 1px #409EFF;
}

.year-option {
  display: flex;
  align-items: center;
  color: #606266;
  font-size: 14px;
}

.year-option i {
  color: #909399;
  font-size: 14px;
}

.charts-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-top: 20px;
}

.chart-wrapper {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.chart {
  height: 400px;
  width: 100%;
}

/* 下拉菜单样式 */
:deep(.year-select-dropdown) {
  min-width: 120px !important;
}

:deep(.year-select-dropdown .el-select-dropdown__item) {
  padding: 8px 12px;
}

:deep(.year-select-dropdown .el-select-dropdown__item.selected) {
  color: #409EFF;
  font-weight: 600;
  background-color: #ecf5ff;
}

:deep(.year-select-dropdown .el-select-dropdown__item:hover) {
  background-color: #f5f7fa;
}

.stat-cards {
  margin-top: 20px;
}

.stat-card {
  text-align: center;
}

.stat-card-header {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
  color: #666;
}

.stat-card-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
  margin-top: 10px;
}

.stat-card i {
  font-size: 18px;
  color: #409EFF;
}

@media screen and (max-width: 768px) {
  .charts-container {
    grid-template-columns: 1fr;
  }
  
  .chart {
    height: 300px;
  }

  .stat-cards .el-col {
    margin-bottom: 20px;
  }
}
</style> 