package com.everhomes.techpark.expansion;

public class ListEnterpriseDetailCommand {
    private Integer pageAnchor;
    private Integer pageSize;
   
    private Long communityId;
    
    private String buildingName;
    
    public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	
	
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public Integer getPageAnchor() {
        return pageAnchor;
    }
    public void setPageAnchor(Integer pageAnchor) {
        this.pageAnchor = pageAnchor;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    
    
    
}
