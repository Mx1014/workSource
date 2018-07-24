package com.everhomes.pmtask.zhuzong;

import java.util.List;


public class ZhuzongTasks {
    static final int SUCCESS_CODE = 0;
    private ZhuzongTasksResult Result;
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

    public ZhuzongTasksResult getResult() {
        return Result;
    }

    public void setResult(ZhuzongTasksResult result) {
        Result = result;
    }
}
