package com.gxwz.syschronicdisease.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxwz.syschronicdisease.entity.Medical;
import com.gxwz.syschronicdisease.entity.Result;
import com.gxwz.syschronicdisease.service.MedicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@CrossOrigin
@RestController
@RequestMapping("/disease")
public class MedicalController {

    @Autowired
    private MedicalService medicalService;

    // 分页+条件查询
    @GetMapping("/list")
    public Result list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String diseaseName,
            @RequestParam(required = false) String diseaseCode
    ) {
        try {
            LambdaQueryWrapper<Medical> wrapper = new LambdaQueryWrapper<>();
            if (diseaseName != null && !diseaseName.isEmpty()) {
                wrapper.like(Medical::getDiseaseName, diseaseName);
            }
            if (diseaseCode != null && !diseaseCode.isEmpty()) {
                wrapper.like(Medical::getDiseaseCode, diseaseCode);
            }
//            wrapper.orderByDesc(Medical::getCreateTime);
            wrapper.orderByAsc(Medical::getId);
            Page<Medical> pageInfo = new Page<>(page, pageSize);
            Page<Medical> result = medicalService.page(pageInfo, wrapper);

            // 返回数据和总数，前端分页结构
            Map<String, Object> data = new HashMap<>();
            data.put("list", result.getRecords());
            data.put("total", result.getTotal());

            return new Result("success", data, 200);
        } catch (Exception e) {
            // 捕获异常，返回错误信息和非零状态码
            e.printStackTrace(); // 打印异常堆栈到后端控制台，便于排查
            return new Result("获取慢病信息失败：" + e.getMessage(), null, 500); // 返回一个具体的错误码和信息
        }
    }

    // 新增
    @PostMapping("/add")
    public Result add(@RequestBody Medical medical) {
        medical.setCreateTime(LocalDateTime.now());
        boolean saved = medicalService.save(medical);
        return saved ? new Result("新增成功", null, 200) : new Result("新增失败", null, 500);
    }

    // 编辑
    @PutMapping("/edit")
    public Result update(@RequestBody Medical medical) {
        boolean updated = medicalService.updateById(medical);
        return updated ? new Result("编辑成功", null, 200) : new Result("编辑失败", null, 500);
    }

    // 删除
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Integer id) {
        boolean removed = medicalService.removeById(id);
        return removed ? new Result("删除成功", null, 200) : new Result("删除失败", null, 500);
    }

    // 详情
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable("id") Integer id) {
        Medical medical = medicalService.getById(id);
        return medical != null ? new Result("success", medical, 200) : new Result("未找到数据", null, 500);
    }
}