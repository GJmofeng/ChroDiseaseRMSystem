package com.gxwz.syschronicdisease.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gxwz.syschronicdisease.entity.Result;
import com.gxwz.syschronicdisease.entity.User;
import com.gxwz.syschronicdisease.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    public Result getAll(@RequestParam String fullname) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (fullname != null) {
            queryWrapper.like("fullname", fullname);
        }
        return new Result("查询成功",userService.list(queryWrapper), 200);
    }

    @GetMapping("/getOne")
    public Result getOne(@PathVariable("id") Long id) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return new Result("查询成功",userService.getOne(queryWrapper), 200);
    }

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
