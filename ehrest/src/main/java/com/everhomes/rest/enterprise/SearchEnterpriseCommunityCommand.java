package com.everhomes.rest.enterprise;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>keyword: 查询关键字</li>
 * <li>communityType: 园区小区类型{@link com.everhomes.rest.community.CommunityType}</li>
 * <li>pageOffset: 页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class SearchEnterpriseCommunityCommand {   
    private Long regionId;
    
    private Byte communityType;
    
    @NotNull
    private String keyword;
    
    private Integer pageOffset;
    // start from 1, page size is configurable at server side
    private Integer pageSize;
    public Long getRegionId() {
        return regionId;
    }
    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }
    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public Integer getPageOffset() {
        return pageOffset;
    }
    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
	public Byte getCommunityType() {
		return communityType;
	}
	public void setCommunityType(Byte communityType) {
		this.communityType = communityType;
	}
    
    
}
