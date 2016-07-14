package com.everhomes.rest.techpark.rental.admin;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 查询资源列表
 * <li>communityId: 园区id-暂时不做</li>
 * <li>namespaceId: 域空间</li>
 * </ul>
 */
public class GetResourceTypeListCommand {

	private Integer namespaceId;
	private Long communityId;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	
}
