// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>portalItem: 门户Item，参考{@link com.everhomes.rest.portal.PortalItemDTO}</li>
 * </ul>
 */
public class GetPortalItemByIdResponse {

	private PortalItemDTO portalItem;

	public GetPortalItemByIdResponse() {

	}

	public GetPortalItemByIdResponse(PortalItemDTO portalItem) {
		super();
		this.portalItem = portalItem;
	}

	public PortalItemDTO getPortalItem() {
		return portalItem;
	}

	public void setPortalItem(PortalItemDTO portalItem) {
		this.portalItem = portalItem;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
