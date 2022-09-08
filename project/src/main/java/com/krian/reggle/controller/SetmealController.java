package com.krian.reggle.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.krian.reggle.common.R;
import com.krian.reggle.dto.SetmealDto;
import com.krian.reggle.entity.Category;
import com.krian.reggle.entity.Setmeal;
import com.krian.reggle.service.CategoryService;
import com.krian.reggle.service.SetmealDishService;
import com.krian.reggle.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * @param setmealDto
     * @return
     * @Func 添加套餐
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("套餐信息：{}", setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功！");
    }

    /**
     * @param page
     * @param pageSize
     * @param name
     * @return
     * @Func 分页查询套餐信息
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件：
        queryWrapper.like(name != null, Setmeal::getName, name);
        // 添加排序条件：
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        // 执行查询：
        setmealService.page(pageInfo, queryWrapper);

        // 对象拷贝：
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            // 对象拷贝：
            BeanUtils.copyProperties(item, setmealDto);
            // 获取分类id：
            Long categoryId = item.getCategoryId();
            // 根据分类id查询分类对象：
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                // 设置分类名称：
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }

            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);

        // 返回dtoPage，封装完成的数据对象：
        return R.success(dtoPage);
    }

    /**
     * @param ids
     * @return R
     * @Func 删除套餐信息
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("删除套餐信息的id:{}", ids);
        setmealService.removeWithDish(ids);
        return R.success("删除套餐信息成功！");
    }

}
