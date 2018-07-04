package com.everhomes.rest.rentalv2;

import com.everhomes.rest.rentalv2.admin.RentalOpenTimeDTO;

import java.util.List;

/**
 * <ul>
 * <li>usingInfos：使用订单信息{@link UsingInfoDTO}</li>
 * <li>currentUsingInfos: 正在使用的订单信息{@link UsingInfoDTO}</li>
 * </ul>
 */
public class GetResourceUsingInfoResponse {

    private List<UsingInfoDTO> usingInfos;
    private List<UsingInfoDTO> currentUsingInfos;
    private String openTimes;
    private String sourceName;

    public List<UsingInfoDTO> getUsingInfos() {
        return usingInfos;
    }

    public void setUsingInfos(List<UsingInfoDTO> usingInfos) {
        this.usingInfos = usingInfos;
    }

    public List<UsingInfoDTO> getCurrentUsingInfos() {
        return currentUsingInfos;
    }

    public void setCurrentUsingInfos(List<UsingInfoDTO> currentUsingInfos) {
        this.currentUsingInfos = currentUsingInfos;
    }

    public String getOpenTimes() {
        return openTimes;
    }

    public void setOpenTimes(String openTimes) {
        this.openTimes = openTimes;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
