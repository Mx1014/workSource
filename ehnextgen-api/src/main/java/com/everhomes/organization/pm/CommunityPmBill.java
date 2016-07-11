// @formatter:off
package com.everhomes.organization.pm;

import java.util.List;

import com.everhomes.server.schema.tables.pojos.EhOrganizationBills;
import com.everhomes.util.StringHelper;

public class CommunityPmBill extends EhOrganizationBills {
	private static final long serialVersionUID = -1845997496710353657L;

	private List<CommunityPmBillItem> itemList;
	
	
	public List<CommunityPmBillItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<CommunityPmBillItem> itemList) {
		this.itemList = itemList;
	}

	public CommunityPmBill() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
