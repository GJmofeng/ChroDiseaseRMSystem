package com.gxwz.syschronicdisease.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxwz.syschronicdisease.entity.Menu;
import com.gxwz.syschronicdisease.mapper.MenuMapper;
import com.gxwz.syschronicdisease.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public void removeByUserId(String userid) {
        menuMapper.deleteByUserId(userid);
    }
}
