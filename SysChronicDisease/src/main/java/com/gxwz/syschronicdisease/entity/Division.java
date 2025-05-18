package com.gxwz.syschronicdisease.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("division")
public class Division {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long parent;

    private String dname;

    private String level;

    private Integer sort;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public Division() {
    }

    public Division(Long id, Long parent, String dname, String level, Integer sort, LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.parent = parent;
        this.dname = dname;
        this.level = level;
        this.sort = sort;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Division{" +
                "id=" + id +
                ", parent=" + parent +
                ", dname='" + dname + '\'' +
                ", level='" + level + '\'' +
                ", sort=" + sort +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
