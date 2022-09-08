package com.krian.reggle.dto;

import com.krian.reggle.entity.Setmeal;
import com.krian.reggle.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {  // 通过继承实现对数据传输对象的一个拓展

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
