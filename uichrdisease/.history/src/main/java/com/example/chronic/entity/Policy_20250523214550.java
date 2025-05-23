package com.example.chronic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("chronic_policy")
public class Policy {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 年度
     */
    private Integer year;
    
    /**
     * 封顶线（元）
     */
    private BigDecimal capAmount;
    
    /**
     * 报销比例（%）
     */
    private BigDecimal reimbursementRate;
} 