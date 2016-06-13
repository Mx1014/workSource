package com.everhomes.rest.techpark.expansion;

import java.util.List;

import com.everhomes.discover.ItemType;

public class EnterpriseApplyEntryResponse {
    private Long nextPageAnchor;
    
    @ItemType(EnterpriseApplyEntryDTO.class)
    private List<EnterpriseApplyEntryDTO> applyEntrys;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

	public List<EnterpriseApplyEntryDTO> getApplyEntrys() {
		return applyEntrys;
	}

	public void setApplyEntrys(List<EnterpriseApplyEntryDTO> applyEntrys) {
		this.applyEntrys = applyEntrys;
	}

}
