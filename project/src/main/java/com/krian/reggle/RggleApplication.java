package com.krian.reggle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j // 日志门面（lombok提供）
@SpringBootApplication
public class RggleApplication {
    public static void main(String[] args) {
        SpringApplication.run(RggleApplication.class, args);

        // 控制台日志输出：
        log.info("项目启动成功!!!");
    }
}
