// @formatter:off
package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>cityId: 城市Id</li>
 * <li>longitude: 经度</li>
 * <li>communityType: 类型 communityType: 园区小区类型{@link com.everhomes.rest.community.CommunityType}</li>
 * <li>latigtue: 纬度</li>
 * <li>pageOffset: 页码</li>
 * </ul>
 */
public class ListNearbyCommunityCommand {
    private Long cityId;
    private Double longitude;
    private Double latigtue;
    
    
    // start from 1, page size is configurable at server side
    private Long pageOffset;
    
    private Byte communityType;
    
    public ListNearbyCommunityCommand() {
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatigtue() {
        return latigtue;
    }

    public void setLatigtue(Double latigtue) {
        this.latigtue = latigtue;
    }
    
    public Byte getCommunityType() {
		return communityType;
	}

	public void setCommunityType(Byte communityType) {
		this.communityType = communityType;
	}

	public void setPageOffset(Long pageOffset) {
		this.pageOffset = pageOffset;
	}

	public Long getPageOffset() {
		return pageOffset;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
