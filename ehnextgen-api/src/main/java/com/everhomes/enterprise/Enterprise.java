package com.everhomes.enterprise;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.address.Address;
import com.everhomes.group.Group;
import com.everhomes.group.GroupCustomField;
import com.everhomes.util.StringHelper;

public class Enterprise extends Group {


    private static final long serialVersionUID = -6007540996674505596L;

    private List<EnterpriseAttachment> attachments = new ArrayList<EnterpriseAttachment>();
    
    private String contactor;
    
    private String entries; 
    
    private Long communityId;
    
    public String getContactor() {
		return contactor;
	}

	public void setContactor(String contactor) {
		this.contactor = contactor;
	}

	public String getEntries() {
		return entries;
	}

	public void setEntries(String entries) {
		this.entries = entries;
	}
	
	public String getEnterpriseCheckinDate() {
		return GroupCustomField.ENTERPRISE_CHECKIN_DATE.getStringValue(this);
	}

	public void setEnterpriseCheckinDate(String checkinDate) {
		GroupCustomField.ENTERPRISE_CHECKIN_DATE.setStringValue(this, checkinDate);
	}
	
	public String getEnterpriseAddress() {
		return GroupCustomField.ENTERPRISE_ADDRESS_SEQUENCE.getStringValue(this);
	}

	public void setEnterpriseAddress(String address) {
		GroupCustomField.ENTERPRISE_ADDRESS_SEQUENCE.setStringValue(this, address);
	}

	public String getContactsPhone() {
    	return GroupCustomField.ENTERPRISE_CONTACTS_PHONE.getStringValue(this);
    }
    
    public void setContactsPhone(String phone) {
    	GroupCustomField.ENTERPRISE_CONTACTS_PHONE.setStringValue(this, phone);
    }
    public String getPostUri() {
    	return GroupCustomField.ENTERPRISE_POSTURI.getStringValue(this);
    }
    
    public void setPostUri(String postUri) {
    	GroupCustomField.ENTERPRISE_POSTURI.setStringValue(this, postUri);
    }
    
    private List<Address> address = new ArrayList<Address>();
    public List<EnterpriseAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<EnterpriseAttachment> attachments) {
		this.attachments = attachments;
	}

	public List<Address> getAddress() {
		return address;
	}

	public void setAddress(List<Address> address) {
		this.address = address;
	}

	
	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
