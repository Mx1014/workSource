package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>enterpriseId: 企业id</li>
 *  <li>enterpriseContactor: 企业联系人 </li>
 *  <li>mobile: 联系人手机号</li>
 *  <li>status: 状态 0: none, 1: trial, 2: normal</li>
 *  <li>communityId: 园区id</li>
 * </ul>
 *
 */
public class UpdateEnterpriseVideoConfAccountCommand {
	
	private Long id;
	
	private Long enterpriseId;
	
	private String enterpriseContactor;
	
	private String mobile;
	
	private Byte status;
	
	private Integer namespaceId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
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

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	

}
