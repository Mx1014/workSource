package com.everhomes.rest.openapi.jindi;

import java.util.List;

import com.everhomes.discover.ItemType;

public class JindiDataResponse {
	@ItemType(Object.class)
	private List<Object> dataList;
	private Long nextPageAnchor;
	private Byte hasMore;
	
	public List<Object> getDataList() {
		return dataList;
	}
	public void setDataList(List<Object> dataList) {
		this.dataList = dataList;
	}
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public Byte getHasMore() {
		return hasMore;
	}
	public void setHasMore(Byte hasMore) {
		this.hasMore = hasMore;
	}
}
