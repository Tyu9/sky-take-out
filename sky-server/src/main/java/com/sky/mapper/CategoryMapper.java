package com.sky.mapper;

import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author：tantantan
 * @Package：com.sky.mapper
 * @Project：sky-take-out
 * @name：CategoryMapper
 * @Date：2024/5/22 19:36
 * @Filename：CategoryMapper
 */
@Mapper
public interface CategoryMapper {
    List<Category> page(Category category);

    void update(Category category);
    @Insert("insert into category values (null,#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void add(Category category);

    @Delete("delete  from category where  id = #{id}")
    void deleteById(Long id);
    @Select("select * from category where type = #{type}")
    List<Category> findByType(Integer type);
}
