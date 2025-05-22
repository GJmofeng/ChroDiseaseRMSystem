package com.example.uichrdisease.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.uichrdisease.entity.ChronicCert;
import com.example.uichrdisease.mapper.ChronicCertMapper;
import com.example.uichrdisease.service.ChronicCertService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ChronicCertServiceImpl extends ServiceImpl<ChronicCertMapper, ChronicCert> implements ChronicCertService {

    @Override
    public IPage<ChronicCert> page(Integer page, Integer pageSize, String certNo, String patientName, String status) {
        LambdaQueryWrapper<ChronicCert> wrapper = new LambdaQueryWrapper<>();
        
        // 添加查询条件
        if (StringUtils.hasText(certNo)) {
            wrapper.like(ChronicCert::getCertNo, certNo);
        }
        if (StringUtils.hasText(patientName)) {
            wrapper.like(ChronicCert::getPatientName, patientName);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(ChronicCert::getStatus, status);
        }
        
        // 按创建时间降序排序
        wrapper.orderByDesc(ChronicCert::getCreateTime);
        
        return page(new Page<>(page, pageSize), wrapper);
    }
} 