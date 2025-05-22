package com.gxwz.syschronicdisease.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxwz.syschronicdisease.entity.Medical;
import com.gxwz.syschronicdisease.service.MedicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medical")
public class MedicalController {

    @Autowired
    private MedicalService medicalService;

    @GetMapping("/list")
    public IPage<Medical> list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "diseaseCode", required = false) String diseaseCode) {
        QueryWrapper<Medical> wrapper = new QueryWrapper<>();
        if (diseaseCode != null && !diseaseCode.isEmpty()) {
            wrapper.like(Medical::getDiseaseCode, diseaseCode);
        }
        wrapper.orderByAsc(Medical::getId);
        Page<Medical> pageInfo = new Page<>(page, pageSize);
        return medicalService.page(pageInfo, wrapper);
    }

    @PostMapping("/add")
    public String add(@RequestBody Medical medical) {
        medicalService.save(medical);
        return "添加成功";
    }

    @PostMapping("/update")
    public String update(@RequestBody Medical medical) {
        medicalService.updateById(medical);
        return "更新成功";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        medicalService.removeById(id);
        return "删除成功";
    }
} 