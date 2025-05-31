package com.gxwz.syschronicdisease.controller;

import com.gxwz.syschronicdisease.entity.User;
import com.gxwz.syschronicdisease.service.UserService;
import com.gxwz.syschronicdisease.util.JwtUtil;
import com.gxwz.syschronicdisease.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
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
        // 根据userid查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid", user.getUserid());
        User userResult = userService.getOne(queryWrapper);
        
        if (userResult == null) {
            return new Result("用户不存在", null, 404);
        }
        
        // 验证密码
        if (!passwordEncoder.matches(user.getPassword(), userResult.getPassword())) {
            return new Result("密码错误", null, 400);
        }
        
        // 生成token
        String token = UUID.randomUUID().toString().replace("-", "");
        
        // 返回用户信息（不包含密码）和token
        userResult.setPassword(null);
        Map<String, Object> data = new HashMap<>();
        data.put("user", userResult);
        data.put("token", token);
        
        return new Result("登录成功", data, 200);
    }
} 