package com.everhomes.rest.videoconf;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>sourceAccount: 源账号 </li>
 *  <li>password: 源账号密码</li>
 *  <li>confType: 会议类型</li>
 *  <li>validDate: 有效期</li>
 *  <li>status: 状态 0-invalid 1-valid</li>
 *  <li>occupyFlag: 使用状态 0-available 1-occupied</li>
 *  <li>occupyAccountId: 用户账号</li>
 *  <li>confId: 会议id</li>
 *  <li>occupyIdentifierToken: 占用的手机号</li>
 * </ul>
 *
 */
public class SourceVideoConfAccountDTO {
	
	private Long id;
	
	private String sourceAccount;
	
	private String password;

	private String confType;
	
	private Timestamp validDate;
	
	private Byte status;
	
	private Byte occupyFlag;
	
	private Long occupyAccountId;
	
	private Long confId;
	
	private String occupyIdentifierToken;

	public String getOccupyIdentifierToken() {
		return occupyIdentifierToken;
	}

	public void setOccupyIdentifierToken(String occupyIdentifierToken) {
		this.occupyIdentifierToken = occupyIdentifierToken;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSourceAccount() {
		return sourceAccount;
	}

	public void setSourceAccount(String sourceAccount) {
		this.sourceAccount = sourceAccount;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfType() {
		return confType;
	}

	public void setConfType(String confType) {
		this.confType = confType;
	}

	public Timestamp getValidDate() {
		return validDate;
	}

	public void setValidDate(Timestamp validDate) {
		this.validDate = validDate;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Byte getOccupyFlag() {
		return occupyFlag;
	}

	public void setOccupyFlag(Byte occupyFlag) {
		this.occupyFlag = occupyFlag;
	}

	public Long getOccupyAccountId() {
		return occupyAccountId;
	}

	public void setOccupyAccountId(Long occupyAccountId) {
		this.occupyAccountId = occupyAccountId;
	}

	public Long getConfId() {
		return confId;
	}

	public void setConfId(Long confId) {
		this.confId = confId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
