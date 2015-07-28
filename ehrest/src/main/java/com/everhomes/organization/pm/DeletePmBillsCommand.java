package com.everhomes.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
/**
 * <ul>
 *	<li>deleteList :待删除账单id列表</li>
 *</ul>
 *
 */
public class DeletePmBillsCommand {
	
	@ItemType(Long.class)
	List<Long> deleteList;

	public List<Long> getDeleteList() {
		return deleteList;
	}

	public void setDeleteList(List<Long> deleteList) {
		this.deleteList = deleteList;
	}
	
	

}
