package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>longitude: 经度</li>
 * <li>latigtue: 纬度</li>
 * <li>communityType: 园区类型，参考{@link com.everhomes.rest.community.CommunityType}</li>
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListNearbyMixCommunitiesCommand {

	private Double longitude;
    
    private Double latigtue;
    
    private Byte communityType;
    
    private Long pageAnchor;
	
	private Integer pageSize;
    
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

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
