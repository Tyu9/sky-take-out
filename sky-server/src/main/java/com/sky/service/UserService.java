package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @Author：tantantan
 * @Package：com.sky.service
 * @Project：sky-take-out
 * @name：UserService
 * @Date：2024/6/1 9:17
 * @Filename：UserService
 */
public interface UserService {
    User login(UserLoginDTO userLoginDTO);
}
