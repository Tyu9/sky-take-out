package com.sky.controller.user;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @Author：tantantan
 * @Package：com.sky.controller.user
 * @Project：sky-take-out
 * @name：UserController
 * @Date：2024/6/1 9:16
 * @Filename：UserController
 */
@Api(tags = "用户接口文档")
@Slf4j
@RestController
@RequestMapping("/user/user")
public class UserController {
    @Resource
    UserService userService;
    @Resource
    JwtProperties jwtProperties;

    /**
     * 微信用户登录
     * @param userLoginDTO
     * @return
     */
    @ApiOperation("微信用户登录")
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        /*
            日志记录操作
            调用service层login方法查询数据库和微信服务器
            下放令牌
            封装VO
            返回result
         */
        log.info("微信用户登录:{}",userLoginDTO);
        //调用service层login方法
        User user = userService.login(userLoginDTO);
        //创建负载内容
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        //创建令牌
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        //封装VO
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
        return Result.success(userLoginVO);
    }
}
