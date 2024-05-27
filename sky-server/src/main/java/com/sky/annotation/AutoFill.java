package com.sky.annotation;
import com.sky.enumeration.OperationType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author：tantantan
 * @Package：com.sky.annotation
 * @Project：sky-take-out
 * @name：AutoFill
 * @Date：2024/5/24 15:09
 * @Filename：AutoFill
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    //1、设置一个value用来区分是什么操作
    OperationType value();
}
