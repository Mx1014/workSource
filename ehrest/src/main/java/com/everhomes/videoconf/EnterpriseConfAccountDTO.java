package com.everhomes.videoconf;

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
 *  <li>status: 状态 </li>
 *  <li>totalAccount: 账号总数</li>
 *  <li>validAccount: 有效账号数</li>
 *  <li>validDate: 有效期</li>
 *  <li>accountType: 账号类型 0-single 1-multiple</li>
 *  <li>confType: 会议类型 0-25方仅视频 1-25方支持电话 2-100方仅视频 3-100方支持电话</li>
 *  <li>lockFlag: 是否被锁定  0-unlock 1-locked</li>
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
	
	private String status;
	
	private Integer totalAccount;
	
	private Integer validAccount;
	
	private Timestamp validDate;
	
	private Byte accountType;
	
	private Byte confType;
	
	private Byte lockFlag;
	
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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

	public Timestamp getValidDate() {
		return validDate;
	}

	public void setValidDate(Timestamp validDate) {
		this.validDate = validDate;
	}

	public Byte getAccountType() {
		return accountType;
	}

	public void setAccountType(Byte accountType) {
		this.accountType = accountType;
	}

	public Byte getConfType() {
		return confType;
	}

	public void setConfType(Byte confType) {
		this.confType = confType;
	}

	public Byte getLockFlag() {
		return lockFlag;
	}

	public void setLockFlag(Byte lockFlag) {
		this.lockFlag = lockFlag;
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
