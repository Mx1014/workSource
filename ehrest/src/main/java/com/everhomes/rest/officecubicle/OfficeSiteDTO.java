package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>id: siteid</li>
 * <li>spaceId: 空间id</li>
 * <li>siteType: 场所类型0开放式工位 1工位数办公室 2面积办公室 {@link com.everhomes.rest.officecubicle.OfficeSiteType}</li>
 * <li>size: 场所大小 - 对于工位是个数，对于面积是平米</li> 
 * </ul>
 */
public class OfficeSiteDTO {
	private Long id;
	private Long spaceId;
	private Byte siteType;
	private String size;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}

	public Byte getSiteType() {
		return siteType;
	}

	public void setSiteType(Byte siteType) {
		this.siteType = siteType;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
}
