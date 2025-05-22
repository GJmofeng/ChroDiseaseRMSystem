package com.example.uichrdisease.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.uichrdisease.common.Result;
import com.example.uichrdisease.entity.ChronicCert;
import com.example.uichrdisease.service.ChronicCertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chronic-cert")
public class ChronicCertController {

    @Autowired
    private ChronicCertService chronicCertService;

    /**
     * 分页查询慢性病证列表
     */
    @GetMapping("/list")
    public Result<IPage<ChronicCert>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String certNo,
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) String status
    ) {
        IPage<ChronicCert> pageResult = chronicCertService.page(page, pageSize, certNo, patientName, status);
        return Result.success(pageResult);
    }

    /**
     * 获取慢性病证详情
     */
    @GetMapping("/{id}")
    public Result<ChronicCert> getById(@PathVariable Long id) {
        return Result.success(chronicCertService.getById(id));
    }

    /**
     * 新增慢性病证
     */
    @PostMapping
    public Result<Boolean> add(@RequestBody ChronicCert chronicCert) {
        return Result.success(chronicCertService.save(chronicCert));
    }

    /**
     * 更新慢性病证
     */
    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody ChronicCert chronicCert) {
        chronicCert.setId(id);
        return Result.success(chronicCertService.updateById(chronicCert));
    }

    /**
     * 删除慢性病证
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(chronicCertService.removeById(id));
    }
} 