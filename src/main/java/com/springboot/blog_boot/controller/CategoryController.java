package com.springboot.blog_boot.controller;

import com.springboot.blog_boot.domain.CategoryVO;
import com.springboot.blog_boot.mapper.CategoryMapper;
import com.springboot.blog_boot.result.DeleteCategoryResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "카테고리 API")
@RestController
@RequestMapping("/category/*")
public class CategoryController {
    @Autowired
    private CategoryMapper categoryDao;

    @ApiOperation("카테고리 추가")
    @ApiImplicitParam(name = "name", value = "카테고리 이름", required = true)
    @PostMapping("/add")
    public CategoryVO addCategory(@RequestParam String name, Model model) {
        CategoryVO category = new CategoryVO(0, name);

        model.addAttribute("result", categoryDao.addCategory(category));

        return categoryDao.getCategory(category.getCategory_id());
    }

    @ApiOperation("카테고리 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category_id", value = "수정하려는 카테고리 번호", required = true, paramType = "path"),
            @ApiImplicitParam(name = "name", value = "변경할 카테고리 이름", required = true, paramType = "query")
    })
    @PutMapping("/edit/{category_id}")
    public CategoryVO editCategory(@PathVariable int category_id, @RequestParam String name, Model model) {
        Map<String, Object> input = new HashMap<>();
        input.put("category_id", category_id);
        input.put("name", name);

        int result = categoryDao.updateCategory(input);

        model.addAttribute("result", result);

        return categoryDao.getCategory(category_id);
    }

    @ApiOperation("카테고리 삭제")
    @ApiImplicitParam(name = "category_id", value = "삭제할 카테고리 번호", required = true, paramType = "path")
    @DeleteMapping("/delete/{category_id}")
    public DeleteCategoryResult deleteCategory(@PathVariable int category_id, Model model) {
        int result = categoryDao.deleteCategory(category_id);

        model.addAttribute("result", result);

        return new DeleteCategoryResult(category_id, result > 0);
    }
}
