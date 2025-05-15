package com.gxwz.syschronicdisease.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxwz.syschronicdisease.entity.Insured;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface InsuredMapper extends BaseMapper<Insured> {

    @Update("update insured set insured_name=#{insured_name},gender=#{gender}," +
            "phone=#{phone},nation=#{nation},address=#{address} where card_id = #{card_id} ")
    void updateInsured(Insured insured);
}
