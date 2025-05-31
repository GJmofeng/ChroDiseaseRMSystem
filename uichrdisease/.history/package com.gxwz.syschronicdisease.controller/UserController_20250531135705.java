package com.gxwz.syschronicdisease.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid", user.getUserid());
        User serviceOne = userService.getOne(queryWrapper);
        
        if (serviceOne != null && passwordEncoder.matches(user.getPassword(), serviceOne.getPassword())) {
            // 生成 token
            String token = UUID.randomUUID().toString().replace("-", "");
            // 将 token 存入用户信息中
            serviceOne.setToken(token);
            // 清除密码
            serviceOne.setPassword(null);
            return new Result("登录成功", serviceOne, 200);
        } else {
            return new Result("用户名或密码错误", null, 401);
        }
    }
} 