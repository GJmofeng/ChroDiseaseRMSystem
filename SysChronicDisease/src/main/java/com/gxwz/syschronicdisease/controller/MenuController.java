package com.gxwz.syschronicdisease.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gxwz.syschronicdisease.entity.Menu;
import com.gxwz.syschronicdisease.entity.Result;
import com.gxwz.syschronicdisease.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public Result getMenuList() {
        return new Result("查询成功", menuService.list(),200);
    }

    @GetMapping("/getRole/{userid}")
    public Result getRole(@PathVariable("userid") String userid){

        QueryWrapper<Menu> queryWrapper = new QueryWrapper<Menu>();
        queryWrapper.eq("userid",userid);
        return new Result("查询成功", menuService.list(queryWrapper),200);
    }

    @PostMapping("/add")
    public Result add(@RequestBody List<Menu> menus) {

        menuService.removeByUserId(menus.get(0).getUserid());

        for (Menu menu : menus) {
            menuService.save(menu);
        }
        return new Result("添加成功", null,200);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Menu menu) {
        return new Result("修改成功", menuService.updateById(menu),200);
    }

    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id) {
        return new Result("删除成功", menuService.removeById(id),200);
    }
}
