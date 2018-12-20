package com.woowle.sugarcoatedhaws.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;

/**
 * Create By xiaoyin.lu.o on 2018/12/19
 **/
@Data
public class User extends Model<User> {
  @TableId(value = "id")
  private String id;

  private String userName;

  private String sugar;

  private String salt;

  private Integer accountId;

  private Integer role;

  public User(){}

  public User(String userName){
    this.userName = userName;
  }

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return id;
  }
}
