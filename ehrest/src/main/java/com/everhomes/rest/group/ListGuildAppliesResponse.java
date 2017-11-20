// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: dtos {@link com.everhomes.rest.group.GuildApplyDTO}</li>
 * </ul>
 */
public class ListGuildAppliesResponse {

	@ItemType(GuildApplyDTO.class)
	private List<GuildApplyDTO> dtos;

	public List<GuildApplyDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<GuildApplyDTO> dtos) {
		this.dtos = dtos;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
