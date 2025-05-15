package com.gxwz.syschronicdisease.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxwz.syschronicdisease.entity.Insured;
import com.gxwz.syschronicdisease.mapper.InsuredMapper;
import com.gxwz.syschronicdisease.service.InsuredService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InsuredServiceImpl extends ServiceImpl<InsuredMapper, Insured> implements InsuredService {

    @Autowired
    private InsuredMapper insuredMapper;


}
