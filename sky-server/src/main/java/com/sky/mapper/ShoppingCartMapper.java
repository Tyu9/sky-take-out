package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author：tantantan
 * @Package：com.sky.mapper
 * @Project：sky-take-out
 * @name：ShoppingCartMapper
 * @Date：2024/6/3 19:32
 * @Filename：ShoppingCartMapper
 */
@Mapper
public interface ShoppingCartMapper {
    //添加商品到购物车
    void add(@Param("shoppingCart") ShoppingCart shoppingCart);
    //查询购物车中数据
    ShoppingCart querry(@Param("userId") Long userId,@Param("shoppingCartDTO") ShoppingCartDTO shoppingCartDTO);
    //更新购物车中商品数量
    @Update("update shopping_cart set number = #{shoppingCart.number} where dish_flavor =#{shoppingCart.dishFlavor} and dish_id = #{shoppingCart.dishId} and user_id = #{shoppingCart.userId}")
    void updateNumber(@Param("shoppingCart") ShoppingCart shoppingCart);
    //通过用户ID查询购物车中对应数据
    @Select("select * from shopping_cart where user_id = #{userId}")
    List<ShoppingCart> findByUserId(Long userId);
    //删除购物车中商品
    void deleteOne(@Param("userId") Long userId,@Param("shoppingCart") ShoppingCart shoppingCart);
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void cleanShoppingCartByUserId(Long userId);
}
