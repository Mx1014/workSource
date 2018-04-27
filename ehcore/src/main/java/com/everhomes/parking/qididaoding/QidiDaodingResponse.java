// @formatter:off
package com.everhomes.parking.qididaoding;

import com.everhomes.util.StringHelper;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/4/17 14:22
 */
public class QidiDaodingResponse<T> {
    private Integer errorCode;/*200	请求成功 300	token过期 400	签名错误 500	其他错误，具体看错误信息errorMsg*/
    private String errorMsg;
    private T data;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public boolean isSuccess() {
        return errorCode!=null && errorCode==200;
    }

    public boolean isTokenOutOfDate() {
        return errorCode!=null && (errorCode==300 || errorCode==500);
    }
}
