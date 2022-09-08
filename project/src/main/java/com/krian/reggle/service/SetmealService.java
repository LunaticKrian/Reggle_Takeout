package com.krian.reggle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.krian.reggle.dto.SetmealDto;
import com.krian.reggle.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    // 新增套餐，同时需要保存套餐和菜品的关联关系：
    void saveWithDish(SetmealDto setmealDto);

    // 删除套餐，同时需要删除套餐和菜品的关联数据：
    void removeWithDish(List<Long> ids);
}
