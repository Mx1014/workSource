package com.everhomes.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>accountId: 视频账号id</li>
 *  <li>status: 状态 0-失效 1-有效 2-被锁定 </li>
 *  <li>occupyFlag: 使用状态 0-空闲 1-占用 </li>
 * </ul>
 *
 */
public class UserAccountDTO {
	
	private Long accountId;
	
	private Byte status;
	
	private Byte occupyFlag;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
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
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
