package com.example.chronic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.chronic.entity.Policy;
import com.example.chronic.mapper.PolicyMapper;
import com.example.chronic.service.PolicyService;
import org.springframework.stereotype.Service;

@Service
public class PolicyServiceImpl extends ServiceImpl<PolicyMapper, Policy> implements PolicyService {
} 