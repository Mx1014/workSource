package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>accountId: 账号id</li>
 *  <li>validDate: 有效期</li>
 *  <li>confType: 会议类型 0-25方仅视频; 1-25方支持电话; 2-100方仅视频; 3-100方支持电话</li>
 * </ul>
 *
 */
public class UpdateVideoConfAccountCommand {
	
	private Long accountId;
	
	private Long validDate;
	
	private Byte confType;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getValidDate() {
		return validDate;
	}

	public void setValidDate(Long validDate) {
		this.validDate = validDate;
	}
	
	public Byte getConfType() {
		return confType;
	}

	public void setConfType(Byte confType) {
		this.confType = confType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
