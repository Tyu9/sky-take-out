package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Value("${sky.salt}")
    private String salt;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        // 对密码进行md5加盐加密操作
        String password = DigestUtils.md5DigestAsHex((employeeLoginDTO.getPassword() + salt).getBytes());
        //根据用户名查询数据库中的数据
        Employee employee = employeeMapper.login(employeeLoginDTO.getUsername());
        // 判断传递的账号是否存在
        if (employee == null) {
            //返回账号不存在Exception
            log.info(MessageConstant.ACCOUNT_NOT_FOUND);
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        } else if (!employee.getPassword().equals(password)) {
            //判断密码是否正确
            log.info(MessageConstant.PASSWORD_ERROR);
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        } else if (employee.getStatus() == StatusConstant.DISABLE) {
            //判断账号是否被锁定
            log.info(MessageConstant.ACCOUNT_LOCKED);
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        return employee;
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        //将employeeDTO转为employee类作为对数据库的操作对象
        Employee employee = new Employee();
        //BeanUtils.copyProperties(A,B)将A中的数据复制到B中，要求属性名相同
        BeanUtils.copyProperties(employeeDTO, employee);
        //补全employee对象属性
        String token = DigestUtils.md5DigestAsHex((PasswordConstant.DEFAULT_PASSWORD + salt).getBytes());
        employee.setStatus(StatusConstant.ENABLE);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());
        employee.setPassword(token);
        //调用mapper层方法
        employeeMapper.save(employee);
    }

    /**
     * 分页查询员工
     *
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult page(EmployeePageQueryDTO employeePageQueryDTO) {
        //设置分页查询的页码和每页记录数
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        //数据库中查询数据存入集合
        List<Employee> employeeList = employeeMapper.page(employeePageQueryDTO.getName());
        Page<Employee> page= (Page<Employee>)employeeList;
        //封装返回总记录数和数据
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 修改员工账号状态
     *
     * @param employee
     */
    @Override
    public void update(Employee employee) {
        //1、补全修改时间、修改人
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());
        //2、调用mapper层方法
        employeeMapper.update(employee);
    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @Override
    public Employee findById(Long id) {
       Employee employee = employeeMapper.findById(id);
        return employee;
    }

    /**
     * 修改员工密码
     *
     * @param passwordEditDTO
     */
    @Override
    public void updatePassword(PasswordEditDTO passwordEditDTO) {
        //获取到传入的旧密码并进行加盐加密
        String password = DigestUtils.md5DigestAsHex((passwordEditDTO.getOldPassword() + salt).getBytes());
        //通过id查询该员工信息
        Employee employee = employeeMapper.findById(passwordEditDTO.getEmpId());
        //判断是否能查到该员工
        if(employee == null){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        //判断输入的旧密码和数据库中密码是否对应
        if(!password.equals(employee.getPassword())){
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        //将新密码加盐加密
        employee.setPassword(DigestUtils.md5DigestAsHex((passwordEditDTO.getNewPassword()+salt).getBytes()));
        employeeMapper.update(employee);
    }
}
