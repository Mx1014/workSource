// @formatter:off
package com.everhomes.rest.portal;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>serviceModuleApps: 模块应用列表，参考{@link com.everhomes.rest.portal.ServiceModuleAppDTO}</li>
 * </ul>
 */
public class ListServiceModuleAppsResponse {

	@ItemType(ServiceModuleAppDTO.class)
	private List<ServiceModuleAppDTO> serviceModuleApps;

	public ListServiceModuleAppsResponse() {

	}

	public ListServiceModuleAppsResponse(List<ServiceModuleAppDTO> serviceModuleApps) {
		super();
		this.serviceModuleApps = serviceModuleApps;
	}

	public List<ServiceModuleAppDTO> getServiceModuleApps() {
		return serviceModuleApps;
	}

	public void setServiceModuleApps(List<ServiceModuleAppDTO> serviceModuleApps) {
		this.serviceModuleApps = serviceModuleApps;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
