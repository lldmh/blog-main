package com.mh.handler.exception;

import com.mh.domain.ResponseResult;
import com.mh.enums.AppHttpCodeEnum;
import com.mh.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author dmh
 * @Date 2023/7/19 21:14
 * @Version 1.0
 * @introduce
 */
@RestControllerAdvice //=ControllerAdvice和ResponseBody的合体
@Slf4j
public class GlobalExceptionHandler {
    //系统异常
    @ExceptionHandler(SystemException.class)  //自定义的异常类SystemException直接捕获这个类的异常了
    public ResponseResult systemExceptionHandler(SystemException e){
        //打印异常信息
        log.error("出现了异常！ {}", e);
        //从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(e.getCode(), e.getMsg());
    }

    //普通的异常直接在输出
    @ExceptionHandler(Exception.class)    //普通的异常类
    public ResponseResult exceptionHandler(Exception e){
        //打印异常信息
        log.error("出现了异常！ {}",e);
        //从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());
    }
}
