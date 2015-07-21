package com.everhomes.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageOffset : 下一页码</li>
 * <li>requests : 账单和缴费记录列表</li>
 *</ul>
 *
 */

public class ListFamilyBillAndPayByFamilyIdCommandResponse {
	
	private String billDate;
	

	private Long nextPageOffset;
	
	@ItemType(PmBillsDTO.class)
	private List<PmBillsDTO> requests;

	public String getBillDate() {
		return billDate;
	}
	
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public Long getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Long nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	public List<PmBillsDTO> getRequests() {
		return requests;
	}

	public void setRequests(List<PmBillsDTO> requests) {
		this.requests = requests;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	

}
