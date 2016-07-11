package com.everhomes.yellowPage;
 

public class ServiceAlliance extends YellowPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7415272177411093300L;

    public String getContactName() {
        return ServiceAllianceCustomField.CONTACTNAME.getStringValue(this);
    }
    
    public void setContactName(String contactName) {
    	ServiceAllianceCustomField.CONTACTNAME.setStringValue(this, contactName);
    }
    
    public String getContactMobile() {
        return ServiceAllianceCustomField.CONTACTMOBILE.getStringValue(this);
    }
    
    public void setContactMobile(String contactMobile) {
    	ServiceAllianceCustomField.CONTACTMOBILE.setStringValue(this, contactMobile);
    }
    
    public String getServiceType() {
        return ServiceAllianceCustomField.SERVICETYPE.getStringValue(this);
    }
    
    public void setServiceType(String serviceType) {
    	ServiceAllianceCustomField.SERVICETYPE.setStringValue(this, serviceType);
    }
    
}
