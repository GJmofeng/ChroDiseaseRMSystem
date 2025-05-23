package com.example.chronic.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.chronic.common.Result;
import com.example.chronic.entity.Policy;
import com.example.chronic.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policy")
public class PolicyController {

    @Autowired
    private PolicyService policyService;

    @GetMapping("/list")
    public Result<List<Policy>> list() {
        LambdaQueryWrapper<Policy> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Policy::getYear);
        List<Policy> list = policyService.list(wrapper);
        return Result.success(list);
    }

    @PostMapping
    public Result<?> add(@RequestBody Policy policy) {
        policyService.save(policy);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Policy policy) {
        policyService.updateById(policy);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        policyService.removeById(id);
        return Result.success();
    }
} 