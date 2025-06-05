package com.gxwz.syschronicdisease.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxwz.syschronicdisease.entity.Menu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    @Delete("delete from menu where userid=#{userid}")
    void deleteByUserId(String userid);
}
