package com.everhomes.rest.wifi;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListWifiSettingResponse {
	private Long nextPageAnchor;
	
	@ItemType(WifiSettingDTO.class)
	private List<WifiSettingDTO> requests;
	
	
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}


	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}


	public List<WifiSettingDTO> getRequests() {
		return requests;
	}


	public void setRequests(List<WifiSettingDTO> requests) {
		this.requests = requests;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
