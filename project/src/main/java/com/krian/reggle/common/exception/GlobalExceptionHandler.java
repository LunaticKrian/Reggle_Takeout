package com.krian.reggle.common.exception;

import com.krian.reggle.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

// 全局异常处理：
@Slf4j
@ControllerAdvice(annotations = {RestController.class, Controller.class})  // 处理添加了指定注解的Controller
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * @return R
     * @Func SQLIntegrityConstraintViolationException 异常处理
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception) {
        log.info(exception.getMessage());

        if (exception.getMessage().contains("Duplicate entry")) {
            String[] split = exception.getMessage().split(" ");
            String msg = split[2] + "已存在，添加信息失败！";
            return R.error(msg);
        }

        return R.error("未知错误！");
    }

    /**
     * @param exception
     * @return
     * @Func 处理自定义异常的全局异常处理器方法
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException exception) {
        log.info(exception.getMessage());

        // 把自定义的异常信息返回给前端：
        return R.error(exception.getMessage());
    }

}
