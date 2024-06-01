package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.BaseException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * @Author：tantantan
 * @Package：com.sky.service.impl
 * @Project：sky-take-out
 * @name：UserService
 * @Date：2024/6/1 9:17
 * @Filename：UserService
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    WeChatProperties weChatProperties;
    @Resource
    UserMapper userMapper;
    @Override
    public User login(UserLoginDTO userLoginDTO) {
        /*
            调用httpClient发送get方式请求到微信服务器
            获取微信服务器返回的openid
            去数据库根据openid查询是否存在对应的user
            如果存在，说明是老用户直接返回查询到的user
            如果不存在，说明是新用户创建新的user补充信息插入到数据库
            返回user
         */
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        HashMap<String, String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",userLoginDTO.getCode());
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(url, map);
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        if(openid == null){
            throw new BaseException("未获取到openid，登录失败");
        }
        User user = userMapper.getByOpenid(openid);
        if(user != null) return user;
        user = new User();
        user.setOpenid(openid);
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);
         return user;
    }
}
