package com.woowle.sugarcoatedhaws.mapper;

import com.woowle.sugarcoatedhaws.model.Category;
import com.woowle.sugarcoatedhaws.model.response.ChildCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Create By xiaoyin.lu.o on 2018/12/19
 **/
public interface CategoryMapper extends Mapper<Category> {
    public List<ChildCategory> getChildNode(@Param("categoryCode") String categoryCode);

}
