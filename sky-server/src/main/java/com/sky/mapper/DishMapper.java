package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author：tantantan
 * @Package：com.sky.mapper
 * @Project：sky-take-out
 * @name：DishMapper
 * @Date：2024/5/22 19:36
 * @Filename：DishMapper
 */
@Mapper
public interface DishMapper {
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @AutoFill(OperationType.INSERT)
    @Insert("insert into dish values" +
            "(null,#{name},#{categoryId},#{price},#{image},#{description},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void add(Dish dish);


    List<DishVO> page(DishPageQueryDTO dishPageQueryDTO);

    Integer findStatusByIds(@Param("ids") List<Integer> ids, @Param("status") Integer status);

    Integer findSetmealDishByDishIds(@Param("ids") List<Integer> ids);

    void deleteDishs(@Param("ids") List<Integer> ids);

    @Select("select * from dish where id = #{id}")
    Dish findById(Long id);

    @AutoFill(OperationType.UPDATE)
    void updateDish(Dish dish);

    @Select("select * from dish where category_id = #{id}")
    List<Dish> listByCategoryId(Long id);

    @Select("select * from dish where category_id = #{id}")
    List<Dish> findByCategoryId(Long id);

    List<Dish> list(Dish dish);
}
