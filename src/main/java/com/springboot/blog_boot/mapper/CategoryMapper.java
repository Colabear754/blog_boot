package com.springboot.blog_boot.mapper;


import com.springboot.blog_boot.domain.CategoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CategoryMapper {
    public List<CategoryVO> getCategories();
    public CategoryVO getCategory(int category_id);
    public int addCategory(CategoryVO category);
    public int updateCategory(Map<String, Object> params);
    public int deleteCategory(int category_id);
}
