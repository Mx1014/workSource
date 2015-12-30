package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>sourceAccount: 源账号</li>
 * <li>pageOffset: 页码，从1开始</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListSourceVideoConfAccountCommand {
	
	private String sourceAccount;
	
	private Integer pageOffset;
    
    private Integer pageSize;

	public String getSourceAccount() {
		return sourceAccount;
	}

	public void setSourceAccount(String sourceAccount) {
		this.sourceAccount = sourceAccount;
	}

	public Integer getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
    
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
