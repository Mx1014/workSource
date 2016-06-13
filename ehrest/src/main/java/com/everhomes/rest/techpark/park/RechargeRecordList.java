package com.everhomes.rest.techpark.park;

import java.util.List;

import com.everhomes.discover.ItemType;

public class RechargeRecordList {
	
	@ItemType(value = RechargeRecordDTO.class)
	private List<RechargeRecordDTO> rechargeRecord;

	private Long nextPageAnchor;

	public List<RechargeRecordDTO> getRechargeRecord() {
		return rechargeRecord;
	}

	public void setRechargeRecord(List<RechargeRecordDTO> rechargeRecord) {
		this.rechargeRecord = rechargeRecord;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	
}
