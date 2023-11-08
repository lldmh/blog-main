package com.mh.aspect;

import com.alibaba.fastjson.JSON;
import com.mh.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author dmh
 * @Date 2023/7/22 16:08
 * @Version 1.0
 * @introduce    日志切面
 */
@Component//注入
@Aspect  //切面类注解
@Slf4j
public class LogAspect {
    //确定切点
    @Pointcut("@annotation(com.mh.annotation.SystemLog)")   //注解的全类名（当使用这个注解的时候，就运行这个方法）
    public void pt(){

    }

    //通知方法（用环绕通知，最强大）
    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object ret;
        try{
            //前
            handleBefore(joinPoint);
            ret = joinPoint.proceed();      //目标方法执行返回的返回值
            //后
            handleAfter(ret);
        }finally {
            //结束后执行  log来自@Slf4j
            log.info("=======End=======" + System.lineSeparator());  //当前系统的换行符
        }
        return ret;
    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {

        //当前对象(Holder:一般使用seruidelocal进行数据共享，保证多个线程中的隔离)
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        //获取被增强方法的注解对象(getSystemLog是在joinPoint中获取的)
        SystemLog systemLog = getSystemLog(joinPoint);

        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}", request.getRequestURL());
        // 打印描述信息
        log.info("BusinessName   : {}", systemLog.businessName());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(),
                ((MethodSignature) joinPoint.getSignature()).getName());
        // 打印请求的 IP
        log.info("IP             : {}",request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}", JSON.toJSON(joinPoint.getArgs()));
    }
    private void handleAfter(Object ret) {
        // 打印出参
        log.info("Response       : {}", JSON.toJSON(ret));
    }

    //获取被增强方法的注解对象(getSystemLog是在joinPoint中获取的)
    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        //获得标签名joinPoint.getSignature();是封装的controller类中那个接口的所有代码
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //通过这个签名获取注解上的信息
        SystemLog systemLog = methodSignature.getMethod().getAnnotation(SystemLog.class);
        //返回这个对象
        return systemLog;
    }


}
