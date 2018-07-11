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
 * <li>enableDuration:门禁是否支持授权按有效期开门，1是0否</li>
 * <li>serverId:关联服务器Id</li>
 * <li>hasQr:是否支持二维码0否1是</li>
 * <li>maxDuration:有效时间最大值(天)</li>
 * <li>maxCount:按次最大次数</li>
 * <li>defualtInvalidDuration:按次授权默认有效期(天)</li>
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
    private Byte enableDuration;
    private Long serverId;
    private Byte hasQr;
    private Integer maxDuration;
    private Integer maxCount;
    private Integer defualtInvalidDuration;
    
    public Byte getEnableDuration() {
		return enableDuration;
	}
	public void setEnableDuration(Byte enableDuration) {
		this.enableDuration = enableDuration;
	}
	public Integer getMaxDuration() {
		return maxDuration;
	}
	public void setMaxDuration(Integer maxDuration) {
		this.maxDuration = maxDuration;
	}
	public Integer getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}
	public Integer getDefualtInvalidDuration() {
		return defualtInvalidDuration;
	}
	public void setDefualtInvalidDuration(Integer defualtInvalidDuration) {
		this.defualtInvalidDuration = defualtInvalidDuration;
	}
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
	public Long getServerId() {
		return serverId;
	}
	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}
	public Byte getHasQr() {
		return hasQr;
	}
	public void setHasQr(Byte hasQr) {
		this.hasQr = hasQr;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
