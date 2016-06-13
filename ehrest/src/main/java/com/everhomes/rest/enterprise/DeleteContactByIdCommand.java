package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;

public class DeleteContactByIdCommand {

    private java.lang.Long     enterpriseId;
    private java.lang.Long   contactId;   
    

	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    } 
	
	 
	public java.lang.Long getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(java.lang.Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}


	public java.lang.Long getContactId() {
		return contactId;
	}


	public void setContactId(java.lang.Long contactId) {
		this.contactId = contactId;
	}
 
}
