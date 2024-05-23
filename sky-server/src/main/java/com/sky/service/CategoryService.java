package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

/**
 * @Author：tantantan
 * @Package：com.sky.service
 * @Project：sky-take-out
 * @name：CategoryService
 * @Date：2024/5/22 19:37
 * @Filename：CategoryService
 */
public interface CategoryService {
    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult page(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 修改类信息
     * @param category
     */
    void update(Category category);

    /**
     * 添加分类
     * @param categoryDTO
     */
    void add(CategoryDTO categoryDTO);

    /**
     * 删除分类
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    List<Category> findByType(Integer type);
}
