package com.everhomes.rest.address.admin;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>communityIds: 小区Id列表</li>
 * <li>keyword: 查询关键字</li>
 * <li>namespaceId: 域空间</li>
 * </ul>
 */
public class ListBuildingByCommunityIdsCommand {
	
	@ItemType(Long.class)
	private List<Long> communityIds;
    private String keyword;
    private Integer namespaceId;
	public List<Long> getCommunityIds() {
		return communityIds;
	}
	public void setCommunityIds(List<Long> communityIds) {
		this.communityIds = communityIds;
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
    
}
