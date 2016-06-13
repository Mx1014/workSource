package com.everhomes.rest.techpark.expansion;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListEnterpriseApplyEntryResponse {
    private Long nextPageAnchor;
    
    @ItemType(EnterpriseApplyEntryDTO.class)
    private List<EnterpriseApplyEntryDTO> entrys;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

	public List<EnterpriseApplyEntryDTO> getEntrys() {
		return entrys;
	}

	public void setEntrys(List<EnterpriseApplyEntryDTO> entrys) {
		this.entrys = entrys;
	}

    
}
