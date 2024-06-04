package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.BaseException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author：tantantan
 * @Package：com.sky.service.impl
 * @Project：sky-take-out
 * @name：DishServiceImpl
 * @Date：2024/5/24 20:26
 * @Filename：DishServiceImpl
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    /**
     * 添加菜品
     *
     * @param dishDTO
     */
    @Transactional
    @Override
    public void add(DishDTO dishDTO) {
        //该操作涉及到两个表，菜品表和口味表
        //1、添加菜品表
            //dishDTO和dish对象属性名一致，可以进行属性拷贝
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.add(dish);
        //2、添加口味表
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        if(dishFlavors == null || dishFlavors.size() == 0){
            throw new BaseException("口味表为空");
        }
        dishFlavors.forEach(dishFlavor->dishFlavor.setDishId(dish.getId()));
        dishFlavorMapper.addFishFlavor(dishFlavors);
    }

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        //1、设置起始页和每页数据大小
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        //2、查询数据
        Page<DishVO> pages = (Page<DishVO>) dishMapper.page(dishPageQueryDTO);
        return new PageResult(pages.getTotal(),pages.getResult());
    }

    /**
     * 批量删除菜品
     *
     * @param ids
     */
    @Transactional
    @Override
    public void deleteByIds(List<Integer> ids) {
        /*
            1、根据ID查询批量删除的菜品中是否存在在售菜品/包含在套餐中的菜品
            2、存在则不可删除抛出异常
            3、不存在则先删除口味表再删除口味表
         */
        //1、根据Ids查询批量删除的菜品是否存在在售菜品，存在则不可删除
        //sql:select count(*) from dish where id in (id1,id2 ..) and status = 1
        Integer byIds = dishMapper.findStatusByIds(ids, StatusConstant.ENABLE);
        //如果查询到的数量>0 说明存在在售菜品
        if(byIds != null){
            log.info("存在在售菜品，不能删除");
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }
        //2、根据Ids查询套餐表批量删除的菜品是否存在关联套餐，存在则不可删除
        //sql:select count(*) from setmeal_dish where dish_id in (id1,id2 ..)
        Integer setmealDishByDishIds = dishMapper.findSetmealDishByDishIds(ids);
        //如果查询到数量>0 说明存在关联套餐
        if (setmealDishByDishIds != null){
            log.info("存在关联套餐，不能删除");
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //3、删除菜品
        //sql:delete from dish where id in (id1,id2 ..)
        dishMapper.deleteDishs(ids);
        //4、删除菜品口味
        //sql:delete from dish_flavor where dish_id in (id1,id2 ..)
        dishFlavorMapper.deleteDishFlavors(ids);
    }

    /**
     * 根据ID查询菜品信息
     *
     * @param id
     * @return
     */
    @Transactional
    @Override
    public DishVO findById(Long id) {
        /*
            1、通过id在菜品表中查询到菜品信息
            2、通过菜品ID在口味表中查询到口味信息
            3、将菜品信息封装到DishVO中
         */
        Dish dish = dishMapper.findById(id);
        List<DishFlavor> flavorById = dishFlavorMapper.findFlavorById(dish.getId());
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(flavorById);
        return dishVO;
    }

    /**
     * 修改菜品操作
     *
     * @param dishDTO
     */
    @Transactional
    @Override
    public void updateDish(DishDTO dishDTO) {
        /*一对多的修改操作，先删除多的一方数据在新增
         * 1、修改菜品表
         * 2、根据菜品ID删除旧的口味表中数据
         * 3、插入新的口味表数据
         */

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //改菜品表
        dishMapper.updateDish(dish);
        //改口味表
            //先删除旧的口味，再插入新的口味
        dishFlavorMapper.deleteById(dish.getId());
            //再增加新的口味
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        if(dishFlavors == null || dishFlavors.size() == 0){
            throw new BaseException("口味不能为空");
        }
        dishFlavors.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
        dishFlavorMapper.add(dishFlavors);
    }

    /**
     * 修改菜品状态操作
     *
     * @param dishDTO
     */
    @Override
    public void updateStatus(DishDTO dishDTO) {
        /*
            通过ID查询到要修改的菜品，将其状态设置为dishDTO中的状态
         */
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.updateDish(dish);
        if(dish.getStatus() == StatusConstant.DISABLE){
            //将对应套餐也停售
            // 如果是停售操作，还需要将包含当前菜品的套餐也停售
            List<Long> dishIds = new ArrayList<>();
            dishIds.add(dishDTO.getId());
            // select setmeal_id from setmeal_dish where dish_id in (?,?,?)
            List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(dishIds);
            if (setmealIds != null && setmealIds.size() > 0) {
                for (Long setmealId : setmealIds) {
                    Setmeal setmeal = Setmeal.builder()
                            .id(setmealId)
                            .status(StatusConstant.DISABLE)
                            .build();
                    setmealMapper.updateSetmeal(setmeal);
                }
            }
        }
    }

    /**
     * 根据分类id查询菜品
     *
     * @param id
     * @return
     */
    @Override
    public List<Dish> list(Long id) {
      List<Dish>  dish = dishMapper.listByCategoryId(id);
        return dish;
    }

    /**
     * 条件查询菜品和口味
     *
     * @param dish
     * @return
     */
    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);
        ArrayList<DishVO> list = new ArrayList<>();
        for (Dish dishVO : dishList) {
            DishVO vo = new DishVO();
            BeanUtils.copyProperties(dishVO,vo);
            List<DishFlavor> flavors = dishFlavorMapper.findFlavorById(dishVO.getId());
            vo.setFlavors(flavors);
            list.add(vo);
        }
        return list;
    }
}
