const express = require('express')
const router = express.Router()
const ReimbursementAuditController = require('../controller/ReimbursementAuditController')

// 获取报销审核列表
router.get('/audit/list', ReimbursementAuditController.getAuditList)

// 更新报销审核状态
router.put('/audit/update', ReimbursementAuditController.updateAuditStatus)

module.exports = router 