package com.sky.config;

import com.sky.intercepter.AdminLoginTokenInterceptor;
import com.sky.intercepter.UserLoginTokenInterceptor;
import com.sky.json.JacksonObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author：tantantan
 * @Package：com.sky.config
 * @Project：sky-take-out
 * @name：WebConfig
 * @Date：2024/5/19 14:28
 * @Filename：WebConfig
 */
@Configuration
public class MyWebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 定义添加拦截器
     */
    @Autowired
    private AdminLoginTokenInterceptor adminLoginTokenInterceptor;
    @Resource
    private UserLoginTokenInterceptor userLoginTokenInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminLoginTokenInterceptor) //添加设置的拦截器
                .addPathPatterns("/admin/**") //设置拦截路径
                .excludePathPatterns("/admin/employee/login"); //设置放行路径
        registry.addInterceptor(userLoginTokenInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/user/login","/user/shop/status");
    }

    /**
     * 通过knife4生成接口文档
     * @return
     */
    @Bean
    public Docket adminDocket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("苍穹外卖项目接口文档")
                .version("2.0")
                .description("苍穹外卖项目接口文档")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .groupName("管理端接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller.admin"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
    @Bean
    public Docket userDocket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("苍穹外卖项目接口文档")
                .version("2.0")
                .description("苍穹外卖项目接口文档")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .groupName("用户端接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller.user"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //swagger会生成静态资源，这里配置swagger的静态资源生成位置
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 重写消息转化器
     */
    @Override
    protected  void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建消息转化器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将消息转化器对象添加到消息转化器集合中
        converters.add(0,messageConverter);
    }
}
