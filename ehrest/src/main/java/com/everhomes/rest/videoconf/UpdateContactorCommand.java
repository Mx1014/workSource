package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>contactorName: 企业联系人姓名</li>
 *  <li>contactor: 手机号</li>
 *  <li>enterpriseId: 企业id</li>
 * </ul>
 *
 */
public class UpdateContactorCommand {

	private String contactorName;
	
	private String contactor;
	
	private Long enterpriseId;

	public String getContactorName() {
		return contactorName;
	}

	public void setContactorName(String contactorName) {
		this.contactorName = contactorName;
	}

	public String getContactor() {
		return contactor;
	}

	public void setContactor(String contactor) {
		this.contactor = contactor;
	}
	
	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
