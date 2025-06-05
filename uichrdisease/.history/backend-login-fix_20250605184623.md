# 后端登录接口修复建议

根据您提供的 `UserController` 代码，我发现登录接口已经正确返回了用户信息，但我们需要确保数据库中的用户有正确的角色值。以下是一些建议：

## 1. 确保数据库中有超级管理员用户

在数据库中执行以下SQL语句，将某个用户设置为超级管理员：

```sql
-- 查询现有用户
SELECT id, userid, fullname, role FROM user;

-- 将指定用户设置为超级管理员
UPDATE user SET role = 'superadmin' WHERE userid = 'admin';
-- 或者
UPDATE user SET role = 'superadmin' WHERE id = 1;
```

## 2. 检查用户表中的角色字段

确保 `user` 表中有 `role` 字段，并且该字段可以存储角色值：

```sql
-- 查看表结构
DESCRIBE user;

-- 如果没有role字段，添加该字段
ALTER TABLE user ADD COLUMN role VARCHAR(50) DEFAULT 'user';
```

## 3. 添加调试日志

在 `UserController` 的登录方法中添加调试日志，确认返回的用户信息中包含角色：

```java
@PostMapping("/login")
public Result login(@RequestBody User user) {
    // 根据userid查询用户
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("userid", user.getUserid());
    User userResult = userService.getOne(queryWrapper);

    if (userResult == null) {
        return new Result("用户不存在", null, 404);
    }

    // 验证密码
    if (!passwordEncoder.matches(user.getPassword(), userResult.getPassword())) {
        return new Result("密码错误", null, 400);
    }

    // 生成token
    String token = UUID.randomUUID().toString().replace("-", "");

    // 添加调试日志
    System.out.println("用户登录成功: " + userResult.getUserid());
    System.out.println("用户角色: " + userResult.getRole());

    // 返回用户信息（不包含密码）和token
    userResult.setPassword(null);
    Map<String, Object> data = new HashMap<>();
    data.put("user", userResult);
    data.put("token", token);

    return new Result("登录成功", data, 200);
}
```

## 4. 创建一个超级管理员用户

如果数据库中还没有超级管理员用户，可以使用以下SQL语句创建一个：

```sql
-- 创建超级管理员用户（密码需要加密）
INSERT INTO user (userid, password, fullname, role) 
VALUES ('superadmin', '$2a$10$YourEncryptedPasswordHere', '超级管理员', 'superadmin');
```

或者通过您的注册接口创建一个用户，然后将其角色更新为超级管理员。

## 5. 测试步骤

1. 确认数据库中有超级管理员用户
2. 使用该用户登录系统
3. 在浏览器控制台中检查登录响应，确认返回的用户信息中包含 `role` 字段，且值为 `superadmin`
4. 尝试访问权限管理页面

如果按照上述步骤操作后仍然无法访问权限管理页面，请检查浏览器控制台中的错误信息，并确认 localStorage 中存储的用户信息是否正确。 