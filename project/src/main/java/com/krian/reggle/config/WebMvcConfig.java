package com.krian.reggle.config;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Slf4j
@Configuration // 声明当前类为一个配置类
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * @Func 设置静态资源映射
     * @param registry
     */
    @Override
    protected void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        log.info("开始静态资源映射......");

        // 添加一个资源处理器，处理浏览器提交的请求（addResourceLocations：映射本地目录）
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }
}
