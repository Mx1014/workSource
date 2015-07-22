package com.everhomes.organization.pm;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;


/**
 * <ul>
 * 	<li>insertList : 待插入账单列表,详见: {@link com.everhomes.organization.pm。UpdatePmBillsDto}</li>
 *	<li>updateList : 待更新账单列表,详见: {@link com.everhomes.organization.pm。UpdatePmBillsDto}</li>
 *	<li>deleteList : 待删除账单列表,详见: {@link com.everhomes.organization.pm。UpdatePmBillsDto}</li>
 *</ul>
 *
 */
public class UpdatePmBillsCommand {
	
	@ItemType(UpdatePmBillsDto.class)
	private List<UpdatePmBillsDto> insertList;
	
	@ItemType(UpdatePmBillsDto.class)
	private List<UpdatePmBillsDto> updateList;
	
	@ItemType(UpdatePmBillsDto.class)
	private List<UpdatePmBillsDto> deleteList;

	public List<UpdatePmBillsDto> getInsertList() {
		return insertList;
	}

	public void setInsertList(List<UpdatePmBillsDto> insertList) {
		this.insertList = insertList;
	}

	public List<UpdatePmBillsDto> getUpdateList() {
		return updateList;
	}

	public void setUpdateList(List<UpdatePmBillsDto> updateList) {
		this.updateList = updateList;
	}

	public List<UpdatePmBillsDto> getDeleteList() {
		return deleteList;
	}

	public void setDeleteList(List<UpdatePmBillsDto> deleteList) {
		this.deleteList = deleteList;
	}
	
	
	
	

}
