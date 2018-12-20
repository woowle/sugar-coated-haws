package com.woowle.sugarcoatedhaws.common.exception;

import com.woowle.sugarcoatedhaws.common.VO.ResultCode;
import lombok.extern.slf4j.Slf4j;

/**
 * Create By xiaoyin.lu.o on 2018/9/13
 **/
@Slf4j
public class AffraityException extends RuntimeException {
  private String module;
  private String code;
  private String msg;


  private final static String ERROR_MSG_TEMPLATE_MODULE = "Module : {} , Code : {},Msg : {}";
  private final static String ERROR_MSG_TEMPLATE_NO_MODULE = " Code : {},Msg : {}";

  public AffraityException(String code){
    super(ResultCode.getValueByKey(code));
    this.code = code;
    this.msg = ResultCode.getValueByKey(code);
    log.error(ERROR_MSG_TEMPLATE_NO_MODULE,this.code,ResultCode.getValueByKey(code));
  }

  public AffraityException(String code,String msg){
    super(msg);
    this.code = code;
    this.msg = msg;
    log.error(ERROR_MSG_TEMPLATE_NO_MODULE,this.code,this.msg);
  }

  public AffraityException(String code,String msg,String module){
    super(msg);
    this.code = code;
    this.msg = msg;
    this.module = module;
    log.error(ERROR_MSG_TEMPLATE_MODULE,this.module,this.code,this.msg);
  }

  public AffraityException(String code,Exception e){
    super(e);
    this.code = code;
    this.msg = e.getMessage();
    log.error(ERROR_MSG_TEMPLATE_NO_MODULE,this.code,this.msg);
  }

  public AffraityException(Exception e){
    super(e);
  }

  public String getCode() {
    return code;
  }

  public String getMsg() {
    return msg;
  }

  public String getModule() {
    return module;
  }


}
