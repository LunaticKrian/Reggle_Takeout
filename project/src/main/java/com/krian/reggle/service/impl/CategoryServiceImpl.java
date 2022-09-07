package com.krian.reggle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krian.reggle.common.exception.CustomException;
import com.krian.reggle.entity.Category;
import com.krian.reggle.entity.Dish;
import com.krian.reggle.entity.Setmeal;
import com.krian.reggle.mapper.CategoryMapper;
import com.krian.reggle.service.CategoryService;
import com.krian.reggle.service.DishService;
import com.krian.reggle.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * @param id
     * @Func 根据ID删除分类，删除之前需要进行判断
     */
    @Override
    public void remove(Long id) {
        // 查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常：
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 构建查询条件，根据分类ID进行查询：
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        long count_dish = dishService.count(dishLambdaQueryWrapper);

        log.info("count_dish：{}",count_dish);

        if (count_dish > 0) {
            // 已经关联菜品，抛出一个业务异常：
            throw new CustomException("当前分类项关联了菜品，无法正常删除！");
        }

        // 查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常：
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 构建查询条件，根据分类ID进行查询：
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        long count_setmeal = setmealService.count(setmealLambdaQueryWrapper);

        log.info("count_setmeal：{}",count_setmeal);

        if (count_setmeal > 0){
            // 已经关联套餐，抛出一个业务异常：
            throw new CustomException("当前分类关联了套餐，无法正常删除！");
        }

        // 正常删除分类：
        super.removeById(id);
    }
}
