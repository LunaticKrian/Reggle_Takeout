package com.krian.reggle.common;

// 基于ThreadLocal封装的工具类，用于保存和获取当前登陆用户的ID
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new InheritableThreadLocal<>();

    /**
     * @param id
     * @Func 设置值到ThreadLocal中
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * @return Long
     * @Func 获取ThreadLocal中的数据
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
