package com.krian.reggle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.krian.reggle.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
