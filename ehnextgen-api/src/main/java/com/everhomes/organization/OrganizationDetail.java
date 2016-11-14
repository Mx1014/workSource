// @formatter:off
package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationDetails;
import com.everhomes.util.StringHelper;

public class OrganizationDetail extends EhOrganizationDetails {
	
	private static final long serialVersionUID = 8428338216022084922L;
	
	private String buildingName;
	
	private String enterpriseName;
	
	private String avatar;
	
	

	public String getBuildingName() {
		return buildingName;
	}



	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}



	public String getEnterpriseName() {
		return enterpriseName;
	}



	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}



	public String getAvatar() {
		return avatar;
	}



	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}


    public String getEmailDomain() {
        return OrganizationCustomField.EMAIL_DOMAIN.getStringValue(this);
    }
    
    public void setEmailDomain(String emailDomain) {
    	OrganizationCustomField.EMAIL_DOMAIN.setStringValue(this, emailDomain);
    }
    

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
