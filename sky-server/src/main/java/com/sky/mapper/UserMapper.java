package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * @Author：tantantan
 * @Package：com.sky.mapper
 * @Project：sky-take-out
 * @name：UserMapper
 * @Date：2024/6/1 9:19
 * @Filename：UserMapper
 */
@Mapper
public interface UserMapper {
    @Select("select * from user where openid = #{openid}")
    public User getByOpenid(String openid);
    @Insert("insert into user values (null,#{openid},#{name},#{phone},#{sex},#{idNumber}, #{avatar},#{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);
}
