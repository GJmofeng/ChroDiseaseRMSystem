package com.example.uichrdisease.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("chronic_cert")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChronicCert {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField(value = "cert_no")
    private String certNo;
    
    @TableField(value = "patient_name")
    private String patientName;
    
    @TableField(value = "id_card")
    private String idCard;
    
    @TableField(value = "disease_type")
    private String diseaseType;
    
    @TableField(value = "issue_date")
    private Date issueDate;
    
    @TableField(value = "validity_period")
    private Date validityPeriod;
    
    @TableField(value = "status")
    private String status;
    
    @TableField(value = "create_time")
    private Date createTime;
    
    @TableField(value = "update_time")
    private Date updateTime;
    
    @TableField(value = "create_by")
    private String createBy;
    
    @TableField(value = "update_by")
    private String updateBy;
} 