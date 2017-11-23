// @formatter:off
package com.everhomes.rest.portal;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>portalItemGroups: 门户ItemGroup列表，参考{@link com.everhomes.rest.portal.PortalItemGroupDTO}</li>
 * </ul>
 */
public class ListPortalItemGroupsResponse {

	@ItemType(PortalItemGroupDTO.class)
	private List<PortalItemGroupDTO> portalItemGroups;

	public ListPortalItemGroupsResponse() {

	}

	public ListPortalItemGroupsResponse(List<PortalItemGroupDTO> portalItemGroups) {
		super();
		this.portalItemGroups = portalItemGroups;
	}

	public List<PortalItemGroupDTO> getPortalItemGroups() {
		return portalItemGroups;
	}

	public void setPortalItemGroups(List<PortalItemGroupDTO> portalItemGroups) {
		this.portalItemGroups = portalItemGroups;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
