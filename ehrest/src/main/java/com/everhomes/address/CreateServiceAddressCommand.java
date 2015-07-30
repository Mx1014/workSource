// @formatter:off
package com.everhomes.address;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>cityId: 城市id</li>
 * <li>areaId: 区县id</li>
 * <li>address: 服务地址详情</li>
 * </ul>
 */
public class CreateServiceAddressCommand {
    
    @NotNull
    private Long cityId;
    
    @NotNull
    private Long areaId;
    
    @NotNull
    private String address;

    public CreateServiceAddressCommand() {
    }
    
    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
