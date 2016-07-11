package com.everhomes.techpark.rental;

import com.everhomes.server.schema.tables.pojos.EhRentalRules;
import com.everhomes.util.StringHelper; 

public class 
RentalRule extends EhRentalRules {

	/**
	 * 
	 */
	private static final long serialVersionUID = -902020680950316665L;


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

    public String getContactName() {
        return RentalRuleCustomField.CONTACTNAME.getStringValue(this);
    }
    
    public void setContactName(String contactName) {
    	RentalRuleCustomField.CONTACTNAME.setStringValue(this, contactName);
    }
    
    public String getContactAddress() {
        return RentalRuleCustomField.CONTACTADDRESS.getStringValue(this);
    }
    
    public void setContactAddress(String contactMobile) {
    	RentalRuleCustomField.CONTACTADDRESS.setStringValue(this, contactMobile);
    }
    
}
