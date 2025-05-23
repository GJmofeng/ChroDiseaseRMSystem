package com.gxwz.syschronicdisease.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gxwz.syschronicdisease.entity.Policy;
import com.gxwz.syschronicdisease.entity.Result;
import com.gxwz.syschronicdisease.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policy")
public class PolicyController {

    @Autowired
    private PolicyService policyService;

    @GetMapping("/list")
    public Result list() {
        LambdaQueryWrapper<Policy> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Policy::getYear);
        List<Policy> list = policyService.list(wrapper);
        return new Result("获取成功", list, 200);
    }

    @PostMapping
    public Result add(@RequestBody Policy policy) {
        policyService.save(policy);
        return new Result("添加成功", null, 200);
    }

    @PutMapping
    public Result update(@RequestBody Policy policy) {
        policyService.updateById(policy);
        return new Result("更新成功", null, 200);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        policyService.removeById(id);
        return new Result("删除成功", null, 200);
    }
} 