package com.gxwz.syschronicdisease.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userid;
    private String href;
}
