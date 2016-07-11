package com.everhomes.yellowPage;
 

public class MakerZone extends YellowPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7415272177411093300L;

    public String getContactName() {
        return MakerZoneCustomField.CONTACTNAME.getStringValue(this);
    }
    
    public void setContactName(String contactName) {
    	MakerZoneCustomField.CONTACTNAME.setStringValue(this, contactName);
    }
    
    public String getContactMobile() {
        return MakerZoneCustomField.CONTACTMOBILE.getStringValue(this);
    }
    
    public void setContactMobile(String contactMobile) {
    	MakerZoneCustomField.CONTACTMOBILE.setStringValue(this, contactMobile);
    }
    
    
    
}
