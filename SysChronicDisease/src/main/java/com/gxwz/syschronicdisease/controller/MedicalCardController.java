package com.gxwz.syschronicdisease.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxwz.syschronicdisease.entity.MedicalCard;
import com.gxwz.syschronicdisease.entity.Result;
import com.gxwz.syschronicdisease.service.MedicalCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/medicalCard")
public class MedicalCardController {

    @Autowired
    private MedicalCardService medicalCardService;

    @GetMapping("/getAll")
    public Result getAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String fullname,
            @RequestParam(required = false) String medicalName) {
        try {
            // 创建分页对象
            Page<MedicalCard> pageInfo = new Page<>(page, pageSize);

            // 创建查询条件
            QueryWrapper<MedicalCard> queryWrapper = new QueryWrapper<>();
            if (fullname != null && !fullname.trim().isEmpty()) {
                queryWrapper.like("insured_name", fullname.trim());
            }
            if (medicalName != null && !medicalName.trim().isEmpty()) {
                queryWrapper.like("medical_name", medicalName.trim());
            }

            // 执行分页查询
            Page<MedicalCard> result = medicalCardService.page(pageInfo, queryWrapper);

            // 构造返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("list", result.getRecords());
            data.put("total", result.getTotal());
            data.put("pages", result.getPages());
            data.put("current", result.getCurrent());
            data.put("size", result.getSize());

            return new Result("查询成功", data, 200);
        } catch (Exception e) {
            return new Result("查询失败：" + e.getMessage(), null, 500);
        }
    }

    @PostMapping("/add")
    public Result add(@RequestBody MedicalCard medicalCard) {
        try {
            // 检查是否已存在
            MedicalCard serviceOne = medicalCardService.getOne(
                    new QueryWrapper<MedicalCard>()
                            .eq("id", medicalCard.getId())
            );

            if (serviceOne != null) {
                return new Result("已存在", null, 200);
            }

            // 保存数据
            boolean saved = medicalCardService.save(medicalCard);
            return new Result(saved ? "添加成功" : "添加失败", null, saved ? 200 : 500);
        } catch (Exception e) {
            return new Result("添加失败：" + e.getMessage(), null, 500);
        }
    }

    @PostMapping("/update")
    public Result update(@RequestBody MedicalCard medicalCard) {
        try {
            // 检查是否存在
            MedicalCard existing = medicalCardService.getById(medicalCard.getId());
            if (existing == null) {
                return new Result("记录不存在", null, 404);
            }

            // 更新数据
            boolean updated = medicalCardService.updateById(medicalCard);
            return new Result(updated ? "修改成功" : "修改失败", null, updated ? 200 : 500);
        } catch (Exception e) {
            return new Result("修改失败：" + e.getMessage(), null, 500);
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id) {
        try {
            // 检查是否存在
            MedicalCard existing = medicalCardService.getById(id);
            if (existing == null) {
                return new Result("记录不存在", null, 404);
            }

            // 删除数据
            boolean removed = medicalCardService.removeById(id);
            return new Result(removed ? "删除成功" : "删除失败", null, removed ? 200 : 500);
        } catch (Exception e) {
            return new Result("删除失败：" + e.getMessage(), null, 500);
        }
    }

    @GetMapping("/getMedical/{cardId}")
    public Result getMedical(@PathVariable("cardId") String cardId) {
        try {
            List<MedicalCard> list = medicalCardService.list(
                    new QueryWrapper<MedicalCard>()
                            .eq("cardId", cardId)
            );
            return new Result("查询成功", list, 200);
        } catch (Exception e) {
            return new Result("查询失败：" + e.getMessage(), null, 500);
        }
    }
}
