package com.gxwz.syschronicdisease.common;

import lombok.Data;

/**
 * 统一返回结果类
 */
@Data
public class Result<T> {
    /**
     * 状态码
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 私有构造方法
     */
    private Result() {
    }

    /**
     * 成功返回结果
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功返回结果
     *
     * @param data 返回数据
     */
    public static <T> Result<T> success(T data) {
        return success(data, "操作成功");
    }

    /**
     * 成功返回结果
     *
     * @param data    返回数据
     * @param message 返回消息
     */
    public static <T> Result<T> success(T data, String message) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setData(data);
        result.setMessage(message);
        return result;
    }

    /**
     * 成功返回结果
     *
     * @param data  返回数据
     * @param total 总记录数
     */
    public static <T> Result<T> success(T data, Long total) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setData(data);
        result.setTotal(total);
        result.setMessage("操作成功");
        return result;
    }

    /**
     * 失败返回结果
     */
    public static <T> Result<T> error() {
        return error("操作失败");
    }

    /**
     * 失败返回结果
     *
     * @param message 错误信息
     */
    public static <T> Result<T> error(String message) {
        return error(500, message);
    }

    /**
     * 失败返回结果
     *
     * @param code    错误码
     * @param message 错误信息
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> Result<T> validateFailed() {
        return error(400, "参数验证失败");
    }

    /**
     * 参数验证失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> Result<T> validateFailed(String message) {
        return error(400, message);
    }

    /**
     * 未登录返回结果
     */
    public static <T> Result<T> unauthorized() {
        return error(401, "未登录或token已过期");
    }

    /**
     * 未授权返回结果
     */
    public static <T> Result<T> forbidden() {
        return error(403, "没有相关权限");
    }
} 