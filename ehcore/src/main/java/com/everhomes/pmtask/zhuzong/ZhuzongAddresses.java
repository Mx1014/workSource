package com.everhomes.pmtask.zhuzong;

import com.everhomes.rest.energy.util.EnumType;

import java.util.List;

/**
 * Created by Administrator on 2018/3/30.
 */
public class ZhuzongAddresses {
    static final int SUCCESS_CODE = 0;
    private List<ZhuzongAddress> Result;
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

    public List<ZhuzongAddress> getResult() {
        return Result;
    }

    public void setResult(List<ZhuzongAddress> result) {
        Result = result;
    }
}
