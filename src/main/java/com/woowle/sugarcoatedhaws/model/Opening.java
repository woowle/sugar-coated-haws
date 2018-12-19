package com.woowle.sugarcoatedhaws.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import org.springframework.data.annotation.Id;

/**
 * Create By xiaoyin.lu.o on 2018/12/19
 **/
public class Opening extends Model<Opening> {
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return id;
  }
}
