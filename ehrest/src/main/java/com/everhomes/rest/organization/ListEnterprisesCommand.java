package com.everhomes.rest.organization;


/**
 * <ul>查询园区的企业
 * <li>namespaceId: 域</li>
 * <li>communityId: 小区ID</li>
 * <li>buildingId: 楼栋ID</li>
 * <li>keywords: 关键字搜索</li>
 * </ul>
 * @author sfyan
 *
 */
public class ListEnterprisesCommand {
	
	private Integer namespaceId;
	
    private Long communityId;
    
    private Long buildingId;
    
    private Long bildingName;
    
    private String keywords;
    
    private Long pageAnchor;
    
    private Integer pageSize;

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

	
	public Long getBildingName() {
		return bildingName;
	}

	public void setBildingName(Long bildingName) {
		this.bildingName = bildingName;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

    
}
