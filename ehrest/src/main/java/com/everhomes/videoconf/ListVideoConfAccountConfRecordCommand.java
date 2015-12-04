package com.everhomes.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>accountId: 视频账号id</li>
 * <li>pageAnchor: 本页开始的锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListVideoConfAccountConfRecordCommand {
	
	private Long accountId;
	
	private Long pageAnchor;
	
    private Integer pageSize;
	

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
