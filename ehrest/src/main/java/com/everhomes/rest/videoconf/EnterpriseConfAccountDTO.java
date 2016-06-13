package com.everhomes.rest.videoconf;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>enterpriseId: 企业id </li>
 *  <li>enterpriseName: 企业名称</li>
 *  <li>enterpriseDisplayName: 企业简称</li>
 *  <li>enterpriseContactor: 企业联系人</li>
 *  <li>mobile: 手机号</li>
 *  <li>useStatus: 企业使用状态 0-formally use 1-on trial 2-overdue </li>
 *  <li>status: 状态 0: inactive, 1: active, 2: locked </li>
 *  <li>totalAccount: 账号总数</li>
 *  <li>validAccount: 有效账号数</li>
 *  <li>buyChannel: 购买渠道 0-offline 1-online</li>
 * </ul>
 *
 */

public class EnterpriseConfAccountDTO {
	
	private Long id;
	
	private Long enterpriseId;
	
	private String enterpriseName;
	
	private String enterpriseDisplayName;
	
	private String enterpriseContactor;
	
	private String mobile;
	
	private Byte useStatus;
	
	private Byte status;
	
	private Integer totalAccount;
	
	private Integer validAccount;
	
	private Byte buyChannel;

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

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getEnterpriseDisplayName() {
		return enterpriseDisplayName;
	}

	public void setEnterpriseDisplayName(String enterpriseDisplayName) {
		this.enterpriseDisplayName = enterpriseDisplayName;
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

	public Byte getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(Byte useStatus) {
		this.useStatus = useStatus;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Integer getTotalAccount() {
		return totalAccount;
	}

	public void setTotalAccount(Integer totalAccount) {
		this.totalAccount = totalAccount;
	}

	public Integer getValidAccount() {
		return validAccount;
	}

	public void setValidAccount(Integer validAccount) {
		this.validAccount = validAccount;
	}

	public Byte getBuyChannel() {
		return buyChannel;
	}

	public void setBuyChannel(Byte buyChannel) {
		this.buyChannel = buyChannel;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
