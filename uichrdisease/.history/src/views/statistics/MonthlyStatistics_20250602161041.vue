<template>
  <div class="monthly-statistics">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>月度报销统计</span>
          <div class="date-picker">
            <el-date-picker
              v-model="selectedDate"
              type="month"
              placeholder="选择月份"
              format="YYYY年MM月"
              value-format="YYYY-MM"
              @change="handleDateChange"
            />
          </div>
        </div>
      </template>

      <div class="charts-container">
        <!-- 月度报销金额趋势 -->
        <div class="chart-wrapper">
          <div ref="amountChartRef" class="chart"></div>
        </div>
        
        <!-- 月度报销类型分布 -->
        <div class="chart-wrapper">
          <div ref="typeChartRef" class="chart"></div>
        </div>
      </div>

      <!-- 月度统计卡片 -->
      <div class="stat-cards">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card shadow="hover" class="stat-card">
              <template #header>
                <div class="stat-card-header">
                  <span>总报销金额</span>
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
                  <span>报销笔数</span>
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
                  <span>平均报销金额</span>
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
  name: 'MonthlyStatistics',
  setup() {
    const amountChartRef = ref(null)
    const typeChartRef = ref(null)
    const selectedDate = ref(new Date().toISOString().slice(0, 7))
    
    let amountChart = null
    let typeChart = null

    // 统计数据
    const totalAmount = ref(0)
    const totalCount = ref(0)
    const averageAmount = computed(() => {
      return totalCount.value ? totalAmount.value / totalCount.value : 0
    })

    // 初始化图表
    const initCharts = () => {
      amountChart = echarts.init(amountChartRef.value)
      typeChart = echarts.init(typeChartRef.value)
      
      window.addEventListener('resize', handleResize)
    }

    // 处理窗口大小变化
    const handleResize = () => {
      amountChart?.resize()
      typeChart?.resize()
    }

    // 处理日期变化
    const handleDateChange = () => {
      loadData()
    }

    // 加载数据
    const loadData = async () => {
      try {
        const response = await getAllReimbursements()
        if (response.code === 200) {
          const data = response.data
          const [year, month] = selectedDate.value.split('-')
          
          // 按日期统计报销金额
          const dailyAmounts = Array(31).fill(0)
          // 按类型统计报销金额
          const typeAmounts = {}
          
          totalAmount.value = 0
          totalCount.value = 0
          
          data.forEach(item => {
            const date = new Date(item.date)
            if (date.getFullYear().toString() === year && 
                (date.getMonth() + 1).toString().padStart(2, '0') === month) {
              const day = date.getDate() - 1
              const amount = item.reimbursementAmount || 0
              
              dailyAmounts[day] += amount
              totalAmount.value += amount
              totalCount.value++
              
              // 统计类型分布
              const type = item.medicalName || '其他'
              typeAmounts[type] = (typeAmounts[type] || 0) + amount
            }
          })

          // 更新金额趋势图表
          amountChart.setOption({
            title: {
              text: '月度报销金额趋势',
              left: 'center'
            },
            tooltip: {
              trigger: 'axis',
              formatter: '{b}日: {c}元'
            },
            xAxis: {
              type: 'category',
              data: Array.from({ length: 31 }, (_, i) => i + 1 + '日')
            },
            yAxis: {
              type: 'value',
              name: '金额（元）'
            },
            series: [{
              data: dailyAmounts,
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

          // 更新类型分布图表
          const typeData = Object.entries(typeAmounts).map(([name, value]) => ({
            name,
            value
          }))

          typeChart.setOption({
            title: {
              text: '报销类型分布',
              left: 'center'
            },
            tooltip: {
              trigger: 'item',
              formatter: '{b}: {c}元 ({d}%)'
            },
            series: [{
              type: 'pie',
              radius: '50%',
              data: typeData,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }]
          })
        }
      } catch (error) {
        ElMessage.error('加载数据失败：' + error.message)
      }
    }

    onMounted(() => {
      initCharts()
      loadData()
    })

    onUnmounted(() => {
      window.removeEventListener('resize', handleResize)
      amountChart?.dispose()
      typeChart?.dispose()
    })

    return {
      amountChartRef,
      typeChartRef,
      selectedDate,
      totalAmount,
      totalCount,
      averageAmount,
      handleDateChange
    }
  }
}
</script>

<style scoped>
.monthly-statistics {
  margin: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
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
</style> 