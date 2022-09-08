package com.krian.reggle.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.krian.reggle.common.R;
import com.krian.reggle.dto.DishDto;
import com.krian.reggle.entity.Category;
import com.krian.reggle.entity.Dish;
import com.krian.reggle.service.CategoryService;
import com.krian.reggle.service.DishFlavorService;
import com.krian.reggle.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * @param dishDto
     * @return
     * @Func 新增菜品
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());

        dishService.SaveWithFlavor(dishDto);
        return R.success("新增菜品成功！");
    }

    /**
     * @param page
     * @param pageSize
     * @return R
     * @Func 菜品分页查询
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        // 构建分页器对象：
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        // 构建条件构造器：
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        // 添加过滤条件：
        queryWrapper.like(name != null, Dish::getName, name);

        // 添加排序条件：
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        // 执行查询：
        dishService.page(pageInfo, queryWrapper);

        // 对象拷贝：
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            // 对象拷贝：
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();  // 分类ID
            // 通过分类ID查询分类信息：
            Category category = categoryService.getById(categoryId);  // 分类对象

            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * @param id
     * @return R
     * @Func 根据id查询菜品信息和对应的口味信息
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * @param dishDto
     * @return
     * @Func 修改菜品信息
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功！");
    }

    /**
     * @param dish
     * @return R
     * @Func 根据条件查询对应的菜品数据
     */
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish) {
        // 构造查询条件：
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);  // 添加查询状态为1（起售状态）的菜品
        // 添加排序条件：
        queryWrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getUpdateTime);
        // 执行查询：
        List<Dish> list = dishService.list(queryWrapper);

        return R.success(list);
    }

    // TODO:菜品的删除

    // TODO:菜品的停售

    // TODO:批量处理问题
}
