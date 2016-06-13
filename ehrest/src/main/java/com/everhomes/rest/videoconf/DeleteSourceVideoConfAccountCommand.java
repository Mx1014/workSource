package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * 
 * sourceAccountId: 源账号主键id
 *
 */
public class DeleteSourceVideoConfAccountCommand {
	
	private Long sourceAccountId;

	public Long getSourceAccountId() {
		return sourceAccountId;
	}

	public void setSourceAccountId(Long sourceAccountId) {
		this.sourceAccountId = sourceAccountId;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
