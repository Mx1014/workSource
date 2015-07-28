package com.everhomes.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 *	<li>insertList : 待新增账单列表,详见: {@link com.everhomes.organization.pm.UpdatePmBillsDto}</li>
 *</ul>
 *
 */
public class InsertPmBillsCommand {
	
	@ItemType(UpdatePmBillsDto.class)
	private List<UpdatePmBillsDto> insertList;

	public List<UpdatePmBillsDto> getInsertList() {
		return insertList;
	}

	public void setInsertList(List<UpdatePmBillsDto> insertList) {
		this.insertList = insertList;
	}

	

}
