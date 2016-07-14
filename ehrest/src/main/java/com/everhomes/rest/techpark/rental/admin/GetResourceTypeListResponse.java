package com.everhomes.rest.techpark.rental.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.community.BuildingDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出资源列表返回值(根据图标和园区)
 * <li>nextPageAnchor: 分页，下一页锚点</li>
 * <li>resourceTypes: 资源类型列表</li> 
 * </ul>
 */
public class GetResourceTypeListResponse {
private Long nextPageAnchor;
	
	@ItemType(ResourceTypeDTO.class)
	private List<ResourceTypeDTO> resourceTypes;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ResourceTypeDTO> getResourceTypes() {
		return resourceTypes;
	}

	public void setResourceTypes(List<ResourceTypeDTO> resourceTypes) {
		this.resourceTypes = resourceTypes;
	} 

}
