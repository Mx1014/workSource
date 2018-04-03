// @formatter:off
package com.everhomes.rest.portal;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>portalLayouts: 门户layout列表，参考{@link com.everhomes.rest.portal.PortalLayoutDTO}</li>
 * </ul>
 */
public class ListPortalLayoutsResponse {

	@ItemType(PortalLayoutDTO.class)
	private List<PortalLayoutDTO> portalLayouts;

	public ListPortalLayoutsResponse() {

	}

	public ListPortalLayoutsResponse(List<PortalLayoutDTO> portalLayouts) {
		super();
		this.portalLayouts = portalLayouts;
	}

	public List<PortalLayoutDTO> getPortalLayouts() {
		return portalLayouts;
	}

	public void setPortalLayouts(List<PortalLayoutDTO> portalLayouts) {
		this.portalLayouts = portalLayouts;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
