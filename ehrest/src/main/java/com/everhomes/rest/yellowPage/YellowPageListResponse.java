package com.everhomes.rest.yellowPage;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class YellowPageListResponse {
	@ItemType(YellowPageDTO.class)
	private List<YellowPageDTO> yellowPages;
	@Override
	    public String toString() {
	        return StringHelper.toJsonString(this);
	    }
	public List<YellowPageDTO> getYellowPages() {
		return yellowPages;
	}
	public void setYellowPages(List<YellowPageDTO> yellowPages) {
		this.yellowPages = yellowPages;
	}
}
