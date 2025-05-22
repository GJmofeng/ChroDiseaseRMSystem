package com.example.uichrdisease.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("chronic_cert")
public class ChronicCert {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    // 证件号码
    private String certNo;
    
    // 患者姓名
    private String patientName;
    
    // 身份证号
    private String idCard;
    
    // 疾病类型
    private String diseaseType;
    
    // 发证日期
    private LocalDate issueDate;
    
    // 有效期
    private LocalDate validityPeriod;
    
    // 状态（1：有效，0：已过期）
    private String status;
    
    // 创建时间
    private LocalDateTime createTime;
    
    // 更新时间
    private LocalDateTime updateTime;
    
    // 创建人
    private String createBy;
    
    // 更新人
    private String updateBy;
} 