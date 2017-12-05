// @formatter:off
package com.everhomes.rest.me_menu;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: dtos {@link com.everhomes.rest.me_menu.MeWebMenuDTO}</li>
 * </ul>
 */
public class ListMeWebMenusResponse {

	@ItemType(MeWebMenuDTO.class)
	private List<MeWebMenuDTO> dtos;

	public List<MeWebMenuDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<MeWebMenuDTO> dtos) {
		this.dtos = dtos;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
