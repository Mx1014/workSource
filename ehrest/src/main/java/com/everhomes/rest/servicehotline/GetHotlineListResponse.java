package com.everhomes.rest.servicehotline;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul> 
 *  
 * <li>hotlines:热线列表{@link HotlineDTO}</li>   
 * </ul>
 */
public class GetHotlineListResponse {
	@ItemType(HotlineDTO.class)
	private List<HotlineDTO> hotlines;

	public List<HotlineDTO> getHotlines() {
		return hotlines;
	}

	public void setHotlines(List<HotlineDTO> hotlines) {
		this.hotlines = hotlines;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    } 
	
}
