// @formatter:off
package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizations;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *  *     <li>communityNums: 项目数量</li>
 *  *     <li>longList: 企业编号</li>
 *  *     <li>avatar: 企业logo</li>
 *  *     <li>memberCount: 人员规模</li>
 *  * </ul>
 */
public class Organization extends EhOrganizations {
	private static final long serialVersionUID = 8428338216022084922L;

    //项目数量
    private int communityNums;
    //企业编号
    private List<Long> longList;
    //企业logo
    private String avatar;
    //人员规模
    private Long memberCount;

    public int getCommunityNums() {
        return communityNums;
    }

    public void setCommunityNums(int communityNums) {
        this.communityNums = communityNums;
    }

    public List<Long> getLongList() {
        return longList;
    }

    public void setLongList(List<Long> longList) {
        this.longList = longList;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Long memberCount) {
        this.memberCount = memberCount;
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
