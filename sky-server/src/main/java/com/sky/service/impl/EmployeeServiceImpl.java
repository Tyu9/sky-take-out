package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

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
        String password=DigestUtils.md5DigestAsHex((employeeLoginDTO.getPassword()+salt).getBytes());
        //根据用户名查询数据库中的数据
        Employee employee = employeeMapper.login(employeeLoginDTO.getUsername());
        // 判断传递的账号是否存在
        if(employee==null){
            //返回账号不存在Exception
            log.info(MessageConstant.ACCOUNT_NOT_FOUND);
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }else if(!employee.getPassword().equals(password)){
            //判断密码是否正确
            log.info(MessageConstant.PASSWORD_ERROR);
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        } else if (employee.getStatus()==StatusConstant.DISABLE) {
            //判断账号是否被锁定
            log.info(MessageConstant.ACCOUNT_LOCKED);
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        return employee;
    }


}
