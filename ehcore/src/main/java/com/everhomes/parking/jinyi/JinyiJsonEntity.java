package com.everhomes.parking.jinyi;

import com.everhomes.util.StringHelper;

/**
 * Created by Administrator on 2017/7/13.
 */
public class JinyiJsonEntity<T> {
    private Integer Status;
    private String Message;
    private String ErrorCode;
    private T Data;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    public boolean isSuccess() {
        if (this.Status.equals(1)) {
            return true;
        }
        return false;
    }
}
