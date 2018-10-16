package com.everhomes.rest.techpark.expansion;

import java.util.List;

import com.everhomes.util.StringHelper;

public class TransformToCustomerResponse {
	
	private List<Long> customerIds;

	public List<Long> getCustomerIds() {
		return customerIds;
	}

	public void setCustomerIds(List<Long> customerIds) {
		this.customerIds = customerIds;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
