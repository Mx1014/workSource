package com.everhomes.rest.openapi.shenzhou;

/**
 * Created by ying.xiong on 2017/8/12.
 */
public class ApartmentStatusDTO {
    private String apartmentIdentifier;
    private Byte livingStatus;

    public String getApartmentIdentifier() {
        return apartmentIdentifier;
    }

    public void setApartmentIdentifier(String apartmentIdentifier) {
        this.apartmentIdentifier = apartmentIdentifier;
    }

    public Byte getLivingStatus() {
        return livingStatus;
    }

    public void setLivingStatus(Byte livingStatus) {
        this.livingStatus = livingStatus;
    }
}
