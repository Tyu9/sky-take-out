package com.sky.exception;

/**
 * @Author：tantantan
 * @Package：com.sky.exception
 * @Project：sky-take-out
 * @name：BusinessException
 * @Date：2024/5/18 16:11
 * @Filename：BusinessException
 */
public class BusinessException extends BaseException{
    public BusinessException(){}
    public BusinessException(String msg){
        super(msg);
    }

}
