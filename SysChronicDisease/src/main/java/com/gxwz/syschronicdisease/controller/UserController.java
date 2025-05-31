package com.gxwz.syschronicdisease.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxwz.syschronicdisease.entity.Result;
import com.gxwz.syschronicdisease.entity.User;
import com.gxwz.syschronicdisease.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    // 密码加密器
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Result getAll(@RequestParam String fullname) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (fullname != null) {
            queryWrapper.like("fullname", fullname);
        }
        return new Result("查询成功",userService.list(queryWrapper), 200);
    }

    @GetMapping("/getOne/{id}")
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

    @PostMapping("/add")
    public Result addUser(@RequestBody User user) {
        // 账号唯一性校验
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userid", user.getUserid());
        if (userService.getOne(wrapper) != null) {
            return new Result("该账号已存在", null, 400);
        }
        // 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        boolean success = userService.save(user);
        if (success) {
            // 返回新用户ID
            return new Result("添加成功", user.getId(), 200);
        } else {
            return new Result("添加失败", null, 500);
        }
    }

    // 批量删除
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        boolean success = userService.removeByIds(ids);
        if (success) {
            return new Result("批量删除成功", null, 200);
        } else {
            return new Result("批量删除失败", null, 500);
        }
    }

    @PostMapping("/update")
    public Result updateUser(@RequestBody User user) {
        // 只更新有变动的字段
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", user.getId());
        wrapper.set("fullname", user.getFullname());
        wrapper.set("role", user.getRole());
        // 如果密码不为空，更新密码
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            wrapper.set("password", passwordEncoder.encode(user.getPassword()));
        }
        boolean success = userService.update(wrapper);
        if (success) {
            return new Result("编辑成功", null, 200);
        } else {
            return new Result("编辑失败", null, 500);
        }
    }

    @PostMapping("/resetPwd")
    public Result resetPwd(@RequestBody Map<String, Object> map) {
        Integer id = (Integer) map.get("id");
        if (id == null) {
            return new Result("参数错误", null, 400);
        }
        // 密码加密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String newPwd = encoder.encode("123456");
        User user = new User();
        user.setId(id);
        user.setPassword(newPwd);
        boolean success = userService.updateById(user);
        if (success) {
            return new Result("重置成功", null, 200);
        } else {
            return new Result("重置失败", null, 500);
        }
    }

//    @PostMapping("/login")
//    public Result login(@RequestBody User user) {
//
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("userid", user.getUserid());
//        queryWrapper.eq("password", user.getPassword());
//        User serviceOne = userService.getOne(queryWrapper);
//        if (serviceOne != null) {
//            return new Result("登录成功",serviceOne, 200);
//        }else {
//            return new Result("登录失败", null, 404);
//        }
//    }

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
