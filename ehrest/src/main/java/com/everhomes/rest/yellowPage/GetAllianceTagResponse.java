package com.everhomes.rest.yellowPage;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.acl.admin.AuthorizationServiceModule;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * 参数
 * <li>pageAnchor: 锚点</li>
 * <li>groups: 筛选父标签(list) {@link com.everhomes.rest.yellowPage.AllianceTagGroupDTO}</li>
 * </ul>
 */
public class GetAllianceTagResponse {

    private Long pageAnchor;
    
    @ItemType(AllianceTagGroupDTO.class)
    List<AllianceTagGroupDTO> groups;
    
    @Override
    public String toString() {
    	return StringHelper.toJsonString(this);
    }

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public List<AllianceTagGroupDTO> getGroups() {
		return groups;
	}

	public void setGroups(List<AllianceTagGroupDTO> groups) {
		this.groups = groups;
	}
	

}
