package com.gxwz.syschronicdisease.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gxwz.syschronicdisease.entity.Menu;

public interface MenuService extends IService<Menu> {

    void removeByUserId(String userid);
}
