// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: dtos 参考{@link PortalVersionUserDTO}</li>
 * </ul>
 */
public class ListPortalVersionUsersResponse {


	private List<PortalVersionUserDTO> dtos;

	public List<PortalVersionUserDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<PortalVersionUserDTO> dtos) {
		this.dtos = dtos;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
