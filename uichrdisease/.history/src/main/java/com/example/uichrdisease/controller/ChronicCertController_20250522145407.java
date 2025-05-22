package com.example.uichrdisease.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.uichrdisease.entity.ChronicCert;
import com.example.uichrdisease.entity.Result;
import com.example.uichrdisease.service.ChronicCertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chronicCert")
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

    @GetMapping("/getAll")
    public Result getAll(@RequestParam(required = false) String patientName) {
        return new Result("查询成功", chronicCertService.list(), 200);
    }

    @PostMapping("/add")
    public Result add(@RequestBody ChronicCert chronicCert) {
        ChronicCert existCert = chronicCertService.getOne(
            new QueryWrapper<ChronicCert>().eq("cert_no", chronicCert.getCertNo())
        );
        if (existCert != null) {
            return new Result("已存在", null, 200);
        } else {
            return new Result("添加成功", chronicCertService.save(chronicCert), 200);
        }
    }

    @PostMapping("/update")
    public Result update(@RequestBody ChronicCert chronicCert) {
        return new Result("修改成功", chronicCertService.updateById(chronicCert), 200);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id) {
        return new Result("删除成功", chronicCertService.removeById(id), 200);
    }

    @GetMapping("/getCert/{certNo}")
    public Result getCert(@PathVariable("certNo") String certNo) {
        List<ChronicCert> list = chronicCertService.list(
            new QueryWrapper<ChronicCert>().eq("cert_no", certNo)
        );
        return new Result("查询成功", list, 200);
    }

    @GetMapping("/getByPatient/{patientName}")
    public Result getByPatient(@PathVariable("patientName") String patientName) {
        List<ChronicCert> list = chronicCertService.list(
            new QueryWrapper<ChronicCert>().eq("patient_name", patientName)
        );
        return new Result("查询成功", list, 200);
    }
} 