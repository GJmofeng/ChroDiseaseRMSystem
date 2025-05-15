package com.gxwz.syschronicdisease.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxwz.syschronicdisease.entity.MedicalCard;
import com.gxwz.syschronicdisease.mapper.MedicalCardMapper;
import com.gxwz.syschronicdisease.service.MedicalCardService;
import org.springframework.stereotype.Service;

@Service
public class MedicalCardServiceImpl extends ServiceImpl<MedicalCardMapper, MedicalCard> implements MedicalCardService {
}
