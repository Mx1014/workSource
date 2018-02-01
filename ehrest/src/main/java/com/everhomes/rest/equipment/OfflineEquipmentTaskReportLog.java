package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * Created by rui.jia  2018/1/11 12 :19
 */

public class OfflineEquipmentTaskReportLog {

    private Long sucessIds;

    private Long errorIds;

    private Integer errorCode;

    private String errorDescription;

    public Long getSucessIds() {
        return sucessIds;
    }

    public void setSucessIds(Long sucessIds) {
        this.sucessIds = sucessIds;
    }

    public Long getErrorIds() {
        return errorIds;
    }

    public void setErrorIds(Long errorIds) {
        this.errorIds = errorIds;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
