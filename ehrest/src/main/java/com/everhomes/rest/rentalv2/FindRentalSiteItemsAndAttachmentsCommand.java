package com.everhomes.rest.rentalv2;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>rentalSiteId：场所id</li>
 * <li>rentalSiteRuleIds：预定场所规则ID列表 json字符串 </li> 
 * </ul>
 */
public class FindRentalSiteItemsAndAttachmentsCommand {
 
	private Long rentalSiteId;  
    @ItemType(Long.class)
	private List<Long> rentalSiteRuleIds;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
  
	public Long getRentalSiteId() {
		return rentalSiteId;
	}

	public void setRentalSiteId(Long rentalSiteId) {
		this.rentalSiteId = rentalSiteId;
	}

	public List<Long> getRentalSiteRuleIds() {
		return rentalSiteRuleIds;
	}

	public void setRentalSiteRuleIds(List<Long> rentalSiteRuleIds) {
		this.rentalSiteRuleIds = rentalSiteRuleIds;
	}
 

 
}
