// @formatter:off
package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizations;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *  *     <li>memberCount: 人员规模</li>
 *  * </ul>
 */
public class Organization extends EhOrganizations {
	private static final long serialVersionUID = 8428338216022084922L;

    //人员规模
    private String memberRange;

    private String avatarUri;

    private Integer projectsCount;

    public String getMemberRange() {
        return memberRange;
    }

    public void setMemberRange(String memberRange) {
        this.memberRange = memberRange;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }

    public Integer getProjectsCount() {
        return projectsCount;
    }

    public void setProjectsCount(Integer projectsCount) {
        this.projectsCount = projectsCount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
	public Organization() {
    }

    public String getEmailDomain() {
        return OrganizationCustomField.EMAIL_DOMAIN.getStringValue(this);
    }
    
    public void setEmailDomain(String emailDomain) {
    	OrganizationCustomField.EMAIL_DOMAIN.setStringValue(this, emailDomain);
    }
}
