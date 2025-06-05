# 后端 MenuController 修复指南

根据错误日志，问题出在 `com.gxwz.syschronicdisease.controller.MenuController` 类的第28行，发生了空指针异常（NullPointerException）。

检查提供的代码，发现 `menuService` 字段没有被正确初始化。这是因为缺少 `@Autowired` 注解来注入依赖。

## 修复步骤

请在后端项目中找到 `MenuController.java` 文件，并按照以下方式修改：

```java
package com.gxwz.syschronicdisease.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gxwz.syschronicdisease.entity.Menu;
import com.gxwz.syschronicdisease.entity.Result;
import com.gxwz.syschronicdisease.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;  // 添加这一行
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired  // 添加这个注解
    private MenuService menuService;

    @GetMapping("/list")
    public Result getMenuList() {
        return new Result("查询成功", menuService.list(),200);
    }

    @GetMapping("/getRole/{userid}")
    public Result getRole(@PathVariable("userid") String userid){
        // 添加空值检查
        if (userid == null) {
            return new Result("用户ID不能为空", null, 400);
        }

        QueryWrapper<Menu> queryWrapper = new QueryWrapper<Menu>();
        queryWrapper.eq("userid",userid);
        return new Result("查询成功", menuService.list(queryWrapper),200);
    }

    @PostMapping("/add")
    public Result add(@RequestBody List<Menu> menus) {
        if (menus == null || menus.isEmpty()) {
            return new Result("菜单列表不能为空", null, 400);
        }

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
```

## 关键修改

1. 添加 `@Autowired` 注解到 `menuService` 字段上，确保 Spring 能够自动注入该依赖
2. 添加参数验证，确保 `userid` 不为空
3. 添加对 `menus` 列表的空值检查

这些修改将解决 NullPointerException 问题，并提高代码的健壮性。

## 注意事项

确保 `MenuService` 接口和其实现类已正确定义并被 Spring 管理。如果 `MenuService` 实现类没有被标记为 `@Service`，也需要添加该注解。 