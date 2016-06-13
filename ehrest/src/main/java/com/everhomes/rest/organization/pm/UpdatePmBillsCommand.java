package com.everhomes.rest.organization.pm;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;


/**
 * <ul>
 *	<li>updateList : 待更新账单列表,详见: {@link com.everhomes.rest.organization.pm.UpdatePmBillsDto}</li>
 *</ul>
 *
 */
public class UpdatePmBillsCommand {
	
	@NotNull
	@ItemType(UpdatePmBillsDto.class)
	private List<UpdatePmBillsDto> updateList;

	public List<UpdatePmBillsDto> getUpdateList() {
		return updateList;
	}

	public void setUpdateList(List<UpdatePmBillsDto> updateList) {
		this.updateList = updateList;
	}
}
