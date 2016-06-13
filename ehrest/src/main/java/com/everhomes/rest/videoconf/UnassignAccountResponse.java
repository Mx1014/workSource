package com.everhomes.rest.videoconf;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>accountIds: 未分配账号的id list</li>
 * <li>accountsCount: 订单账号总数</li>
 * <li>unassignAccountsCount: 订单未分配账号数</li>
 * <li>expiredDate: 有效期</li>
 * <li>confType: 会议类型 0-25方仅视频 1-25方支持电话 2-100方仅视频 3-100方支持电话</li>
 * </ul>
 */
public class UnassignAccountResponse {
	@ItemType(Long.class)
	private List<Long> accountIds;
	
	private Integer accountsCount;
	
	private Integer unassignAccountsCount;
	
	private Timestamp expiredDate;
	
	private Byte confType;

	public List<Long> getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(List<Long> accountIds) {
		this.accountIds = accountIds;
	}

	public Integer getAccountsCount() {
		return accountsCount;
	}

	public void setAccountsCount(Integer accountsCount) {
		this.accountsCount = accountsCount;
	}

	public Integer getUnassignAccountsCount() {
		return unassignAccountsCount;
	}

	public void setUnassignAccountsCount(Integer unassignAccountsCount) {
		this.unassignAccountsCount = unassignAccountsCount;
	}

	public Timestamp getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Timestamp expiredDate) {
		this.expiredDate = expiredDate;
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
