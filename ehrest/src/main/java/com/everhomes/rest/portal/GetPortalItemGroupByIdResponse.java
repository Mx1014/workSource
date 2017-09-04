// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>portalItemGroup: 门户Item，参考{@link com.everhomes.rest.portal.PortalItemGroupDTO}</li>
 * </ul>
 */
public class GetPortalItemGroupByIdResponse {

	private PortalItemGroupDTO portalItemGroup;

	public GetPortalItemGroupByIdResponse() {

	}

	public GetPortalItemGroupByIdResponse(PortalItemGroupDTO portalItemGroup) {
		super();
		this.portalItemGroup = portalItemGroup;
	}

	public PortalItemGroupDTO getPortalItemGroup() {
		return portalItemGroup;
	}

	public void setPortalItemGroup(PortalItemGroupDTO portalItemGroup) {
		this.portalItemGroup = portalItemGroup;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
