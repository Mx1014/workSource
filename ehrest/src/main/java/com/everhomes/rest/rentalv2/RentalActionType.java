package com.everhomes.rest.rentalv2;

/**
 * Created by Administrator on 2017/11/29.
 */
public class RentalActionType {
    private Long resourceTypeId;
    private Byte pageType;
    private Byte payMode;

    public Long getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(Long resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public Byte getPageType() {
        return pageType;
    }

    public void setPageType(Byte pageType) {
        this.pageType = pageType;
    }

    public Byte getPayMode() {
        return payMode;
    }

    public void setPayMode(Byte payMode) {
        this.payMode = payMode;
    }
}
