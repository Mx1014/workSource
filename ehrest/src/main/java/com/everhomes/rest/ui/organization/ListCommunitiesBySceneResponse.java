// @formatter:off
package com.everhomes.rest.ui.organization;


import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>dtos: 小区信息，参考{@link com.everhomes.rest.address.CommunityDTO}</li>
 * </ul>
 *
 */
public class ListCommunitiesBySceneResponse {
	
    @ItemType(CommunityDTO.class)
    private List<CommunityDTO> dtos;
	
    
	public List<CommunityDTO> getDtos() {
		return dtos;
	}


	public void setDtos(List<CommunityDTO> dtos) {
		this.dtos = dtos;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
