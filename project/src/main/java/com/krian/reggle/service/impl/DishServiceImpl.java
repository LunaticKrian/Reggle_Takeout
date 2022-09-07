package com.krian.reggle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krian.reggle.dto.DishDto;
import com.krian.reggle.entity.Dish;
import com.krian.reggle.entity.DishFlavor;
import com.krian.reggle.mapper.DishMapper;
import com.krian.reggle.service.DishFlavorService;
import com.krian.reggle.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品时，同时需要插入对应口味数据，需要操作两张表：dish，dish_flavor
     * 新增菜品，同时保存对应的口味数据
     *
     * @param dishDto
     */
    @Transactional  // 数据库的事务控制，启动类上需要开始对事务的支持
    public void SaveWithFlavor(DishDto dishDto) {
        // 保存菜品的基本信息到菜品表dish：
        this.save(dishDto);

        // 获取菜品ID：
        Long dishId = dishDto.getId();

        // 菜品口味：
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item) -> {  // 遍历flavors中的每一项
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);  // saveBatch批量保存
    }

    /**
     * @param id
     * @return
     * @Func 根据ID查询菜品信息和对应的口味信息
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        // 查询菜品基本信息：
        Dish dish = this.getById(id);

        // 查询菜品对应的口味信息：
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        // 更新dish表的基本信息：
        this.updateById(dishDto);

        // 清理当前菜品对应的口味数据 -> dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);  // 删除操作

        // 添加当前提交过来的口味数据 -> dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
}
