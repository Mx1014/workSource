// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: dtos {@link com.everhomes.rest.portal.PortalVersionDTO}</li>
 * </ul>
 */
public class ListPortalVersionResponse {

	@ItemType(PortalVersionDTO.class)
	private List<PortalVersionDTO> dtos;

	public List<PortalVersionDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<PortalVersionDTO> dtos) {
		this.dtos = dtos;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
