// @formatter:off
package com.everhomes.rest.portal;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>serviceModuleApps: 模块应用列表，参考{@link com.everhomes.rest.portal.AssetServiceModuleAppDTO}</li>
 * </ul>
 */
public class ListAssetServiceModuleAppsResponse {

	@ItemType(AssetServiceModuleAppDTO.class)
	private List<AssetServiceModuleAppDTO> serviceModuleApps;

	public ListAssetServiceModuleAppsResponse() {

	}

	public ListAssetServiceModuleAppsResponse(List<AssetServiceModuleAppDTO> serviceModuleApps) {
		super();
		this.serviceModuleApps = serviceModuleApps;
	}

	public List<AssetServiceModuleAppDTO> getServiceModuleApps() {
		return serviceModuleApps;
	}

	public void setServiceModuleApps(List<AssetServiceModuleAppDTO> serviceModuleApps) {
		this.serviceModuleApps = serviceModuleApps;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
