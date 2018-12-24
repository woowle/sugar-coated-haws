package com.woowle.sugarcoatedhaws.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.woowle.sugarcoatedhaws.common.VO.Result;
import com.woowle.sugarcoatedhaws.enums.IsDeleteEnum;
import com.woowle.sugarcoatedhaws.enums.LevelEnum;
import com.woowle.sugarcoatedhaws.mapper.CategoryMapper;
import com.woowle.sugarcoatedhaws.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author  zhang
 */

@Transactional
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public Result getCategory1Code(){

        Category category = new Category();
        category.setLevel(LevelEnum.LEVEL_1.getValue());
        category.setIsDelete(IsDeleteEnum.IS_DELETE_FALSE.getValue());
        return Result.success(categoryMapper.selectList(new QueryWrapper<>(category)));

    }

    public Result getAllCategory(){

        Category category = new Category();
        category.setIsDelete(IsDeleteEnum.IS_DELETE_FALSE.getValue());
        return Result.success(categoryMapper.selectList(new QueryWrapper<>(category)));

    }

    public Result getChildNode(String categoryCode){

        return Result.success(categoryMapper.getChildNode(categoryCode));

    }

}
