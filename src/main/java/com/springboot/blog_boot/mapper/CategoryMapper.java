package com.example.blog.mapper;

import com.example.blog.domain.CategoryVO;

import java.util.List;
import java.util.Map;

public interface CategoryMapper {
    public List<CategoryVO> getCategories();
    public CategoryVO getCategory(int category_id);
    public int addCategory(CategoryVO category);
    public int updateCategory(Map<String, Object> params);
    public int deleteCategory(int category_id);
}
