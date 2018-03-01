//@formatter:off
package com.everhomes.rest.dynamicExcel;

/**
 * Created by Wentian Wang on 2018/1/12.
 */

public class DynamicImportResponse {
    private Integer successRowNumber = 0;
    private Integer failedRowNumber = 0;
    private Object storage;
    private String failCause;

    public String getFailCause() {
        return failCause;
    }

    public void setFailCause(String failCause) {
        this.failCause = failCause;
    }

    public Integer getSuccessRowNumber() {
        return successRowNumber;
    }

    public void setSuccessRowNumber(Integer successRowNumber) {
        this.successRowNumber = successRowNumber;
    }

    public Integer getFailedRowNumber() {
        return failedRowNumber;
    }

    public void setFailedRowNumber(Integer failedRowNumber) {
        this.failedRowNumber = failedRowNumber;
    }

    public Object getStorage() {
        return storage;
    }

    public void setStorage(Object storage) {
        this.storage = storage;
    }

    public void write2failCause(Integer numOfSheet) {
        this.failCause = "导入了"+numOfSheet+"个sheet页," +"共导入" + "成功了" + successRowNumber +"行,"+"失败了"+ failedRowNumber+"行";
        if(failCause != null){
            //failCause是导入失败的第一次原因
            this.failCause += "," + failCause;
        }
    }
}
