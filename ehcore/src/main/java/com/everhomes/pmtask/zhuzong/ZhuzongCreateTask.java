package com.everhomes.pmtask.zhuzong;

/**
 * Created by Administrator on 2018/3/30.
 */
public class ZhuzongCreateTask {
    static final int SUCCESS_CODE = 0;
    private String Result;
    private Integer StateCode;
    public Boolean isSuccess() {
        if(StateCode == SUCCESS_CODE)
            return true;
        return false;
    }

    public static int getSuccessCode() {
        return SUCCESS_CODE;
    }

    public Integer getStateCode() {
        return StateCode;
    }

    public void setStateCode(Integer stateCode) {
        StateCode = stateCode;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }
}
