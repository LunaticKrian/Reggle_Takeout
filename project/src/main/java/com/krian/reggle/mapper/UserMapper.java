package com.krian.reggle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.krian.reggle.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
