package com.gxwz.syschronicdisease.controller;

import com.gxwz.syschronicdisease.entity.Region;
import com.gxwz.syschronicdisease.entity.Result;
import com.gxwz.syschronicdisease.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/region")
@CrossOrigin
public class RegionController {

    @Autowired
    private RegionService regionService;

    @GetMapping("/list")
    public Result list() {
        List<Region> list = regionService.list();
        return new Result("获取成功", list, 200);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Region region) {
        boolean success = regionService.save(region);
        if (success) {
            return new Result("添加成功", null, 200);
        } else {
            return new Result("添加失败", null, 500);
        }
    }

    @PostMapping("/update")
    public Result update(@RequestBody Region region) {
        boolean success = regionService.updateById(region);
        if (success) {
            return new Result("更新成功", null, 200);
        } else {
            return new Result("更新失败", null, 500);
        }
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody Region region) {
        boolean success = regionService.removeById(region.getId());
        if (success) {
            return new Result("删除成功", null, 200);
        } else {
            return new Result("删除失败", null, 500);
        }
    }
} 