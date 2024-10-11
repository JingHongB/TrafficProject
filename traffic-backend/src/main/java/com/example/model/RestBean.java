package com.example.model;

import com.example.utils.Const;

/**
 * 该类用于封装 REST API的标准响应结构
 * 使用record定义一个不可变的数据类，该类包含三个属性：状态码、数据、信息
 *
 * @param code    返回的状态码
 * @param data    返回的数据
 * @param message 返回的信息
 * @param <T>     数据类型
 */
public record RestBean<T>(int code, T data, String message) {
    /**
     * 该方法用于创建一个成功的返回结果
     *
     * @param data 返回的数据
     * @param <T>  数据类型
     * @return 包含成功信息和数据的RestBean实例
     */
    public static <T> RestBean<T> success(T data) {
        return new RestBean<>(Const.SUCCESS_CODE, data, Const.SUCCESS_MSG);
    }

    /**
     * 该方法用于创建一个成功但无数据的返回结果
     *
     * @param <T> 数据类型
     * @return 包含成功信息的RestBean实例，数据为null
     */
    public static <T> RestBean<T> success() {
        return success(null);
    }

    /**
     * 该方法用于创建一个失败的返回结果
     *
     * @param code    返回的状态码
     * @param message 返回的错误信息
     * @param <T>     数据类型
     * @return 包含失败信息的RestBean实例，数据为null
     */
    public static <T> RestBean<T> failure(int code, String message) {
        return new RestBean<>(code, null, message);
    }
}
