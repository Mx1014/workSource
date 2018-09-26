package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>province: 省份名</li>
 * <li>activeDoorNumber：已激活门禁数</li>
 * </ul>
 */
public class ActiveDoorByPlaceDTO {
    private String province;
    private Long activeDoorNumber;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Long getActiveDoorNumber() {
        return activeDoorNumber;
    }

    public void setActiveDoorNumber(Long activeDoorNumber) {
        this.activeDoorNumber = activeDoorNumber;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
