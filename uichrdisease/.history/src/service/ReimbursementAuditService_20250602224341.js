const db = require('../utils/db')

class ReimbursementAuditService {
  // 获取报销审核列表
  async getAuditList() {
    const sql = `
      SELECT 
        r.id,
        r.card_id as cardId,
        r.insured_name as insuredName,
        r.medical_name as medicalName,
        r.address,
        r.total_cost as totalCost,
        r.reimbursement_amount as reimbursementAmount,
        r.invoice_no as invoiceNo,
        r.date,
        r.is_reimbursement as isReimbursement,
        r.audit_time as auditTime
      FROM reimbursement r
      ORDER BY 
        CASE 
          WHEN r.is_reimbursement = '未审核' THEN 1
          ELSE 2
        END,
        r.date DESC
    `
    try {
      const result = await db.query(sql)
      return result
    } catch (error) {
      throw new Error('获取报销审核列表失败：' + error.message)
    }
  }

  // 更新报销审核状态
  async updateAuditStatus(id, isReimbursement, auditTime) {
    const sql = `
      UPDATE reimbursement 
      SET 
        is_reimbursement = ?,
        audit_time = ?
      WHERE id = ?
    `
    try {
      const result = await db.query(sql, [isReimbursement, auditTime, id])
      if (result.affectedRows === 0) {
        throw new Error('未找到对应的报销记录')
      }
      return { id, isReimbursement, auditTime }
    } catch (error) {
      throw new Error('更新报销审核状态失败：' + error.message)
    }
  }
}

module.exports = new ReimbursementAuditService() 