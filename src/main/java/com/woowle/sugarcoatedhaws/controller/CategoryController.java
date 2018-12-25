package com.woowle.sugarcoatedhaws.controller;

import com.woowle.sugarcoatedhaws.common.VO.Result;
import com.woowle.sugarcoatedhaws.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhang
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/getCategory1Code")
    public Result getCategory1Code() {

        return categoryService.getCategory1Code();
    }

    @PostMapping("/getCategory/all")
    public Result getAllCategory() {

        return categoryService.getAllCategory();
    }

    @PostMapping("/getChildNode")
    public Result getChildNode(String categoryCode) {

        return categoryService.getChildNode(categoryCode);
    }
}
