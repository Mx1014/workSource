// @formatter:off
package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizations;
import com.everhomes.util.StringHelper;

public class Organization extends EhOrganizations {
	private static final long serialVersionUID = 8428338216022084922L;
	
	public Organization() {
    }

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getEmailDomain() {
        return OrganizationCustomField.EMAIL_DOMAIN.getStringValue(this);
    }
    
    public void setEmailDomain(String emailDomain) {
    	OrganizationCustomField.EMAIL_DOMAIN.setStringValue(this, emailDomain);
    }
    
}
