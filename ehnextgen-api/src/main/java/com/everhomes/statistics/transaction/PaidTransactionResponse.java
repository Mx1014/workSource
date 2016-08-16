package com.everhomes.statistics.transaction;

import java.util.List;

public class PaidTransactionResponse {

	private String nextAnchor;
	
	private List<PaidTransaction> list;

	public String getNextAnchor() {
		return nextAnchor;
	}

	public void setNextAnchor(String nextAnchor) {
		this.nextAnchor = nextAnchor;
	}

	public List<PaidTransaction> getList() {
		return list;
	}

	public void setList(List<PaidTransaction> list) {
		this.list = list;
	}

	
}
