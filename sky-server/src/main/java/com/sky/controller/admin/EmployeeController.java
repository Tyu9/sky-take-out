package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 员工管理
 */
@Api(tags="员工管理相关接口")
@RestController
@RequestMapping("/admin/employee") //后续方法的请求路径统一在前面加上/admin/employee
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;
    @Value("${sky.jwt.admin-secret-key}")
    private String adminSecretKey;
    @Value("${sky.jwt.admin-ttl}")
    private Long adminTtl;
    /**
     * 登录
     * 请求方式：post
     * 请求路径：/admin/employee/login
     * 请求参数：username password
     * 返回数据类型：EmployeeLoginVO
     * 返回数据：token、ID、username、name
     *
     * @param employeeLoginDTO
     * @return
     */
    @ApiOperation("员工登录接口")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        //拿到请求参数username、password打印到日志
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();
        log.info("用户登录操作：用户名：" + username + "，密码：" + password);
        //去数据库查询登录的用户信息
        Employee employee = employeeService.login(employeeLoginDTO);
        //创建JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID,employee.getId());
        claims.put(JwtClaimsConstant.USERNAME,employee.getUsername());
            //方式一：自定义属性并在上方添加@Value("配置文件中对应全路径属性：sky.jwt.admin-secret-sky")
//        String token = JwtUtil.createJWT(adminSecretKey, adminTtl, claims);
            //方式二 : 通过properties包中的配置类@ConfigurationProperties(prefix="sky.jwt")读取配置文件
        String token = JwtUtil.createJWT(jwtProperties.getAdminSecretKey(), jwtProperties.getAdminTtl(), claims);
        //将令牌和对象封装进employeeLoginVO
            //方式一:普通全参构造器创建
//        EmployeeLoginVO employeeLoginVO1 = new EmployeeLoginVO(employee.getId(), employee.getUsername(), employee.getName(), token);
            //方式二：@Builder 底层有静态全参构造方法可以通过链式调用启用
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();
        //返回Result
        return Result.success(employeeLoginVO);
    }


    /**
     * 退出
     *
     * @return
     */
    @ApiOperation("员工退出登录接口")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

}
