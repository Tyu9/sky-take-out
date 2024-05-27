package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * @Author：tantantan
 * @Package：com.sky.service
 * @Project：sky-take-out
 * @name：DishService
 * @Date：2024/5/24 20:26
 * @Filename：DishService
 */
public interface DishService {
    /**
     * 添加菜品
     * @param dishDTO
     */
    void add(DishDTO dishDTO);

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品
     * @param ids
     */
    void deleteByIds(List<Integer> ids);

    /**
     * 根据ID查询菜品信息
     * @param id
     * @return
     */
    DishVO findById(Long id);

    /**
     * 修改菜品操作
     * @param  dishDTO
     */
        void updateDish(DishDTO dishDTO);
}
