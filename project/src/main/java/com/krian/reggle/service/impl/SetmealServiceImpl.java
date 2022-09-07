package com.krian.reggle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krian.reggle.entity.Setmeal;
import com.krian.reggle.mapper.SetmealMapper;
import com.krian.reggle.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
