package com.everhomes.rest.business;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>nextPageOffset : 下一页</li>
 * 	<li>list : business 列表</li>
 * </ul>
 *
 */
public class ListBusinessByKeywordCommandResponse {
	private Integer nextPageOffset;
	
	@ItemType(BusinessDTO.class)
	private List<BusinessDTO> list;
	
	public Integer getNextPageOffset() {
		return nextPageOffset;
	}
	
	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}
	
	public List<BusinessDTO> getList() {
		return list;
	}
	
	public void setList(List<BusinessDTO> list) {
		this.list = list;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	

}
