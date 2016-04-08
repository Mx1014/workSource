package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>accountId: 视频账号id</li>
 *  <li>status: 状态 0-失效 1-有效 2-被锁定 </li>
 *  <li>occupyFlag: 使用状态 0-空闲 1-占用 </li>
 *  <li>confId: 账号正在开会时的会议id </li>
 *  <li>purchaseAuthority: 是否具有购买权限 </li>
 * </ul>
 *
 */
public class UserAccountDTO {
	
	private Long accountId;
	
	private Byte status;
	
	private Byte occupyFlag;
	
	private Long confId;
	
	private boolean purchaseAuthority;

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
	
	public Long getConfId() {
		return confId;
	}

	public void setConfId(Long confId) {
		this.confId = confId;
	}

	public boolean isPurchaseAuthority() {
		return purchaseAuthority;
	}

	public void setPurchaseAuthority(boolean purchaseAuthority) {
		this.purchaseAuthority = purchaseAuthority;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
