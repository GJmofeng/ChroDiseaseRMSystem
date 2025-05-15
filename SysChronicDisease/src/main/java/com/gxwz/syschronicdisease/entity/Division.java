package com.gxwz.syschronicdisease.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("division")

public class Division {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long parent;

    private String dname;

    private String level;

    public Division() {
    }

    public Division(Long id, Long parent, String dname, String level) {
        this.id = id;
        this.parent = parent;
        this.dname = dname;
        this.level = level;
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

    @Override
    public String toString() {
        return "Division{" +
                "id=" + id +
                ", parent=" + parent +
                ", dname='" + dname + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
