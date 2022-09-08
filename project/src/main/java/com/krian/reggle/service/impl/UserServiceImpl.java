package com.krian.reggle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krian.reggle.entity.User;
import com.krian.reggle.mapper.UserMapper;
import com.krian.reggle.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
