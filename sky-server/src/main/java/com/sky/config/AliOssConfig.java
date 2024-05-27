package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author：tantantan
 * @Package：com.sky.config
 * @Project：sky-take-out
 * @name：AliOssConfig
 * @Date：2024/5/24 19:30
 * @Filename：AliOssConfig
 */
@Configuration
public class AliOssConfig {
    @Autowired
    private AliOssProperties aliOssProperties;
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties){
        //新建一个阿里云工具类
        AliOssUtil aliOssUtil = new AliOssUtil(
                aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
        return aliOssUtil;
    }

}
