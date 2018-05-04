package com.everhomes.parking.fujica;

import com.everhomes.util.StringHelper;

public class FujicaResponse<T> {
    private T JsonParam;
    private Byte DataType;
    private Boolean IsSuccess;
    private Integer MessageCode;
    private String MessageContent;

    public boolean isSuccess(){
        if(IsSuccess!=null) {
            return IsSuccess;
        }
        return false;
    }

    public T getJsonParam() {
        return JsonParam;
    }

    public void setJsonParam(T jsonParam) {
        JsonParam = jsonParam;
    }

    public Byte getDataType() {
        return DataType;
    }

    public void setDataType(Byte dataType) {
        DataType = dataType;
    }

    public Boolean getSuccess() {
        return IsSuccess;
    }

    public void setSuccess(Boolean success) {
        IsSuccess = success;
    }

    public Integer getMessageCode() {
        return MessageCode;
    }

    public void setMessageCode(Integer messageCode) {
        MessageCode = messageCode;
    }

    public String getMessageContent() {
        return MessageContent;
    }

    public void setMessageContent(String messageContent) {
        MessageContent = messageContent;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
