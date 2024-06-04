package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author：tantantan
 * @Package：com.sky.service
 * @Project：sky-take-out
 * @name：ShpooingCartService
 * @Date：2024/6/3 19:31
 * @Filename：ShpooingCartService
 */

public interface ShoppingCartService {
    /**
     * 添加购物车商品
     * @param shoppingCartDTO
     */
    void add(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车中添加商品
     * @return
     */
    List<ShoppingCart> list();

    /**
     * 删除购物车中商品
     * @param shoppingCartDTO
     */
    void deleteOne(ShoppingCartDTO shoppingCartDTO);

    /**
     * 清空购物车
     */
    void clean();
}
