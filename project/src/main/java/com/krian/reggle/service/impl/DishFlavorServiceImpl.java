package com.krian.reggle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krian.reggle.entity.DishFlavor;
import com.krian.reggle.mapper.DishFlavorMapper;
import com.krian.reggle.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
