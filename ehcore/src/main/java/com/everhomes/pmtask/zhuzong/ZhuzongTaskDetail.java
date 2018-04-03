package com.everhomes.pmtask.zhuzong;

import java.util.List;

/**
 * Created by Administrator on 2018/4/2.
 */
public class ZhuzongTaskDetail {
    static final int SUCCESS_CODE = 0;
    private ZhuzongTasksData Result;
    private Integer StateCode;
    public Boolean isSuccess() {
        if(StateCode == SUCCESS_CODE)
            return true;
        return false;
    }

    public static int getSuccessCode() {
        return SUCCESS_CODE;
    }

    public ZhuzongTasksData getResult() {
        return Result;
    }

    public void setResult(ZhuzongTasksData result) {
        Result = result;
    }

    public Integer getStateCode() {
        return StateCode;
    }

    public void setStateCode(Integer stateCode) {
        StateCode = stateCode;
    }
}
