package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author：tantantan
 * @Package：com.sky.controller.admin
 * @Project：sky-take-out
 * @name：DishController
 * @Date：2024/5/24 20:24
 * @Filename：DishController
 */
@Api(tags ="菜品相关接口")
@RestController
@Slf4j
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @ApiOperation("根据id查询菜品接口")
    @GetMapping("/{id}")
    public Result<DishVO> findById(@PathVariable Long id) {
        log.info("根据id查询菜品操作");
        DishVO byId = dishService.findById(id);
        return Result.success(byId);
    }

    /**
     * 修改菜品接口
     * @param dishDTO
     * @return
     */
    @ApiOperation("修改菜品接口")
    @PutMapping
    public Result updateDish(@RequestBody DishDTO dishDTO){
        log.info("修改菜品操作:{}", dishDTO);
        dishService.updateDish(dishDTO);
        return Result.success();
    }

    /**
     * 新增菜品
     *
     * @param dishDTO
     * @return
     */
    @ApiOperation("新增菜品接口")
    @PostMapping
    public Result add(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品操作");
        dishService.add(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    @ApiOperation("菜品分页查询接口")
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询操作");
        PageResult page = dishService.page(dishPageQueryDTO);
        return Result.success(page);
    }

    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    @ApiOperation("批量删除接口")
    @DeleteMapping()
    public Result deleteByIds(@RequestParam List<Integer> ids){
        log.info("批量删除操作");
        dishService.deleteByIds(ids);
        return Result.success();
    }

    /**
     *
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("修改菜品状态接口")
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status, Long id){
        DishDTO dishDTO = new DishDTO();
        dishDTO.setStatus(status);
        dishDTO.setId(id);
        log.info("更改菜品状态");
        dishService.updateStatus(dishDTO);
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @ApiOperation("根据分类id查询菜品接口")
    @GetMapping("/list")
    public Result<List<Dish>> list( Long categoryId){
        log.info("根据分类ID查询菜品");
        List<Dish> dish = dishService.list(categoryId);
        return Result.success(dish);
    }

}
