package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>communityId: 用户当前所有小区ID</li>
 *  <li>namespaceId : 域</li>
 *  <li>setAdminFlag : 是否设置了管理员</li>
 *  <li>keyword: 内容关键字</li>
 *  <li>organizationType : 机构类型 ，详情{@link com.everhomes.rest.organization.OrganizationType}</li>
 *  <li>pageAnchor: 本页开始锚点</li>
 *  <li>pageSize: 每页的数量</li>
 *  <li>buildingName: 楼栋名称</li>
 * </ul>
 *
 */
public class SearchOrganizationCommand {
    private Integer namespaceId;
    private String keyword;
    
    private Long communityId;
    private Byte setAdminFlag;
    private Long pageAnchor;
    private Integer pageSize;
    
    private String organizationType;
    private String buildingName;
    
    public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public Byte getSetAdminFlag() {
		return setAdminFlag;
	}
	public void setSetAdminFlag(Byte setAdminFlag) {
		this.setAdminFlag = setAdminFlag;
	}
	public Integer getNamespaceId() {
        return namespaceId;
    }
    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
    public Long getCommunityId() {
        return communityId;
    }
    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }
    

    public String getOrganizationType() {
		return organizationType;
	}
	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
