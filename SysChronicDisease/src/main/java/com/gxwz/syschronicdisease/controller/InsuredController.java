package com.gxwz.syschronicdisease.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxwz.syschronicdisease.entity.Insured;
import com.gxwz.syschronicdisease.entity.Result;
import com.gxwz.syschronicdisease.service.InsuredService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/insureds")
public class InsuredController {

    @Autowired
    private InsuredService insuredService;

    @GetMapping("/page")
    public Result getInsuredsByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        // 创建分页对象
        Page<Insured> page = new Page<>(pageNum, pageSize);
        // 创建查询条件（如果需要）
        QueryWrapper<Insured> queryWrapper = new QueryWrapper<>();
        // 可以添加排序
        queryWrapper.orderByDesc("id");

        // 执行分页查询
        Page<Insured> pageResult = insuredService.page(page, queryWrapper);

        return new Result("查询成功", pageResult, 200);
    }

    @GetMapping("/getAll")
    public Result getAllInsureds(){
        return new Result("查询成功",insuredService.list(),200);
    }

    @PostMapping("/add")
    public Result addInsured(@RequestBody Insured insured) {
        Insured insured1 = insuredService.getOne(new QueryWrapper<Insured>().eq("id", insured.getId()));
        if (insured1 != null) {
            return new Result("用户已存在", null, 500);
        }else {
            return new Result("添加成功", insuredService.save(insured), 200);
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteInsured(@PathVariable("id") Integer id) {

        return new Result("删除成功", insuredService.removeById(id), 200);
    }

    @PostMapping("/update")
    public Result updateInsured(@RequestBody Insured insured) {

        return new Result("修改成功", insuredService.updateById(insured), 200);
    }

    @GetMapping("/getInsured/{cardId}")
    public Result getInsured(@PathVariable("cardId") String cardId) {
        return new Result("查询成功", insuredService.getOne(new QueryWrapper<Insured>().eq("card_id", cardId)), 200);
    }
}
