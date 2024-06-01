package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author：tantantan
 * @Package：com.sky.controller.admin
 * @Project：sky-take-out
 * @name：CategoryController
 * @Date：2024/5/22 19:36
 * @Filename：CategoryController
 */
@Api(tags ="菜品分类接口")
@RestController
@Slf4j
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        //1、日志打印操作
        log.info("分页查询分类:{}",categoryPageQueryDTO);
        //2、调用Service层方法
        PageResult page = categoryService.page(categoryPageQueryDTO);
        return Result.success(page);
    }

    /**
     * 修改分类状态
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("分类状态设置")
    @PostMapping("/status/{status}")
    public Result status(@PathVariable Integer status, Long id){
        //1、日志记录操作
        log.info("修改分类状态:{},ID：{}",status,id);
        Category category = Category.builder()
                .status(status)
                .id(id)
                .build();
        categoryService.update(category);
        return Result.success();
    }
    /**
     *修改分类信息
     * @param categoryDTO
     * @return
     */
    @ApiOperation("修改分类信息")
    @PutMapping()
    public Result update(@RequestBody CategoryDTO categoryDTO){
        //1、日志记录操作
        log.info("修改分类信息:{}",categoryDTO);
        //2、转为Entity类型
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        //3、调用Service层方法
        categoryService.update(category);
        return Result.success();
    }

    /**
     *添加新的分类
     * @param categoryDTO
     * @return
     */
    @ApiOperation("添加分类")
    @PostMapping()
    public Result add(@RequestBody CategoryDTO categoryDTO){
        //1、日志打印操作
        log.info("添加分类:{}",categoryDTO);
        //2、调用Service新增操作
        categoryService.add(categoryDTO);
        //3、返回结果
        return Result.success();
    }

    /**
     * 根据ID删除分类
     * @param id
     * @return
     */
    @ApiOperation("删除分类")
    @DeleteMapping()
    public Result delete(Long id){
        //1、日志打印操作
        log.info("删除分类");
        //2、调用Service根据ID删除分类
        categoryService.deleteById(id);
        //3、返回Result
        return Result.success();
    }

    /**
     * 瑕疵功能，实际前端请求的路径还是/admin/category/page
     * 按照类型查询分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    public Result<List> findByType(Integer type){
        //1、日志记录操作
        log.info("根据类型查询分类");
        //2、调用Service执行分页查找操作
        List<Category> categories = categoryService.findByType(type);
        //3、返回结果
        return Result.success(categories);
    }
}
