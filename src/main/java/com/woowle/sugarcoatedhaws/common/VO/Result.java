package com.woowle.sugarcoatedhaws.common.VO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Create By xiaoyin.lu.o on 2018/11/5
 **/
@Getter
public class Result {
  private Integer success;
  @SerializedName("result_code")
  private String resultCode;
  @SerializedName("result_data")
  private Object resultData;
  @SerializedName("request_id")
  private String requestId;


  private Result(Integer success){
    this.success=success;
  }

  @Override
  public String toString(){
    return toJson();
  }

  public String toJson(){
    return new Gson().toJson(this);
  }

  public static Result success(){
    Result result = new Result(1);
    return result;
  }

  public static Result success(Object resultData){
    Result result = success();
    result.resultData = resultData;
    return result;

  }

  public static  Result failed(String resultCode){
    Result result = new Result(0);
    result.resultCode = resultCode;
    result.resultData = ResultCode.getValueByKey(resultCode);
    return result;
  }

  public static Result failed(String resultCode,String failedReason){
    Result result = failed(resultCode);
    result.resultData = failedReason;
    return result;
  }

  @JsonIgnore
  public boolean isSuccess(){
    return 1 == this.success;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }
}
