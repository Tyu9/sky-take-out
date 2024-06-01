package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

/**
 * @Author：tantantan
 * @Package：com.sky.service
 * @Project：sky-take-out
 * @name：setmeal
 * @Date：2024/5/27 16:54
 * @Filename：setmeal
 */
public interface SetmealService {
    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐
     * @param setmealDTO
     */
    void add(SetmealDTO setmealDTO);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    SetmealVO findById(Long id);

    /**
     * 修改套餐内容
     * @param setmealDTO
     */
    void updateSetmeal(SetmealDTO setmealDTO);

    /**
     * 修改套餐在售/停售状态
     * @param status
     * @param id
     */
    void modifyStatus(Integer status, Long id);
    /**
     * 批量删除套餐
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    List<Setmeal> list(Setmeal setmeal);

    List<DishItemVO> getDishItemById(Long id);
}
