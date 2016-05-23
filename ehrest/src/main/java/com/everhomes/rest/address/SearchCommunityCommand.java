// @formatter:off
package com.everhomes.rest.address;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>cityId: 城市Id</li>
 * <li>keyword: 查询关键字</li>
 * <li>communityType: 园区小区类型{@link com.everhomes.rest.community.CommunityType}</li>
 * <li>pageOffset: 页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class SearchCommunityCommand {
    private Long cityId;
    
    private Long regionId;
    
    @NotNull
    private String keyword;
    
    private Integer pageOffset;
    // start from 1, page size is configurable at server side
    private Integer pageSize;
    
    private Byte communityType;
    
    public SearchCommunityCommand() {
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
    }

    public Byte getCommunityType() {
		return communityType;
	}

	public void setCommunityType(Byte communityType) {
		this.communityType = communityType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
