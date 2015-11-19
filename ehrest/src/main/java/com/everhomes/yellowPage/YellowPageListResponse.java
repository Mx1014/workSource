package com.everhomes.yellowPage;

import java.util.List;

import com.everhomes.util.StringHelper;

public class YellowPageListResponse {
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
