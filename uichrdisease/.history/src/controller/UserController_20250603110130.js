@PostMapping("/register")
public Result register(@RequestBody User user) {
    // 账号唯一性校验
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.eq("userid", user.getUserid());
    if (userService.getOne(wrapper) != null) {
        return new Result("该账号已存在", null, 400);
    }
    
    // 密码加密
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    
    boolean success = userService.save(user);
    if (success) {
        // 返回新用户信息（不包含密码）
        user.setPassword(null);
        return new Result("注册成功", user, 200);
    } else {
        return new Result("注册失败", null, 500);
    }
} 