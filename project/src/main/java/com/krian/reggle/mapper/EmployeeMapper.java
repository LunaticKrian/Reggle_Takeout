package com.krian.reggle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.krian.reggle.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {  // 继承BaseMapper，由MyBatis_Plus提供

}
