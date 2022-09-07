package com.krian.reggle.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.krian.reggle.common.R;
import com.krian.reggle.entity.Category;
import com.krian.reggle.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// 分类管理
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * @param category
     * @return R
     * @Func 新增分类
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("新建分类：{}", category);
        categoryService.save(category);
        return R.success("新建分类成功！");
    }


    /**
     * @param page
     * @param pageSize
     * @return R
     * @Func 分类数据显示
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        log.info("page = {}, pageSize = {}", page, pageSize);

        // 构建分页构造器：
        Page<Category> pageInfo = new Page<>(page, pageSize);

        // 构建条件构造器：
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        // 添加排序条件：
        queryWrapper.orderByAsc(Category::getSort);

        // 执行查询：
        categoryService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * @param ids
     * @return R
     * @Func 删除分类信息
     */
    @DeleteMapping
    public R<String> delete(Long ids) {
        log.info("删除分类，分类ID：{}", ids);

        // categoryService.removeById(id);
        categoryService.remove(ids);
        return R.success("分类信息删除成功！");
    }

    /**
     * @param category
     * @return R
     * @Func 修改分类信息
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("修改分类信息数据：{}", category);

        categoryService.updateById(category);
        return R.success("修改分类信息成功！");
    }

    /**
     * @param category
     * @return R
     * @Func 根据条件获取分类列表
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        // 构造添加查询：
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件：
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        // 添加排序条件：
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        // 执行查询：
        List<Category> categoryList = categoryService.list(queryWrapper);

        return R.success(categoryList);
    }

}
