package com.everhomes.rest.organization.pm;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *	<li>insertList : 待新增账单列表,详见: {@link com.everhomes.rest.organization.pm.UpdatePmBillsDto}</li>
 *</ul>
 *
 */
public class InsertPmBillsCommand {
	
	@NotNull
	@ItemType(UpdatePmBillsDto.class)
	private List<UpdatePmBillsDto> insertList;

	public List<UpdatePmBillsDto> getInsertList() {
		return insertList;
	}

	public void setInsertList(List<UpdatePmBillsDto> insertList) {
		this.insertList = insertList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	

	

}
