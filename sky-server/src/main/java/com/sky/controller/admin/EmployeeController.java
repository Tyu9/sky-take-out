package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@Api(tags = "员工管理相关接口")
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
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        claims.put(JwtClaimsConstant.USERNAME, employee.getUsername());
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

    /**
     * 添加员工
     *
     * @param
     * @return
     */
    @ApiOperation("添加员工接口")
    @PostMapping()
    public Result<String> add(@RequestBody EmployeeDTO employeeDTO) {
        log.info("添加员工操作：" + employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * 分页查询员工
     *
     * @param employeePageQueryDTO
     * @return Result
     */
    @ApiOperation("分页查询员工接口")
    @GetMapping("/page")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("分页查询员工操作：" + employeePageQueryDTO);
        PageResult pageResult = employeeService.page(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 修改员工账号状态
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("修改员工账号状态")
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status, Long id) {
        log.info("修改员工状态操作：" + status + "操作员工：" + id);
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeService.update(employee);
        return Result.success();
    }

    /**
     *  根据ID查询员工
     * @param id
     * @return
     */
    @ApiOperation("根据ID查询员工")
    @GetMapping("/{id}")
    public Result<Employee> findById(@PathVariable Long id) {
        log.info("根据ID查询员工操作：" + id);
        Employee employee= employeeService.findById(id);
        return Result.success(employee);
    }
    /**
     * 修改员工信息
     * @param employee
     * @return
     */
    @ApiOperation("修改员工信息")
    @PutMapping
    public Result update(@RequestBody Employee employee) {
        log.info("修改员工信息操作：" + employee);
        employeeService.update(employee);
        return Result.success();
    }
    /**
     * 该功能前端存在瑕疵导致无法实现：前端只传进来了newPassword和oldPassword，无法获取到empId
     * 修改员工账户密码
     * @param passwordEditDTO
     * @return
     */
    @ApiOperation("修改员工账户密码")
    @PutMapping("/editPassword")
    public Result editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        log.info("修改员工账户密码操作：" + passwordEditDTO);
        employeeService.updatePassword(passwordEditDTO);
        return Result.success();
    }
}
