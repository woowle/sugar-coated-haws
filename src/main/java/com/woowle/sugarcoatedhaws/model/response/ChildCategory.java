package com.woowle.sugarcoatedhaws.model.response;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.woowle.sugarcoatedhaws.model.Category;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Create By xiaoyin.lu.o on 2018/12/19
 **/
@Getter
@Setter
public class ChildCategory {
  private Integer id;

  private String categoryCode;

  private String name;

  private Integer level;

  private Integer parentNodeCode;

  private Integer isDelete;
  private List<Category> child;


}
