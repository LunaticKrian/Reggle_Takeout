package com.krian.reggle.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.krian.reggle.common.R;
import com.krian.reggle.entity.Employee;
import com.krian.reggle.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * @param request
     * @param employee
     * @return R
     * @Func 员工登陆方法
     */
    @PostMapping("/login")  // 使用POST方法提交过来的参数，需要使用 @RequestBody 注解进行封装获取
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {  // 注意属性封装时，属性名的对应

        // 1.加密前端提交的密码：
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));  // 使用DigestUtils工具类进行md5加密

        // 2.根据页面提交的用户名username查询数据库：
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();  // 获取数据库查询包装类对象
        queryWrapper.eq(Employee::getUsername, employee.getUsername());  // 设置查询条件
        Employee emp = employeeService.getOne(queryWrapper);

        // 3.如果没有查询到则返回登陆失败：
        if (emp == null) {
            return R.error("当前用户未被注册，请注册后再次尝试！");
        }

        // 4.密码比对，如果不一致则返回登陆失败：
        if (!emp.getPassword().equals(password)) {
            return R.error("亲检查用户名和密码是否正确！");
        }

        // 5.查看员工状态，如果已经被禁用，则返回员工已经被禁用结果：
        if (emp.getStatus() == 0) {
            return R.error("当前员工账号被禁用，登陆失败！");
        }

        // 6.登陆成功，将员工的ID存入Session中，并返回登陆成功的结果：
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);  // 登陆成功，返回employee对象
    }


    /**
     * @Func 员工退出登陆
     * @param request
     * @return R
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        // 清理Session中的当前员工的id数据：
        request.getSession().removeAttribute("employee");
        return R.success("退出登录成功！");
    }
}
