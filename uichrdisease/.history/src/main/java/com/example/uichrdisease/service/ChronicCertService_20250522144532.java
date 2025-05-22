package com.example.uichrdisease.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.uichrdisease.entity.ChronicCert;

public interface ChronicCertService extends IService<ChronicCert> {
    
    /**
     * 分页查询慢性病证
     * @param page 页码
     * @param pageSize 每页大小
     * @param certNo 证件号码
     * @param patientName 患者姓名
     * @param status 状态
     * @return 分页结果
     */
    IPage<ChronicCert> page(Integer page, Integer pageSize, String certNo, String patientName, String status);
} 