package com.everhomes.rest.techpark.expansion;

import com.everhomes.util.StringHelper;

public class ListEnterpriseDetailCommand {
    private Long pageAnchor;
    private Integer pageSize;
   
    private Long communityId;
    
    private String buildingName;

    // 客户端暂时不做分页
    private  Byte allFlag;
    
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

    public Byte getAllFlag() {
        return allFlag;
    }

    public void setAllFlag(Byte allFlag) {
        this.allFlag = allFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
