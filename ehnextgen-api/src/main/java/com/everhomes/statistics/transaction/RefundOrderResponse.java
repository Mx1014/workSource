package com.everhomes.statistics.transaction;

import java.util.List;

public class RefundOrderResponse {

	private String nextAnchor;
	
	private List<BizPaidOrder> list;

	public String getNextAnchor() {
		return nextAnchor;
	}

	public void setNextAnchor(String nextAnchor) {
		this.nextAnchor = nextAnchor;
	}

	public List<BizPaidOrder> getList() {
		return list;
	}

	public void setList(List<BizPaidOrder> list) {
		this.list = list;
	}

	
}
