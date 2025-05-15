package com.gxwz.syschronicdisease.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gxwz.syschronicdisease.entity.MedicalCard;
import com.gxwz.syschronicdisease.entity.Result;
import com.gxwz.syschronicdisease.service.MedicalCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicalCard")
public class MedicalCardController {

    @Autowired
   private MedicalCardService medicalCardService;

    @GetMapping("/getAll")
    public Result getAll(@RequestParam String fullname) {
       return new Result("查询成功",medicalCardService.list(), 200);
    }

    @PostMapping("/add")
    public Result add(@RequestBody MedicalCard medicalCard) {
        MedicalCard serviceOne= medicalCardService.getOne(new QueryWrapper<MedicalCard>().eq("id",medicalCard.getId()));
        if (serviceOne != null) {
            return new Result("已存在",null, 200);
        }else {
        return new Result("添加成功",medicalCardService.save(medicalCard), 200);
        }
    }
    @PostMapping("/update")
    public Result update(@RequestBody MedicalCard medicalCard) {
        return new Result("修改成功", medicalCardService.updateById(medicalCard), 200);
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id) {
        return new Result("删除成功", medicalCardService.removeById(id), 200);
    }

    @GetMapping("/getMedical/{cardId}")
    public Result getMedical(@PathVariable("cardId") String cardId) {
        List<MedicalCard> list = medicalCardService.list(new QueryWrapper<MedicalCard>().eq("cardId",cardId));
        return new Result("查询成功",list, 200);
    }
}
