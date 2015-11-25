package com.everhomes.techpark.expansion;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListEnterpriseApplyEntryResponse {
    private Integer nextPageAnchor;
    
    @ItemType(EnterpriseApplyEntryDTO.class)
    private List<EnterpriseApplyEntryDTO> entrys;

    public Integer getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Integer nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

	public List<EnterpriseApplyEntryDTO> getEntrys() {
		return entrys;
	}

	public void setEntrys(List<EnterpriseApplyEntryDTO> entrys) {
		this.entrys = entrys;
	}

    
}
