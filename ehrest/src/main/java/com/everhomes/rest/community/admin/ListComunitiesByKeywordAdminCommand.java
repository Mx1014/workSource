package com.everhomes.rest.community.admin;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * 
 * <p>
 * <ul>
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * <li>keyword: 小区关键字</li>
 * <li>namespaceId: 域空间id</li>
 * <li>communityType: 园区类型 0-小区、1-园区，参考{@link com.everhomes.rest.community.CommunityType}</li>
 * </ul>
 *
 */
public class ListComunitiesByKeywordAdminCommand {
	
	private Long pageAnchor;
	
	private Integer pageSize;
	
	private String keyword;
	
	private Integer namespaceId;

	private Byte communityType;
	
	
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
	
	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	
	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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
