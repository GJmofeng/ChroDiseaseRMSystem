package com.gxwz.syschronicdisease.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("policy")
public class Policy {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 年度
     */
    @TableField("year")
    private String year;

    /**
     * 封顶线（元）
     */
    @TableField("max")
    private double max;

    /**
     * 报销比例（%）
     */
    @TableField("percentage")
    private double percentage;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
}
