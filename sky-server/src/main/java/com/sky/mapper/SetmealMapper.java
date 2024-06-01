package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author：tantantan
 * @Package：com.sky.mapper
 * @Project：sky-take-out
 * @name：SetmealMapper
 * @Date：2024/5/22 19:36
 * @Filename：SetmealMapper
 */
@Mapper
public interface SetmealMapper {
    Page<SetmealVO> page(Setmeal setmeal);

    @AutoFill(OperationType.INSERT)
    void add(Setmeal setmeal);

    SetmealVO findById(Long id);
    @AutoFill(OperationType.UPDATE)
    void updateSetmeal(Setmeal setmeal);

    @Update("update setmeal set status = #{status} where id = #{id}")
    void modifyStatus(Integer status, Long id);

    void deleteByIds(List<Long> ids);

    @Select("select * from setmeal where category_id = #{id}")
    List<Setmeal> findByCategoryId(Long id);
    List<Setmeal> list(Setmeal setmeal);
    /**
     * 根据套餐id查询菜品选项
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);
}
