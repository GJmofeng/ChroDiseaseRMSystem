package com.gxwz.syschronicdisease.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gxwz.syschronicdisease.entity.Division;
import com.gxwz.syschronicdisease.entity.Result;
import com.gxwz.syschronicdisease.service.DivisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/division")
public class DivisionController {

    @Autowired
    private DivisionService divisionService;

    @GetMapping("/getall")
    public Result getAll(@RequestParam String dname){

        QueryWrapper<Division> queryWrapper = new QueryWrapper<>();

        if (dname != null){
            queryWrapper.like("dname",dname);
        }
        return new Result("查询成功",divisionService.list(queryWrapper),200);

    }

    @GetMapping("/getone/{id}")
    public Result getOne(@PathVariable Long id) {
        QueryWrapper<Division> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        return new Result("查询成功",divisionService.getOne(queryWrapper),200);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Division division){
        Division serviceOne = divisionService.getOne(new QueryWrapper<Division>().eq("dname", division.getDname()));
        if (serviceOne != null) {
            return new Result("添加失败，该区域已存在",null,205);
        }
        return new Result("添加成功",divisionService.save(division),200);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Division division){

        return new Result("修改成功",divisionService.updateById(division),200);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        return new Result("删除成功", divisionService.removeById(id), 200);
    }
}
