package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.BaseException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author：tantantan
 * @Package：com.sky.service.impl
 * @Project：sky-take-out
 * @name：setmeal
 * @Date：2024/5/27 16:54
 * @Filename：setmeal
 */
@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        /*  分页查询
            1、通过PageHelper设置起始页和每页数据
            2、调用mapper层查询到所有数据
            3、将查询到的数据集合转换成PageResult对象
         */
        //1、通过PageHelper设置起始页和每页数据
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        //2、调用mapper层查询到所有数据
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealPageQueryDTO, setmeal);
        //3、将查询到的数据集合转换成PageResult对象
        Page<SetmealVO> pages = setmealMapper.page(setmeal);
        return new PageResult(pages.getTotal(), pages.getResult());
    }

    /**
     * 新增套餐
     *
     * @param setmealDTO
     */
    @Transactional
    @Override
    public void add(SetmealDTO setmealDTO) {
        /*
            1、将DTO类型转为entity类型
            2、由于涉及到两个表，setmeal 和 setmeal_dish，所以需要调用mapper层
         */
        //1、将DTO类型转为entity类型
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //2、将数据插入对应表中
        //2.1 插入setmeal表
        setmealMapper.add(setmeal);
        //2.2 插入setmeal_dish表
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmeal.getId());
        });
        setmealDishMapper.add(setmealDishes);
    }

    /**
     * 根据id查询套餐
     *
     * @param id
     * @return
     */
    @Override
    public SetmealVO findById(Long id) {
        /*
            1、通过套餐ID查询套餐表Setmeal信息
            2、通过套餐ID查询套餐配菜Setmeal_Dish信息
            3、封装为VO返回数据
         */
        SetmealVO setmealVO = setmealMapper.findById(id);
        List<SetmealDish> setmealDishes = setmealDishMapper.findBySetmealId(id);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 修改套餐内容
     *
     * @param setmealDTO
     */
    @Transactional
    @Override
    public void updateSetmeal(SetmealDTO setmealDTO) {
        /*
            1、将DTO转换为Mapper使用的数据类型Entity
            2、调用mapper层方法将setmeal更新到setmeal表
            3、获取DTO中菜品信息
            4、根据套餐ID将套餐菜品表中的信息删除
            5、将DTO中的菜品信息插入到setmeal_dish表中
         */
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.updateSetmeal(setmeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes == null || setmealDishes.size() == 0) {
            throw new BaseException("添加套餐菜品为空");
        }
        setmealDishes.forEach(s -> {
            s.setSetmealId(setmealDTO.getId());
        });
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());
        setmealDishMapper.add(setmealDishes);
    }

    /**
     * 修改套餐在售/停售状态
     *
     * @param status
     * @param id
     */
    @Override
    public void modifyStatus(Integer status, Long id) {
        /*
            1、判断套餐中是否存在未在售的菜品
            2、调用mapper层修改套餐状态
         */
        setmealDishMapper.findBySetmealId(id).forEach(s -> {
            if (dishMapper.findById(s.getDishId()).getStatus() == 0) {
                throw new BaseException(MessageConstant.SETMEAL_ENABLE_FAILED);
            }
        });
        setmealMapper.modifyStatus(status, id);
    }

    /**
     * 批量删除套餐
     *
     * @param ids
     */
    @Override
    public void deleteByIds(List<Long> ids) {
        /*
            0、判断删除的套餐是否存在在售状态
            1、删除setmeal_dish表中套餐中对应菜品信息
            2、删除setmeal表中套餐
                    ids.forEach(id->{
            if(setmealMapper.findById(id).getStatus()==StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });
        ids.forEach(id->{
            setmealMapper.deleteByid(id);
            setmealDishMapper.deleteBySetmealId(id);
        });
         */


        for (int i = 0; i < ids.size(); i++) {
            if (setmealMapper.findById(ids.get(i)).getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        setmealDishMapper.deleteByIds(ids);
        setmealMapper.deleteByIds(ids);
    }
    @Override
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
