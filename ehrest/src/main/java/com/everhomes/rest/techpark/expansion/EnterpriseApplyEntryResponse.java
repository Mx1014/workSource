package com.everhomes.rest.techpark.expansion;

import java.util.List;

import com.everhomes.discover.ItemType;
/**
 * <ul>
 * <li>nextPageAnchor：下页锚点</li> 
 * <li>applyEntrys：  参考{@link com.everhomes.rest.techpark.expansion.EnterpriseApplyEntryDTO}}</li> 
 * </ul>
 */
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
