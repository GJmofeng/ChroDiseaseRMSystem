package com.gxwz.syschronicdisease.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gxwz.syschronicdisease.entity.Reimbursement;
import com.gxwz.syschronicdisease.service.ReimbursementService;
import com.gxwz.syschronicdisease.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reimbursement")
public class ReimbursementController {

    @Autowired
    private ReimbursementService reimbursementService;

    @GetMapping("/getAudit")
    public Result getAudit() {
        // 获取所有报销记录，按审核状态和日期排序
        QueryWrapper<Reimbursement> queryWrapper = new QueryWrapper<Reimbursement>()
                .orderByAsc("is_reimbursement")  // 未审核的排在前面
                .orderByDesc("date");            // 同状态按日期倒序
        return new Result("查询成功", reimbursementService.list(queryWrapper), 200);
    }
} 