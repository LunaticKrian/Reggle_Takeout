package com.krian.reggle.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.krian.reggle.common.R;
import com.krian.reggle.entity.User;
import com.krian.reggle.service.UserService;
import com.krian.reggle.utils.SMSUtils;
import com.krian.reggle.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * @param user
     * @param session
     * @return
     * @Func 获取登陆验证码
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        // 获取手机号：
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            // 生成随机的4位验证码：
            String validateCode = String.valueOf(ValidateCodeUtils.generateValidateCode(4));

            log.info("生成的随机验证码：{}", validateCode);

            // 调用云平台提供的短信服务API完成短信发送：
            // SMSUtils.sendMessage("验证码", phone, validateCode);

            // 需要将生成的验证码保存到Session：
            session.setAttribute(phone, validateCode);

            return R.success("手机验证码短信发送成功！");
        }
        return R.error("短信发送失败！");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info("获取用户登录信息：{}", map.toString());

        // 获取登录手机号：
        String phone = String.valueOf(map.get("phone"));

        // 获取验证码：
        String code = String.valueOf(map.get("code"));
        log.info("前端提交登陆验证码：{}", code);

        // 从Session中获取保存的验证码：
        String codeInSession = (String)session.getAttribute(phone);
        log.info("Session中获取的验证码：{}", codeInSession);

        // 进行验证码的比对（页面提交的验证码和Session中的验证码进行对比）
        if (codeInSession != null && codeInSession.equals(code)){
            // 如果能够对比成功，登陆成功
            // 判断当前手机号是否为新用户，如果是新用户就自动完成注册：
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null){  // 判断当前手机号用户为新用户
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("登陆失败！");
    }
}
