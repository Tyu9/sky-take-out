package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author：tantantan
 * @Package：com.sky.controller.admin
 * @Project：sky-take-out
 * @name：SetmealController
 * @Date：2024/5/27 16:55
 * @Filename：SetmealController
 */
@Slf4j
@Api(tags = "套餐相关接口")
@RestController
@RequestMapping("/admin/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @ApiOperation("套餐分页查询接口")
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("套餐分页查询接口");
        PageResult pages =  setmealService.page(setmealPageQueryDTO);
        return Result.success(pages);
    }

    /**
     *
     * @ApiOperation("新增套餐接口")
     * @param setmealDTO
     * @return
     */
    @CacheEvict(value = "setemal",key = "#setmealDTO.categoryId")
    @ApiOperation("新增套餐接口")
    @PostMapping
    public Result add(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐操作");
        setmealService.add(setmealDTO);
        return Result.success();
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @ApiOperation("根据id查询套餐接口")
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id){
        log.info("ID查询套餐操作");
        SetmealVO setmealVO = setmealService.findById(id);
        return Result.success(setmealVO);
    }

    /**
     * 修改套餐接口
     * @param setmealDTO
     * @return
     */
    @CacheEvict(value = "setmeal" , key = "#setmealDTO.categoryId")
    @ApiOperation("修改套餐接口")
    @PutMapping
    public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐操作:{}",setmealDTO);
        setmealService.updateSetmeal(setmealDTO);
        return Result.success();
    }

    /**
     * 修改套餐起售/禁止接口
     * @param status
     * @param id
     * @return
     */
    @CacheEvict(value = "setmeal" , allEntries = true)
    @ApiOperation("更改套餐状态接口")
    @PostMapping("/status/{status}")
    public Result modifyStatus(@PathVariable Integer status, Long id){
        log.info("更改套餐状态操作");
        setmealService.modifyStatus(status, id);
        return Result.success();
    }

    /**
     * 批量删除套餐接口
     * @param ids
     * @return
     */
    @CacheEvict(value = "setmeal" , allEntries = true)
    @ApiOperation("批量删除套餐接口")
    @DeleteMapping
    public Result deleteByIds(@RequestParam List<Long> ids){
        log.info("批量删除套餐操作");
        setmealService.deleteByIds(ids);
        return Result.success();
    }
}
