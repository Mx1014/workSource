package com.everhomes.rest.organization;


/**
 * <ul>查询园区的企业
 * <li>namespaceId: 域</li>
 * <li>communityId: 小区ID</li>
 * <li>buildingId: 楼栋ID</li>
 * <li>keywords: 关键字搜索</li>
 * <li>setAdminFlag: 1是0否，按是否设置了管理员来查询，add by tt, 20170522</li>
 * </ul>
 * @author sfyan
 *
 */
public class ListEnterprisesCommand {
	
	private Integer namespaceId;
	
    private Long communityId;
    
    private Long buildingId;
    
    private String buildingName;
    
    private Boolean qryAdminRoleFlag = false;
    
    private String keywords;
    
    private Byte setAdminFlag;
    
    private Long pageAnchor;
    
    private Integer pageSize;

    public Byte getSetAdminFlag() {
		return setAdminFlag;
	}

	public void setSetAdminFlag(Byte setAdminFlag) {
		this.setAdminFlag = setAdminFlag;
	}

	public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
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

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}



	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Boolean getQryAdminRoleFlag() {
		return qryAdminRoleFlag;
	}

	public void setQryAdminRoleFlag(Boolean qryAdminRoleFlag) {
		this.qryAdminRoleFlag = qryAdminRoleFlag;
	}

    
}
