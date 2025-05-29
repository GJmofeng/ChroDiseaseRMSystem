package com.gxwz.syschronicdisease.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@TableName("reimbursement")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reimbursement {

    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("card_id")
    private String cardId;
    @TableField("insured_name")
    private String insuredName;
    @TableField("medical_name")
    private String medicalName;
    @TableField("address")
    private String address;
    @TableField("reimbursement_amount")
    private Double reimbursementAmount;
    @TableField("total_cost")
    private Double totalCost;
    @TableField("invoice_no")
    private String invoiceNo;
    @TableField("date")
    private Date date;
    @TableField("is_reimbursement")
    private String isReimbursement;
    @TableField("is_remit")
    private String isRemit;
}
