package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>enterpriseId: 企业id</li>
 *  <li>enterpriseName: 企业名称</li>
 *  <li>enterpriseContactor: 企业联系人</li>
 *  <li>mobile: 联系人手机号</li>
 * </ul>
 *
 */
public class EnterpriseWithoutVideoConfAccountDTO {

	private Long enterpriseId;
	
	private String enterpriseName;
	
	private String enterpriseContactor;
	
	private String mobile;

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	
	public String getEnterpriseContactor() {
		return enterpriseContactor;
	}

	public void setEnterpriseContactor(String enterpriseContactor) {
		this.enterpriseContactor = enterpriseContactor;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
