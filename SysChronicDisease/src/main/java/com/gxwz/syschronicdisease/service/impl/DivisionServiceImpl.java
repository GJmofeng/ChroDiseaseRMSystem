package com.gxwz.syschronicdisease.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxwz.syschronicdisease.entity.Division;
import com.gxwz.syschronicdisease.mapper.DivisionMapper;
import com.gxwz.syschronicdisease.service.DivisionService;
import org.springframework.stereotype.Service;

@Service
public class DivisionServiceImpl extends ServiceImpl<DivisionMapper, Division> implements DivisionService {
}
