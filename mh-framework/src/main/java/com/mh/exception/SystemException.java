package com.mh.exception;

import com.mh.enums.AppHttpCodeEnum;

/**
 * @Author dmh
 * @Date 2023/7/19 21:12
 * @Version 1.0
 * @introduce
 */
public class SystemException extends RuntimeException{

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
    //获得一个枚举（这里赋值完会自动的被GlobalExceptionHandler中的systemExceptionHandler捕获到）
    public SystemException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }

}