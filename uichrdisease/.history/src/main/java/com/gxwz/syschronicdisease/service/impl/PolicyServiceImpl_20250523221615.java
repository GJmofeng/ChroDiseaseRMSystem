package com.gxwz.syschronicdisease.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxwz.syschronicdisease.entity.Policy;
import com.gxwz.syschronicdisease.mapper.PolicyMapper;
import com.gxwz.syschronicdisease.service.PolicyService;
import org.springframework.stereotype.Service;

@Service
public class PolicyServiceImpl extends ServiceImpl<PolicyMapper, Policy> implements PolicyService {
} 