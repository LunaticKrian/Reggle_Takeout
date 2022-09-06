package com.krian.reggle.config;

import com.krian.reggle.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration // 声明当前类为一个配置类
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * @param registry
     * @Func 设置静态资源映射
     */
    @Override
    protected void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        log.info("开始静态资源映射......");

        // 添加一个资源处理器，处理浏览器提交的请求（addResourceLocations：映射本地目录）
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }


    /**
     * @param converters
     * @Func 扩展MVC框架的消息转换器，项目启动时调用extendMessageConverters方法
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器......");

        // 创建消息转换器对象：
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        // 设置对象装换器，底层使用Jackson将Java对象转换为JSON格式的数据：
        messageConverter.setObjectMapper(new JacksonObjectMapper());

        // 将上面的消息转换器对象追加到MVC框架的转换器集合中：
        converters.add(0, messageConverter);
    }
}
