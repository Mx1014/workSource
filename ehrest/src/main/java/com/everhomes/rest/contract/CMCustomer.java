package com.everhomes.rest.contract;

public class CMCustomer {

    private String AccountName;

    private String Connector;

    private String ConnectorPhone;

    private String Mail;

    private String AccountId;

    private Long communityId;
    private Long customerId;
    
    private String PropertyID;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getAccountId() {
        return AccountId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getConnector() {
        return Connector;
    }

    public void setConnector(String connector) {
        Connector = connector;
    }

    public String getConnectorPhone() {
        return ConnectorPhone;
    }

    public void setConnectorPhone(String connectorPhone) {
        ConnectorPhone = connectorPhone;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

	public String getPropertyID() {
		return PropertyID;
	}

	public void setPropertyID(String propertyID) {
		PropertyID = propertyID;
	}
}
