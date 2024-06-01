package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：tantantan
 * @Package：com.sky.controller.user
 * @Project：sky-take-out
 * @name：ShopController
 * @Date：2024/5/31 15:25
 * @Filename：ShopController
 */
@Slf4j
@Api(tags ="用户获取店铺状态接口")
@RestController("userShopController")
@RequestMapping("/user")
public class ShopController {
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 用户获取店铺状态
     * @return
     */
    @ApiOperation("用户获取店铺状态接口")
    @GetMapping("/shop/status")
    public Result<Integer> getShopStatus() {
        /*
            1、日志打印
            2、获取redisTemplate对象
            3、获取店铺状态
            4、返回结果
         */
        log.info("用户获取店铺状态");
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get("SHOP_STATUS");
        if(shopStatus == null){
            shopStatus = 0;
        }
        return Result.success(shopStatus);
    }
}
