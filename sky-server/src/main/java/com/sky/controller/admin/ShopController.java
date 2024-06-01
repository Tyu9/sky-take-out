package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ClusterOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

/**
 * @Author：tantantan
 * @Package：com.sky.controller.admin
 * @Project：sky-take-out
 * @name：ShopController
 * @Date：2024/5/31 14:58
 * @Filename：ShopController
 */
@RestController
@Slf4j
@Api(tags = "店铺相关接口")
@RequestMapping("/admin")
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置店铺状态接口
     * @param status
     * @return
     */
    @ApiOperation("设置店铺状态接口")
    @PutMapping("/shop/{status}")
    public Result setShopStatus(@PathVariable Integer status){
        //1、日志打印操作
        log.info("设置店铺状态：{}",status);
        //2、根据redisTemplate获取操作String对象，设置key和value
        redisTemplate.opsForValue().set("SHOP_STATUS",status);
        //3、返回result
        return Result.success();
    }
    @ApiOperation("管理端获取店铺状态接口")
    @GetMapping("/shop/status")
    public Result<Integer> getShopStatus(){
        //1、日志打印操作
        //2、通过redisTemplate获取暂存的SHOP_STATUS
        //3、返回Result
        log.info("管理端获取店铺状态");
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get("SHOP_STATUS");
        if(shopStatus == null){
            shopStatus = 0;
        }
        return Result.success(shopStatus);
    }
}
