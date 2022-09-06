package com.krian.reggle.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.krian.reggle.common.R;
import com.krian.reggle.config.constant.Constant;
import com.krian.reggle.entity.Employee;
import com.krian.reggle.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

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
     * @param request
     * @return R
     * @Func 员工退出登陆
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        // 清理Session中的当前员工的id数据：
        request.getSession().removeAttribute("employee");
        return R.success("退出登录成功！");
    }

    /**
     * @param employee
     * @return
     * @Func 添加员工信息
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工信息：{}", employee.toString());

        // 设置初始密码，需要md5加密处理：
        employee.setPassword(DigestUtils.md5DigestAsHex(Constant.DEFAULT_PASSWORD_EMPLOYEE.getBytes(StandardCharsets.UTF_8)));

        // LocalDateTime.now()：获取当前系统时间：
        // employee.setCreateTime(LocalDateTime.now());
        // employee.setUpdateTime(LocalDateTime.now());

        // 从Session中获取当前登陆用户ID：
        // Long employeeId = (Long) request.getSession().getAttribute("employee");

        // employee.setCreateUser(employeeId);
        // employee.setUpdateUser(employeeId);

        // 调用Service方法，保存员工信息：
        employeeService.save(employee);

        return R.success("新增员工成功！");
    }

    /**
     * @param page
     * @param pageSize
     * @param name
     * @return
     * @Func 显示员工信息列表
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);

        // 构建分页构造器：
        Page pageInfo = new Page(page, pageSize);

        // 构建条件构造器：
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();

        // 添加过滤条件：
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, pageInfo);

        // 添加排序条件：
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        // 执行查询：
        employeeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * @param employee
     * @return
     * @Func 更新员状态工信息
     */
    @PutMapping()
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("调用更新，更新员工信息：{}", employee);

        employee.setUpdateUser((long) request.getSession().getAttribute("employee"));
        employee.setUpdateTime(LocalDateTime.now());

        employeeService.updateById(employee);

        return R.success("员工信息修改成功！");
    }


    /**
     * @param id
     * @return
     * @Func 根据ID查询员工信息
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询员工信息：{}", id);

        Employee employee = employeeService.getById(id);
        if (employee != null) return R.success(employee);

        return R.error("没有查询到对应员工信息！");
    }
}
