package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {
    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     *
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 分页查询员工
     * @param employeePageQueryDTO
     * @return
     */
    PageResult page(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 修改员工账号状态
     * @param employee
     */
    void update(Employee employee);

    /**
     * 根据ID查询员工
     * @param id
     * @return
     */
    Employee findById(Long id);

    /**
     * 修改员工密码
     * @param passwordEditDTO
     */
    void updatePassword(PasswordEditDTO passwordEditDTO);
}
