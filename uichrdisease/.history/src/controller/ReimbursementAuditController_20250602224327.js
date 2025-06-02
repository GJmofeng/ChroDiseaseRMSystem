const ReimbursementAuditService = require('../service/ReimbursementAuditService')

class ReimbursementAuditController {
  // 获取报销审核列表
  async getAuditList(req, res) {
    try {
      const result = await ReimbursementAuditService.getAuditList()
      res.json({
        code: 200,
        message: '查询成功',
        data: result
      })
    } catch (error) {
      res.json({
        code: 500,
        message: '查询失败：' + error.message
      })
    }
  }

  // 更新报销审核状态
  async updateAuditStatus(req, res) {
    try {
      const { id, isReimbursement, auditTime } = req.body
      const result = await ReimbursementAuditService.updateAuditStatus(id, isReimbursement, auditTime)
      res.json({
        code: 200,
        message: '更新成功',
        data: result
      })
    } catch (error) {
      res.json({
        code: 500,
        message: '更新失败：' + error.message
      })
    }
  }
}

module.exports = new ReimbursementAuditController() 