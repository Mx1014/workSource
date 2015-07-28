package com.everhomes.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;


/**
 * <ul>
 *	<li>updateList : 待更新账单列表,详见: {@link com.everhomes.organization.pm.UpdatePmBillsDto}</li>
 *</ul>
 *
 */
public class UpdatePmBillsCommand {
	
	@ItemType(UpdatePmBillsDto.class)
	private List<UpdatePmBillsDto> updateList;

	public List<UpdatePmBillsDto> getUpdateList() {
		return updateList;
	}

	public void setUpdateList(List<UpdatePmBillsDto> updateList) {
		this.updateList = updateList;
	}
}
