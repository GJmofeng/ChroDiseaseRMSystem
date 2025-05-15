package com.gxwz.syschronicdisease.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "insured")
public class Insured {

    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "insured_name")
    private String insuredName;
    @TableField(value = "card_id")
    private String cardId;
    private String gender;
    private String nation;
    private String address;
    private String phone;

    public Insured() {
    }

    public Insured(Integer id, String insuredName, String cardId, String gender, String nation, String address, String phone) {
        this.id = id;
        this.insuredName = insuredName;
        this.cardId = cardId;
        this.gender = gender;
        this.nation = nation;
        this.address = address;
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInsuredName() {
        return insuredName;
    }

    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Insured{" +
                "id=" + id +
                ", insuredName='" + insuredName + '\'' +
                ", cardId='" + cardId + '\'' +
                ", gender='" + gender + '\'' +
                ", nation='" + nation + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
