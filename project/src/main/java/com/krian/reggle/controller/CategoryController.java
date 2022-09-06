package com.krian.reggle.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.krian.reggle.common.R;
import com.krian.reggle.entity.Category;
import com.krian.reggle.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.UDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
     * @return
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
     * @Func 删除分类信息
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long id){
        log.info("删除分类，分类ID：{}", id);

        categoryService.removeById(id);
        return R.success("分类信息删除成功！");
    }
}
