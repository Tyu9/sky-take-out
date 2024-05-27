package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author：tantantan
 * @Package：com.sky.mapper
 * @Project：sky-take-out
 * @name：DishFlavorMapper
 * @Date：2024/5/27 10:27
 * @Filename：DishFlavorMapper
 */
@Mapper
public interface DishFlavorMapper {
    void addFishFlavor(@Param("dishFlavors") List<DishFlavor> dishFlavors);

    void deleteDishFlavors(@Param("ids") List<Integer> ids);

    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> findFlavorById(Long dishId);

    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteById(Long dishId);

    void add(@Param("dishFlavors") List<DishFlavor> dishFlavors);
}
