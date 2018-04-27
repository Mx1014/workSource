package com.everhomes.pmtask.zhuzong;

public class ZhuzongJsonEntity<T> {
    static final int SUCCESS_CODE = 0;
    private Integer StateCode;
    private T Result;

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

    public T getResult() {
        return Result;
    }

    public void setResult(T result) {
        Result = result;
    }
}
