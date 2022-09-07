package com.krian.reggle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j  // 日志门面（lombok提供）
@SpringBootApplication  // SpringBoot启动类注解
@ServletComponentScan  // 配置包扫描，当前启动类所在的包以及子包
@EnableTransactionManagement  // 开始事务控制
public class RggleApplication {
    public static void main(String[] args) {
        SpringApplication.run(RggleApplication.class, args);

        // 控制台日志输出：
        log.info("项目启动成功!!!");
    }
}
