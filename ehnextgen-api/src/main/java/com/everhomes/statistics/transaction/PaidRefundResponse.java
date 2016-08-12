package com.everhomes.statistics.transaction;

import java.util.List;

public class PaidRefundResponse {

	private String nextAnchor;
	
	private List<PaidRefund> list;

	public String getNextAnchor() {
		return nextAnchor;
	}

	public void setNextAnchor(String nextAnchor) {
		this.nextAnchor = nextAnchor;
	}

	public List<PaidRefund> getList() {
		return list;
	}

	public void setList(List<PaidRefund> list) {
		this.list = list;
	}

	
}
