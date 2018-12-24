package com.woowle.sugarcoatedhaws.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * Create By xiaoyin.lu.o on 2018/12/19
 **/
@Data
public class Category extends Model<Category> {
  @TableId(value = "id")
  private Integer id;

  private String categoryCode;

  private String name;

  private Integer level;

  private Integer parentNodeCode;

  private Integer isDelete;


  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return id;
  }
}
