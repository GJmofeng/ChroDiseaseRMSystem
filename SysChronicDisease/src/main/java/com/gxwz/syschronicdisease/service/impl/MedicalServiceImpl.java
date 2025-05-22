package com.gxwz.syschronicdisease.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxwz.syschronicdisease.entity.Medical;
import com.gxwz.syschronicdisease.mapper.MedicalMapper;
import com.gxwz.syschronicdisease.service.MedicalService;
import org.springframework.stereotype.Service;

@Service
public class MedicalServiceImpl extends ServiceImpl<MedicalMapper, Medical> implements MedicalService {
}
