<el-menu-item index="/reimbursement-remit">
  <el-icon><Money /></el-icon>
  <span>报销打款</span>
</el-menu-item>

<el-sub-menu index="statistics">
  <template #title>
    <el-icon><TrendCharts /></el-icon>
    <span>统计分析</span>
  </template>
  <el-menu-item index="/yearly-statistics">
    <el-icon><Calendar /></el-icon>
    <span>年度统计</span>
  </el-menu-item>
  <el-menu-item index="/monthly-statistics">
    <el-icon><DataLine /></el-icon>
    <span>月度统计</span>
  </el-menu-item>
</el-sub-menu> 