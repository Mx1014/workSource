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
    
    public String getServiceUrl() {
        return ServiceAllianceCustomField.SERVICEURL.getStringValue(this);
    }
    
    public void setServiceUrl(String serviceUrl) {
    	ServiceAllianceCustomField.SERVICEURL.setStringValue(this, serviceUrl);
    }
    
    public Long getDiscount() {
        return ServiceAllianceCustomField.DISCOUNT.getIntegralValue(this);
    }
    
    public void setDiscount(Long discount) {
    	ServiceAllianceCustomField.DISCOUNT.setIntegralValue(this, discount);
    }
    
    public Long getCategoryId() {
    	return ServiceAllianceCustomField.CATEGORYID.getIntegralValue(this);
    }
    
    public void setCategoryId(Long categoryId) {
    	ServiceAllianceCustomField.CATEGORYID.setIntegralValue(this, categoryId);
    }
    
    public String getDiscountDesc() {
        return ServiceAllianceCustomField.DISCOUNTDESC.getStringValue(this);
    }
    
    public void setDiscountDesc(String discountDesc) {
    	ServiceAllianceCustomField.DISCOUNTDESC.setStringValue(this, discountDesc);
    }
}
