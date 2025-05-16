package com.gxwz.syschronicdisease.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxwz.syschronicdisease.entity.Result;
import com.gxwz.syschronicdisease.entity.User;
import com.gxwz.syschronicdisease.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
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

    @GetMapping("/list")
    public Map<String, Object> getUserList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status
    ) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (search != null && !search.isEmpty()) {
            wrapper.like("fullname", search)
                    .or().like("userid", search);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        Page<User> userPage = new Page<>(page, pageSize);
        userService.page(userPage, wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("data", userPage.getRecords());
        result.put("total", userPage.getTotal());
        return result;
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
