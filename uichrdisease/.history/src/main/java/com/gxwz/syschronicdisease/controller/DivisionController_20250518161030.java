package com.gxwz.syschronicdisease.controller;

import com.gxwz.syschronicdisease.entity.Division;
import com.gxwz.syschronicdisease.entity.Result;
import com.gxwz.syschronicdisease.service.DivisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/division")
@CrossOrigin
public class DivisionController {

    @Autowired
    private DivisionService divisionService;

    @GetMapping("/list")
    public Result list() {
        List<Division> list = divisionService.list();
        return new Result("获取成功", list, 200);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Division division) {
        boolean success = divisionService.save(division);
        if (success) {
            return new Result("添加成功", null, 200);
        } else {
            return new Result("添加失败", null, 500);
        }
    }

    @PostMapping("/update")
    public Result update(@RequestBody Division division) {
        boolean success = divisionService.updateById(division);
        if (success) {
            return new Result("更新成功", null, 200);
        } else {
            return new Result("更新失败", null, 500);
        }
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody Division division) {
        boolean success = divisionService.removeById(division.getId());
        if (success) {
            return new Result("删除成功", null, 200);
        } else {
            return new Result("删除失败", null, 500);
        }
    }
} 