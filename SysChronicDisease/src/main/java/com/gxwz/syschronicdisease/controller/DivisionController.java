package com.gxwz.syschronicdisease.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gxwz.syschronicdisease.entity.Division;
import com.gxwz.syschronicdisease.entity.Result;
import com.gxwz.syschronicdisease.service.DivisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/division")
public class DivisionController {

    @Autowired
    private DivisionService divisionService;

    @GetMapping("/getall")
    public Result getAll(@RequestParam String dname){

        QueryWrapper<Division> queryWrapper = new QueryWrapper<>();

        if (dname != null){
            queryWrapper.like("dname",dname);
        }
        return new Result("查询成功",divisionService.list(queryWrapper),200);

    }

    @GetMapping("/getone/{id}")
    public Result getOne(@PathVariable Long id) {
        QueryWrapper<Division> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        return new Result("查询成功",divisionService.getOne(queryWrapper),200);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Division division){
        Division serviceOne = divisionService.getOne(new QueryWrapper<Division>().eq("dname", division.getDname()));
        if (serviceOne != null) {
            return new Result("添加失败，该区域已存在",null,205);
        }
        return new Result("添加成功",divisionService.save(division),200);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Division division){

        return new Result("修改成功",divisionService.updateById(division),200);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        return new Result("删除成功", divisionService.removeById(id), 200);
    }

    // 树形结构
    @GetMapping("/tree")
    public Result tree(@RequestParam(required = false) String name) {
        List<Division> all = divisionService.list();
        // 可加名称过滤
        if (name != null && !name.isEmpty()) {
            all = all.stream().filter(d -> d.getDname().contains(name)).collect(Collectors.toList());
        }
        List<DivisionNode> tree = buildTree(0L, all);
        return new Result("获取成功", tree, 200);
    }

    // 获取某节点下的直接子节点
    @GetMapping("/children")
    public Result children(@RequestParam Long parent) {
        List<Division> children = divisionService.lambdaQuery().eq(Division::getParent, parent).list();
        return new Result("获取成功", children, 200);
    }

    // 递归构建树
    private List<DivisionNode> buildTree(Long parentId, List<Division> all) {
        List<DivisionNode> nodes = new ArrayList<>();
        for (Division d : all) {
            if (Objects.equals(d.getParent(), parentId)) {
                DivisionNode node = new DivisionNode(d);
                node.setChildren(buildTree(d.getId(), all));
                nodes.add(node);
            }
        }
        return nodes;
    }

    // DivisionNode 内部类
    public static class DivisionNode extends Division {
        private List<DivisionNode> children = new ArrayList<>();
        public DivisionNode(Division d) {
            this.setId(d.getId());
            this.setParent(d.getParent());
            this.setDname(d.getDname());
            this.setLevel(d.getLevel());
            this.setSort(d.getSort());
            this.setCreateTime(d.getCreateTime());
            this.setUpdateTime(d.getUpdateTime());
        }
        public List<DivisionNode> getChildren() { return children; }
        public void setChildren(List<DivisionNode> children) { this.children = children; }
    }
}
