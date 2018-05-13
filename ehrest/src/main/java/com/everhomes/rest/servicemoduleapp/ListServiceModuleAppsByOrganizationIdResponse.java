// @formatter:off
package com.everhomes.rest.servicemoduleapp;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * 
 * <ul>返回值:
 * <li>serviceModuleApps: 模块应用列表，参考{@link ServiceModuleAppDTO}</li>
 * </ul>
 */
public class ListServiceModuleAppsByOrganizationIdResponse {

	@ItemType(ServiceModuleAppDTO.class)
	private List<ServiceModuleAppDTO> serviceModuleApps;
	private Long nextPageAnchor;

	public List<ServiceModuleAppDTO> getServiceModuleApps() {
		return serviceModuleApps;
	}

	public void setServiceModuleApps(List<ServiceModuleAppDTO> serviceModuleApps) {
		this.serviceModuleApps = serviceModuleApps;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
