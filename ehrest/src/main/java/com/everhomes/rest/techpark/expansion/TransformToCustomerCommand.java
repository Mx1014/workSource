package com.everhomes.rest.techpark.expansion;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class TransformToCustomerCommand {
	
	@ItemType(IntentionCustomerDTO.class)
	private List<IntentionCustomerDTO> intentionCustomers;

	public List<IntentionCustomerDTO> getIntentionCustomers() {
		return intentionCustomers;
	}

	public void setIntentionCustomers(List<IntentionCustomerDTO> intentionCustomers) {
		this.intentionCustomers = intentionCustomers;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
