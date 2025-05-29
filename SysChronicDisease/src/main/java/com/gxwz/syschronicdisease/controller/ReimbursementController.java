package com.gxwz.syschronicdisease.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gxwz.syschronicdisease.entity.Policy;
import com.gxwz.syschronicdisease.entity.Reimbursement;
import com.gxwz.syschronicdisease.entity.Result;
import com.gxwz.syschronicdisease.service.PolicyService;
import com.gxwz.syschronicdisease.service.ReimbursementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reimbursement")
public class ReimbursementController {

    @Autowired
    private ReimbursementService reimbursementService;
    @Autowired
    private PolicyService policyService;

    @GetMapping("/getAll")
    public Result getAll() {

        return new Result("成功", reimbursementService.list(), 200);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Reimbursement reimbursement) {

        int year = reimbursement.getDate().getYear() + 1900;
        List<Policy> policies = policyService.list();
        //判断当年是有否政策规定
        for (Policy policy : policies) {
            //判断是否是当前年份
            if (policy.getYear().equals(year + "")) {
                //判断报销金额是否超出
                if (reimbursement.getTotalCost() > policy.getMax()) {
                    return new Result("添加失败", null, 200);
                } else {
                    reimbursement.setReimbursementAmount(reimbursement.getTotalCost() * policy.getPercentage());
                    reimbursement.setIsReimbursement("未审核");
                    reimbursement.setIsRemit("未汇款");
                    return new Result("添加成功", reimbursementService.save(reimbursement), 200);

                }


            }
        }
        return new Result("添加失败", null, 202);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Reimbursement reimbursement) {

        reimbursementService.updateById(reimbursement);
        return new Result("修改成功", null, 200);
    }

    @GetMapping("/getAudit")
    public Result getAudit() {
        QueryWrapper<Reimbursement> queryWrapper = new QueryWrapper<Reimbursement>().eq("is_reimbursement", "未审核");
        return new Result("查询成功", reimbursementService.list(queryWrapper), 200);

    }
    @GetMapping("/getRemit")
    public Result getRemit() {
        QueryWrapper<Reimbursement> queryWrapper = new QueryWrapper<Reimbursement>()
                .eq("is_reimbursement", "已审核")
                .eq("is_remit", "未汇款");
        return new Result("查询成功", reimbursementService.list(queryWrapper), 200);
    }
    @GetMapping("/getStatics")
    public Result getStatics() {
        QueryWrapper<Reimbursement> reimbursementQueryWrapper = new QueryWrapper<Reimbursement>().eq("is_reimbursement", "已审核");
        List<Reimbursement> reimbursements = reimbursementService.list(reimbursementQueryWrapper);
        Map<String, Integer> map = new HashMap();
        map.put("已审核", reimbursements.size());
        map.put("未审核", reimbursementService.list(new QueryWrapper<Reimbursement>().eq("is_reimbursement", "未审核")).size());
        return new Result("查询成功", map, 200);
    }

}
