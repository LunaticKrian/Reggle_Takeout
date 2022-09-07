package com.krian.reggle.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

// TODO:项目重新启动，由于线程处理快慢问题，可能导致出现自动数据填充出现异常！！！

// 自定义元数据对象处理器：
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * @param metaObject
     * @Func 插入操作时，自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充：【insert】......");
        log.info(metaObject.toString());

        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());

        // 同一个线程
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }

    /**
     * @Func 更新操作时，自动填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充：【update】......");
        log.info(metaObject.toString());

        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}
