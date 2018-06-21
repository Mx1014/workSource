package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * Created by sfyan on 2017/4/19.
 */
public class ImportFileResultLog<T> {

    private T data;

    private String errorLog;

    private String scope;

    private Integer code;

    private String fieldName;

    private String sheetName;

    private String errorDescription;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ImportFileResultLog(String scope){
        this.scope = scope;
    }

    public String getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
