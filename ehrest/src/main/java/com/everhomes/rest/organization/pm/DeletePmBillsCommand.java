package com.everhomes.rest.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 *	<li>ids :待删除账单id列表</li>
 *</ul>
 *
 */
public class DeletePmBillsCommand {
	
	@ItemType(Long.class)
	List<Long> ids;

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
	

	
}