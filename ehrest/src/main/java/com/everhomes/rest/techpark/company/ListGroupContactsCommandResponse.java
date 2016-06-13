package com.everhomes.rest.techpark.company;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageOffset：下一页的页码（如果没有则为空）</li>
 * <li>list：通讯录信息，参考{@link com.everhomes.rest.techpark.company.GroupContactDTO}</li>
 * </ul>
 */
public class ListGroupContactsCommandResponse {
	private Integer nextPageOffset;
	
	@ItemType(GroupContactDTO.class)
	private List<GroupContactDTO> list;
	public Integer getNextPageOffset() {
		return nextPageOffset;
	}
	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}
	public List<GroupContactDTO> getList() {
		return list;
	}
	public void setList(List<GroupContactDTO> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	

}
