package com.gxwz.syschronicdisease.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gxwz.syschronicdisease.entity.Policy;
import com.gxwz.syschronicdisease.entity.Result;
import com.gxwz.syschronicdisease.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/policy")
public class PolicyController {

    @Autowired
    private PolicyService policyService;

    @GetMapping("/list")
    public Result list() {
        try {
            LambdaQueryWrapper<Policy> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByDesc(Policy::getYear);
            List<Policy> list = policyService.list(wrapper);
            return new Result("获取成功", list, 200);
        } catch (Exception e) {
            return new Result("获取政策列表失败：" + e.getMessage(), null, 500);
        }
    }


    /**
     * 新增政策
     */
    @PostMapping("/add")
    public Result add(@RequestBody Policy policy) {
        try {
            // 检查年度是否已存在
            LambdaQueryWrapper<Policy> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Policy::getYear, policy.getYear());
            if (policyService.count(wrapper) > 0) {
                return new Result("该年度政策已存在", null, 400);
            }

            // 设置创建时间和更新时间
            Date now = new Date(System.currentTimeMillis());
            policy.setCreateTime(now);
            policy.setUpdateTime(now);

            // 保存政策
            if (policyService.save(policy)) {
                return new Result("添加成功", null, 200);
            } else {
                return new Result("添加失败", null, 500);
            }
        } catch (Exception e) {
            return new Result("添加政策失败：" + e.getMessage(), null, 500);
        }
    }

    /**
     * 更新政策
     */
    @PutMapping("/update")
    public Result update(@RequestBody Policy policy) {
        try {
            // 检查政策是否存在
            Policy existingPolicy = policyService.getById(policy.getId());
            if (existingPolicy == null) {
                return new Result("政策不存在", null, 404);
            }

            // 如果修改了年度，检查新年度是否已存在
            if (!existingPolicy.getYear().equals(policy.getYear())) {
                LambdaQueryWrapper<Policy> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Policy::getYear, policy.getYear());
                if (policyService.count(wrapper) > 0) {
                    return new Result("该年度政策已存在", null, 400);
                }
            }

            // 设置更新时间
            policy.setUpdateTime(new Date(System.currentTimeMillis()));
            // 保持创建时间不变
            policy.setCreateTime(existingPolicy.getCreateTime());

            // 更新政策
            if (policyService.updateById(policy)) {
                return new Result("更新成功", null, 200);
            } else {
                return new Result("更新失败", null, 500);
            }
        } catch (Exception e) {
            return new Result("更新政策失败：" + e.getMessage(), null, 500);
        }
    }

    /**
     * 删除政策
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id) {
        try {
            // 检查政策是否存在
            if (!policyService.getById(id).equals(null)) {
                return new Result("政策不存在", null, 404);
            }

            // 删除政策
            if (policyService.removeById(id)) {
                return new Result("删除成功", null, 200);
            } else {
                return new Result("删除失败", null, 500);
            }
        } catch (Exception e) {
            return new Result("删除政策失败：" + e.getMessage(), null, 500);
        }
    }

    /**
     * 根据年度查询政策
     */
    @GetMapping("/getyear/{year}")
    public Result getByYear(@PathVariable("year") String year) {
        try {
            LambdaQueryWrapper<Policy> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Policy::getYear, year);
            Policy policy = policyService.getOne(wrapper);
            if (policy != null) {
                return new Result("获取成功", policy, 200);
            } else {
                return new Result("未找到该年度的政策", null, 404);
            }
        } catch (Exception e) {
            return new Result("查询政策失败：" + e.getMessage(), null, 500);
        }
    }
}
