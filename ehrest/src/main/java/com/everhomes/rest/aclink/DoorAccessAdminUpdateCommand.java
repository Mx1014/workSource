// @formatter:off
package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>address: 地址</li>
 * <li>description: 描述</li>
 * <li>displayName: 展示名称</li>
 * <li>name: 名称</li>
 * <li>longitude: 经度</li>
 * <li>latitude: 纬度</li>
 * <li>enableAmount:门禁是否支持授权按次开门，1是0否</li>
 * </ul>
 */
public class DoorAccessAdminUpdateCommand {
    @NotNull
    private Long     id;
    
    private String address;
    private String description;
    private String displayName;
    private String name;
    private Double longitude;
    private Double latitude;
    private Byte enableAmount;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
	public Byte getEnableAmount() {
		return enableAmount;
	}
	public void setEnableAmount(Byte enableAmount) {
		this.enableAmount = enableAmount;
	}
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
