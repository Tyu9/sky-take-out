package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result baseExceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }
//    /**
//     * 捕获其他异常
//     * @param ex
//     * @return
//     */
//    @ExceptionHandler
//    public Result exceptionHandler(Exception ex){
//        log.error("异常信息：{}", ex.getMessage());
//        return Result.error(MessageConstant.UNKNOWN_ERROR);
//    }
    /**
     * 捕获重复用户名异常
     */
    @ExceptionHandler
    public Result exceptionSQLExceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.info("异常信息：{}", ex.getMessage());
        ex.printStackTrace();
        // 获取异常信息并将其按照空格进行分割变为数组
        String[] split = ex.getMessage().split(" ");
        // 获取数组中的第3个元素
        if(split[2]!=null&&split[2]!=""){
            // 拼接字符串到返回结果中
            return Result.error(split[2]+MessageConstant.USERNAME_EXIST);
        }
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }


}
