package com.gxwz.syschronicdisease.entity;

public class Result {

    public String message;
    public Object data;
    public int code;


    public Result() {

    }

    public Result(String message, Object data, int code) {
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "message='" + message + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
    
    // 添加一些静态方法来简化返回结果
    public static Result success() {
        return success(null);
    }
    
    public static Result success(Object data) {
        return new Result("操作成功", data, 200);
    }
    
    public static Result error(String message) {
        return new Result(message, null, 500);
    }
    
    public static Result error(int code, String message) {
        return new Result(message, null, code);
    }
} 