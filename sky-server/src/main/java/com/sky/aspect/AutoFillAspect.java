package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @Author：tantantan
 * @Package：com.sky.aspect
 * @Project：sky-take-out
 * @name：AutoFillAspect
 * @Date：2024/5/24 15:18
 * @Filename：AutoFillAspect
 */
@Component
@Aspect
@Slf4j
public class AutoFillAspect {
    /**
     * 该方法大致思路：
     * 我们要对
     */
    @Pointcut("execution (* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void path(){}
    @Before("path()")
    public void Before(JoinPoint joinPoint) throws Exception {
        //0、日志打印操作
        log.info("公共字段自动注入");
        //1、获取原始签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //2、获取原始签名上的注解
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);
        //3、获取注解类型
        OperationType operationType = annotation.value();
        //4、获取对象和Class类
        Object[] args = joinPoint.getArgs();
            //做安全判断
        if(args == null || args.length == 0){
            return;
        }
        Class<?> clazz = args[0].getClass();
        //5、通过反射获取方法
            //5.1、如果是Update操作则只需要填充updateTime和updateUser
        Method updateTime = clazz.getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
        Method updateUser = clazz.getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
        updateTime.invoke(args[0],LocalDateTime.now());
        updateUser.invoke(args[0],BaseContext.getCurrentId());
            //5.2、如果是Insert操作则还需要填充createTime和createUser
        if(operationType == OperationType.INSERT){
            Method createUser = clazz.getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            Method createTime = clazz.getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
            createTime.invoke(args[0],LocalDateTime.now());
            createUser.invoke(args[0], BaseContext.getCurrentId());
        }
    }
}
