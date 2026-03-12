package com.example.commonbase.result;

import lombok.Data;

@Data
public class Result<T> {

    private int code;
    private String message;
    private T data;
    private long timestamp;
    //时间戳：记录接口什么时候返回的，方便排查问题

    private Result() {
        this.timestamp = System.currentTimeMillis();
    }
    //自动设置当前时间戳

    public static <T>Result<T> success(T data){
        Result<T> r=new Result<>();
        r.code=200;
        r.message="success";
        r.data=data;
        return r;
    }
    public static <T>Result<T> success() {
        return success(null);
    }
    public static <T>Result<T> fail(int code,String message){
        Result<T> r=new Result<>();
        r.code=code;
        r.message=message;
        return r;
    }
    public static <T>Result<T> fail(String message){
       return fail(500,message);
    }
}
