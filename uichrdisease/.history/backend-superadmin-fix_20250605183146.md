# 后端超级管理员角色与权限控制实现指南

为了实现只有超级管理员才能进行权限管理设置的需求，需要在后端进行以下修改：

## 1. 修改用户实体类

在 `User` 实体类中确保有 `role` 字段，并且可以区分 `superadmin`（超级管理员）和 `admin`（普通管理员）角色：

```java
package com.gxwz.syschronicdisease.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_user")
public class User {
    private String userid;
    private String password;
    private String fullname;
    private String role;  // 确保有此字段，值可以是 "superadmin", "admin", "user" 等
    // 其他字段...
}
```

## 2. 修改 MenuController

在 `MenuController` 中添加权限检查，确保只有超级管理员才能进行权限管理操作：

```java
package com.gxwz.syschronicdisease.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gxwz.syschronicdisease.entity.Menu;
import com.gxwz.syschronicdisease.entity.Result;
import com.gxwz.syschronicdisease.entity.User;
import com.gxwz.syschronicdisease.service.MenuService;
import com.gxwz.syschronicdisease.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;
    
    @Autowired
    private UserService userService;

    // 检查是否为超级管理员
    private boolean isSuperAdmin(String userid) {
        if (userid == null) return false;
        
        User user = userService.getById(userid);
        return user != null && "superadmin".equals(user.getRole());
    }

    @GetMapping("/list")
    public Result getMenuList() {
        return new Result("查询成功", menuService.list(), 200);
    }

    @GetMapping("/getRole/{userid}")
    public Result getRole(@PathVariable("userid") String userid) {
        // 添加空值检查
        if (userid == null) {
            return new Result("用户ID不能为空", null, 400);
        }

        QueryWrapper<Menu> queryWrapper = new QueryWrapper<Menu>();
        queryWrapper.eq("userid", userid);
        return new Result("查询成功", menuService.list(queryWrapper), 200);
    }

    @PostMapping("/add")
    public Result add(@RequestBody List<Menu> menus, HttpServletRequest request) {
        // 获取当前登录用户ID
        String currentUserId = getCurrentUserId(request);
        
        // 检查权限
        if (!isSuperAdmin(currentUserId)) {
            return new Result("权限不足，只有超级管理员才能进行权限管理设置", null, 403);
        }
        
        if (menus == null || menus.isEmpty()) {
            return new Result("菜单列表不能为空", null, 400);
        }

        // 检查是否尝试给非超级管理员用户分配权限管理功能
        String targetUserId = menus.get(0).getUserid();
        User targetUser = userService.getById(targetUserId);
        
        for (Menu menu : menus) {
            // 如果目标用户不是超级管理员，且尝试分配权限管理功能，则跳过
            if (targetUser != null && !"superadmin".equals(targetUser.getRole()) 
                && menu.getHref() != null && menu.getHref().equals("/main/permission-manage")) {
                continue;
            }
            
            menuService.save(menu);
        }
        
        return new Result("添加成功", null, 200);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Menu menu, HttpServletRequest request) {
        // 获取当前登录用户ID
        String currentUserId = getCurrentUserId(request);
        
        // 检查权限
        if (!isSuperAdmin(currentUserId)) {
            return new Result("权限不足，只有超级管理员才能进行权限管理设置", null, 403);
        }
        
        return new Result("修改成功", menuService.updateById(menu), 200);
    }

    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id, HttpServletRequest request) {
        // 获取当前登录用户ID
        String currentUserId = getCurrentUserId(request);
        
        // 检查权限
        if (!isSuperAdmin(currentUserId)) {
            return new Result("权限不足，只有超级管理员才能进行权限管理设置", null, 403);
        }
        
        return new Result("删除成功", menuService.removeById(id), 200);
    }
    
    // 获取当前登录用户ID的方法（根据你的认证机制实现）
    private String getCurrentUserId(HttpServletRequest request) {
        // 这里根据你的认证机制获取当前登录用户ID
        // 例如，从JWT令牌中提取，或从Session中获取
        // 以下仅为示例
        return (String) request.getSession().getAttribute("userId");
    }
}
```

## 3. 修改数据库

确保在数据库中有超级管理员角色的用户：

```sql
-- 修改现有管理员为超级管理员
UPDATE sys_user SET role = 'superadmin' WHERE userid = 'admin';

-- 或者创建新的超级管理员
INSERT INTO sys_user (userid, password, fullname, role) 
VALUES ('superadmin', '加密后的密码', '超级管理员', 'superadmin');
```

## 4. 修改登录逻辑

确保登录时将用户角色信息返回给前端：

```java
@PostMapping("/login")
public Result login(@RequestBody LoginDTO loginDTO) {
    // 验证用户名密码...
    
    User user = userService.getByUserId(loginDTO.getUserid());
    if (user != null) {
        // 创建包含角色信息的返回对象
        Map<String, Object> data = new HashMap<>();
        data.put("userid", user.getUserid());
        data.put("fullname", user.getFullname());
        data.put("role", user.getRole());  // 确保返回角色信息
        // 其他需要的信息...
        
        return new Result("登录成功", data, 200);
    }
    
    return new Result("用户名或密码错误", null, 401);
}
```

## 5. 添加权限拦截器（可选）

为了更全面的权限控制，可以添加一个拦截器来检查权限：

```java
package com.gxwz.syschronicdisease.interceptor;

import com.gxwz.syschronicdisease.entity.User;
import com.gxwz.syschronicdisease.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SuperAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取当前用户ID
        String userId = (String) request.getSession().getAttribute("userId");
        
        // 检查是否为超级管理员
        if (userId != null) {
            User user = userService.getById(userId);
            if (user != null && "superadmin".equals(user.getRole())) {
                return true;  // 允许访问
            }
        }
        
        // 非超级管理员，返回403
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("{\"message\":\"权限不足，只有超级管理员才能进行此操作\",\"code\":403}");
        return false;
    }
}
```

然后在Spring配置中注册此拦截器：

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SuperAdminInterceptor superAdminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(superAdminInterceptor)
                .addPathPatterns("/menu/add", "/menu/update", "/menu/delete/**");
    }
}
```

## 总结

以上修改将确保：

1. 只有超级管理员角色的用户才能进行权限管理设置
2. 权限管理功能只能分配给超级管理员用户
3. 普通管理员和普通用户无法访问权限管理功能

这些修改与前端的实现相配合，完成了权限管理的完整控制。 