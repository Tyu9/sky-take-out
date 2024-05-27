package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author：tantantan
 * @Package：com.sky.service.impl
 * @Project：sky-take-out
 * @name：CategoryServiceImpl
 * @Date：2024/5/22 19:37
 * @Filename：CategoryServiceImpl
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    /**
     * 分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(CategoryPageQueryDTO categoryPageQueryDTO) {
        //1、调用pageHelper设置起始页和每页数据
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        //2、调用mapper层查询所有数据返回list集合
        Page<Category> categoryPage = categoryMapper.pageQuery(categoryPageQueryDTO);
        if (categoryPage.size() == 0) {
            categoryPageQueryDTO.setPage(categoryPageQueryDTO.getPage() - 1);
            categoryPage = categoryMapper.pageQuery(categoryPageQueryDTO);
        }
        //4、封装为PageResult并传入总记录数和数据
        return new PageResult(categoryPage.getTotal(), categoryPage.getResult());
    }

    /**
     * 更新分类信息
     *
     * @param category
     */
    @Override
    public void update(Category category) {
        //1、补全信息
//        category.setUpdateTime(LocalDateTime.now());
//        category.setUpdateUser(BaseContext.getCurrentId());
        //2、调用mapper层更新操作
        categoryMapper.update(category);
    }

    /**
     * 新增分类
     *
     * @param categoryDTO
     */
    @Override
    public void add(CategoryDTO categoryDTO) {
        //1、将DTO数据转为Entity数据
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        //2、补全category信息
//        category.setUpdateUser(BaseContext.getCurrentId());
//        category.setCreateTime(LocalDateTime.now());
//        category.setCreateUser(BaseContext.getCurrentId());
//        category.setUpdateTime(LocalDateTime.now());
        category.setStatus(StatusConstant.ENABLE);
        //3、调用mapper层添加数据
        categoryMapper.add(category);
    }

    /**
     * 删除分类
     *
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        categoryMapper.deleteById(id);
    }

    /**
     * 根据类型查询分类
     *
     * @param type
     * @return
     */
    @Override
    public List<Category> findByType(Integer type) {
        List<Category> categoryList = categoryMapper.findByType(type);
        return categoryList;
    }
}
