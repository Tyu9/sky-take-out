package com.sky.service.impl;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author：tantantan
 * @Package：com.sky.service.impl
 * @Project：sky-take-out
 * @name：ShoppingCartServiceImpl
 * @Date：2024/6/3 19:32
 * @Filename：ShoppingCartServiceImpl
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Resource
    private ShoppingCartMapper shoppingCartMapper;
    @Resource
    private DishMapper dishMapper;
    @Resource
    private SetmealMapper setmealMapper;
    /**
     * 添加购物车商品
     * @param shoppingCartDTO
     */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        /*
            1、区分添加的商品是菜品还是套餐
            2、如果是菜品，则根据菜品ID、用户ID查询是否存在数据库
                如果存在，则说明不是第一次添加，数量+1 并更新数据库
                如果不存在则说明是第一次添加，直接将数据插入
            3、如果是套餐，则根据套餐ID和用户ID查询数据库是否已经存在
                如果不存在，说明是第一次添加则直接插入数据
                如果存在，则说明不是第一次添加，数量+1 并更新数据库
         */
        Long userId = BaseContext.getCurrentId();
        //根据菜品/套餐ID和用户ID查询数据库是否已存在，来区分是否是第一次添加
        ShoppingCart shoppingCart = shoppingCartMapper.querry(userId,shoppingCartDTO);
        //如果是添加的菜品
        if(shoppingCartDTO.getDishId() != null){

            //第一次添加则直接插入数据
            if(shoppingCart == null){
                shoppingCart = new ShoppingCart();
                BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
                //补全菜品数据
                    //根据菜品ID去菜品表查询相关数据
                Dish dish = dishMapper.findById(shoppingCartDTO.getDishId());
                shoppingCart.setUserId(userId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setNumber(1);
                shoppingCart.setCreateTime(LocalDateTime.now());
                shoppingCartMapper.add(shoppingCart);
            }else{
                //不是第一次添加则数量+1，修改数据
                shoppingCart.setNumber(shoppingCart.getNumber()+1);
                shoppingCartMapper.updateNumber(shoppingCart);
            }
        }
        //如果是添加的套餐
        else{
            //是第一次添加则直接插入数据
            if(shoppingCart == null){
                shoppingCart = new ShoppingCart();
                BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
                //补全套餐数据，去套餐表查询对应套餐数据
                Setmeal setmeal = setmealMapper.findById(shoppingCart.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setUserId(userId);
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setNumber(1);
                shoppingCart.setCreateTime(LocalDateTime.now());
                shoppingCartMapper.add(shoppingCart);
            }else{
                //不是第一次添加则商品数量+1
                shoppingCart.setNumber(shoppingCart.getNumber()+1);
                shoppingCartMapper.updateNumber(shoppingCart);
            }
        }
    }

    /**
     * 查看购物车中添加商品
     *1、通过BaseContext获取当前用户的id
     * 2、通过用户id去购物车表中查询他的数据
     * @return
     */
    @Override
    public List<ShoppingCart> list() {
        Long userId = BaseContext.getCurrentId();
        List<ShoppingCart> list = shoppingCartMapper.findByUserId(userId);
        return list;
    }

    /**
     * 删除购物车中商品
     *
     * @param shoppingCartDTO
     */
    @Override
    public void deleteOne(ShoppingCartDTO shoppingCartDTO) {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart querry = shoppingCartMapper.querry(userId, shoppingCartDTO);
        if(querry.getNumber()>1){
            //数量大于1则减少数量
            querry.setNumber(querry.getNumber()-1);
            shoppingCartMapper.updateNumber(querry);
        }else{
            //数量为1则直接删除
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
            shoppingCartMapper.deleteOne(userId,shoppingCart);
        }
    }

    /**
     * 清空购物车
     */
    @Override
    public void clean() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.cleanShoppingCartByUserId(userId);
    }
}
