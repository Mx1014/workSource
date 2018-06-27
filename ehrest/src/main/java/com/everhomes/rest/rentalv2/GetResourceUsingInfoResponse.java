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
    private List<RentalOpenTimeDTO> openTimes;

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

    public List<RentalOpenTimeDTO> getOpenTimes() {
        return openTimes;
    }

    public void setOpenTimes(List<RentalOpenTimeDTO> openTimes) {
        this.openTimes = openTimes;
    }
}
