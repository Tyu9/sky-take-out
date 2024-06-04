package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author：tantantan
 * @Package：com.sky.controller.user
 * @Project：sky-take-out
 * @name：ShoppingCartController
 * @Date：2024/6/3 19:28
 * @Filename：ShoppingCartController
 */
@Api(tags="购物车接口")
@RestController("userShoppingCartController")
@Slf4j
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {
    @Resource
    private ShoppingCartService shoppingCartService;
    /**
     * 购物车添加商品接口
     * @param shoppingCartDTO
     * @return
     */
    @ApiOperation("购物车添加商品接口")
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        /*
            1、日志打印操作
            2、调用service层方法
            3、返回result
         */
        log.info("购物车添加商品操作");
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查询购物车中添加商品
     * @return
     */
    @ApiOperation("/查询购物车中商品接口")
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
        log.info("查看购物车中商品");
        List<ShoppingCart> shoppingCart =  shoppingCartService.list();
        return Result.success(shoppingCart);
    }

    /**
     * 删除购物车中商品接口
     * @param shoppingCartDTO
     * @return
     */
    @ApiOperation("购物车删除商品接口")
    @PostMapping("/sub")
    public Result deleteOne(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("删除购物车中商品:{}",shoppingCartDTO);
        shoppingCartService.deleteOne(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 清空购物车接口
     * @return
     */
    @ApiOperation("清空购物车")
    @DeleteMapping("/clean")
    public Result clean(){
        log.info("清空购物车");
        shoppingCartService.clean();
        return Result.success();
    }
}
