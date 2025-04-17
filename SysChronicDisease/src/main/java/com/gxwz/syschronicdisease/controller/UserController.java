package com.gxwz.syschronicdisease.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gxwz.syschronicdisease.entity.Result;
import com.gxwz.syschronicdisease.entity.User;
import com.gxwz.syschronicdisease.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid", user.getUserid());
        queryWrapper.eq("password", user.getPassword());
        User serviceOne = userService.getOne(queryWrapper);
        if (serviceOne != null) {
            return new Result("登录成功",serviceOne, 200);
        }else {
            return new Result("登录失败", null, 404);
        }
    }
}
