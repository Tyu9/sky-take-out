package com.sky.controller.admin;

import com.sky.constant.StatusConstant;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

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
    @Resource
    private RedisTemplate redisTemplate;
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
        //删除redis中缓存，这样下次查询时就是数据库中新的数据
        redisTemplate.delete("dish_"+dishDTO.getCategoryId());
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
        //清空所有缓存dish数据
        String key = "dish_*";
        Set keys = redisTemplate.keys(key);
        redisTemplate.delete(keys);
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
        //清空所有缓存dish数据
        String key = "dish_*";
        Set keys = redisTemplate.keys(key);
        redisTemplate.delete(keys);
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
        //清楚所有缓存
        String key =  "dish_*";
        redisTemplate.delete(key);
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @ApiOperation("根据分类id查询菜品接口")
    @GetMapping("/list")
    public Result<List<DishVO>> list( Long categoryId){
        /*
            1、先去redis查询是否存在对于的缓存数据
            2、如果存在则直接返回查找的数据
            3、如果不存在则再去数据库查找,并将查找到的数据写入redis缓存
         */
        log.info("根据分类ID查询菜品");
//        String key = "dish_"+categoryId;
//        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
//        if(list == null){
//            Dish dish = new Dish();
//            dish.setCategoryId(categoryId);
//            dish.setStatus(StatusConstant.ENABLE);
//            list = dishService.listWithFlavor(dish);
//            if(list != null){
//                redisTemplate.opsForValue().set(key,list);
//            }
//        }
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);
        List<DishVO> list = dishService.listWithFlavor(dish);
        return Result.success(list);
    }

}
