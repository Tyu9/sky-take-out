package com.sky.mapper;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {
    @Select("select * from employee where username = #{username}")
    Employee login(String username);

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */


}
