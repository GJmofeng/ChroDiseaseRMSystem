package com.gxwz.syschronicdisease.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("medical")
public class Medical {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("disease_name")
    private String diseaseName;

    @TableField("disease_code")
    private String diseaseCode;

    @TableField("disease_type")
    private String diseaseType;

    @TableField("description")
    private String description;

    @TableField("create_time")
    private LocalDateTime createTime;

    public Medical() {
    }

    public Medical(Integer id, String diseaseName, String diseaseCode, String diseaseType, String description, LocalDateTime createTime) {
        this.id = id;
        this.diseaseName = diseaseName;
        this.diseaseCode = diseaseCode;
        this.diseaseType = diseaseType;
        this.description = description;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getDiseaseCode() {
        return diseaseCode;
    }

    public void setDiseaseCode(String diseaseCode) {
        this.diseaseCode = diseaseCode;
    }

    public String getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Medical{" +
                "id=" + id +
                ", diseaseName='" + diseaseName + '\'' +
                ", diseaseCode='" + diseaseCode + '\'' +
                ", diseaseType='" + diseaseType + '\'' +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
