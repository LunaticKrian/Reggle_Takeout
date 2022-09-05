package com.krian.reggle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krian.reggle.entity.Employee;
import com.krian.reggle.mapper.EmployeeMapper;
import com.krian.reggle.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
