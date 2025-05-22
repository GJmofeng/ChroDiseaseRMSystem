package com.gxwz.syschronicdisease.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * @author hgj
 * @date 2021/12/2 16:43
 */
@TableName("medical_card")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalCard {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(value = "cardId")
    private String cardId;
    @TableField(value = "insured_name")
    private String insuredName;
    @TableField(value = "medical_name")
    private String medicalName;
    @TableField(value = "start_time")
    private Date startTime;
    @TableField(value = "end_time")
    private Date endTime;
}
