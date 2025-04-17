package com.gxwz.syschronicdisease.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxwz.syschronicdisease.entity.User;
import com.gxwz.syschronicdisease.mapper.UserMapper;
import com.gxwz.syschronicdisease.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
