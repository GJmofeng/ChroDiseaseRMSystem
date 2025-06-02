<template>
  <div class="reimbursement-form">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>参合缴费登记</span>
        </div>
      </template>
      
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="卡号" prop="cardId">
          <el-input v-model="form.cardId" placeholder="请输入卡号"></el-input>
        </el-form-item>
        
        <el-form-item label="参保人姓名" prop="insuredName">
          <el-input v-model="form.insuredName" placeholder="请输入参保人姓名"></el-input>
        </el-form-item>
        
        <el-form-item label="医疗机构名称" prop="medicalName">
          <el-input v-model="form.medicalName" placeholder="请输入医疗机构名称"></el-input>
        </el-form-item>
        
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址"></el-input>
        </el-form-item>
        
        <el-form-item label="总费用" prop="totalCost">
          <el-input-number 
            v-model="form.totalCost" 
            :precision="2" 
            :step="0.1" 
            :min="0"
            placeholder="请输入总费用">
          </el-input-number>
        </el-form-item>
        
        <el-form-item label="发票号码" prop="invoiceNo">
          <el-input v-model="form.invoiceNo" placeholder="请输入发票号码"></el-input>
        </el-form-item>
        
        <el-form-item label="日期" prop="date">
          <el-date-picker
            v-model="form.date"
            type="date"
            placeholder="选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD">
          </el-date-picker>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm">提交</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { addReimbursement } from '@/api/reimbursement'

export default {
  name: 'ReimbursementForm',
  setup() {
    const formRef = ref(null)
    
    const form = reactive({
      cardId: '',
      insuredName: '',
      medicalName: '',
      address: '',
      totalCost: 0,
      invoiceNo: '',
      date: '',
      isReimbursement: '未审核',
      isRemit: '未汇款'
    })
    
    const rules = {
      cardId: [
        { required: true, message: '请输入卡号', trigger: 'blur' }
      ],
      insuredName: [
        { required: true, message: '请输入参保人姓名', trigger: 'blur' }
      ],
      medicalName: [
        { required: true, message: '请输入医疗机构名称', trigger: 'blur' }
      ],
      address: [
        { required: true, message: '请输入地址', trigger: 'blur' }
      ],
      totalCost: [
        { required: true, message: '请输入总费用', trigger: 'blur' }
      ],
      invoiceNo: [
        { required: true, message: '请输入发票号码', trigger: 'blur' }
      ],
      date: [
        { required: true, message: '请选择日期', trigger: 'change' }
      ]
    }
    
    const submitForm = async () => {
      if (!formRef.value) return
      
      await formRef.value.validate(async (valid) => {
        if (valid) {
          try {
            const response = await addReimbursement(form)
            if (response.code === 200) {
              ElMessage.success('提交成功')
              resetForm()
            } else {
              ElMessage.error(response.message || '提交失败')
            }
          } catch (error) {
            ElMessage.error('提交失败：' + error.message)
          }
        }
      })
    }
    
    const resetForm = () => {
      if (!formRef.value) return
      formRef.value.resetFields()
    }
    
    return {
      formRef,
      form,
      rules,
      submitForm,
      resetForm
    }
  }
}
</script>

<style scoped>
.reimbursement-form {
  max-width: 800px;
  margin: 20px auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.el-form {
  margin-top: 20px;
}
</style> 