package com.krian.reggle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krian.reggle.common.exception.CustomException;
import com.krian.reggle.dto.SetmealDto;
import com.krian.reggle.entity.Setmeal;
import com.krian.reggle.entity.SetmealDish;
import com.krian.reggle.mapper.SetmealMapper;
import com.krian.reggle.service.SetmealDishService;
import com.krian.reggle.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * @param setmealDto
     * @Func 新增套餐，同时需要保存套餐和菜品的关联关系
     */
    @Transactional  // 事务注解，保证数据的一致性
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存套餐的基本信息，操作Setmeal，执行insert操作：
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        // 给SetmealDish设置setmealId：
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        // 保存套餐和菜品的关联信息，操作setmeal_dish，执行inset操作：
        setmealDishService.saveBatch(setmealDishes);  // 批量保存
    }

    /**
     * @param ids
     * @Func 删除套餐，同时需要删除套餐和菜品的关联数据
     */
    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {
        // 判断ids对应的套餐状态是否可以被删除：
        LambdaQueryWrapper<Setmeal> queryWrapper1 = new LambdaQueryWrapper<>();
        // select count(*) from stemeal where id in (ids) and status = 1;
        queryWrapper1.in(Setmeal::getId, ids);
        queryWrapper1.eq(Setmeal::getStatus, 1);
        long count = this.count(queryWrapper1);

        // 如果不能删除，抛出业务异常：
        if (count > 0) throw new CustomException("套餐正在售卖中，无法删除！");

        // 如果可以被删除，先删除套餐表中的数据 -> setmeal：
        this.removeByIds(ids);

        // 再删除菜品和套餐关系表数据 -> setmeal_dish：
        // delete from setmeal_dish where setmeal_id in (ids)
        LambdaQueryWrapper<SetmealDish> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.in(SetmealDish::getSetmealId, ids);

        setmealDishService.remove(queryWrapper2);
    }
}
