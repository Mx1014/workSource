// @formatter:off
package com.everhomes.parking.guangdawegu;

import com.everhomes.util.StringHelper;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/1/30 16:21
 */
public class GuangDaWeGuResponse<T> {
    private T data;
    private Boolean success;
    private String errorMsg;
    private String errorCode;

    public boolean isSuccess(){
        if(success!=null) {
            return success;
        }
        return false;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
