package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author：tantantan
 * @Package：com.sky.mapper
 * @Project：sky-take-out
 * @name：SetmealDishMapper
 * @Date：2024/5/27 18:48
 * @Filename：SetmealDishMapper
 */
@Mapper
public interface SetmealDishMapper {
    void add(@Param("setmealDishes") List<SetmealDish> setmealDishes);
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> findBySetmealId(Long id);


    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void deleteBySetmealId(Long id);

    void deleteByIds(List<Long> ids);

    List<Long> getSetmealIdsByDishIds(@Param("ids") List<Long> dishIds);
}
