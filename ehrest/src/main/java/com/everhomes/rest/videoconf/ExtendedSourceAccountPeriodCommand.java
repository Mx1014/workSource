package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>sourceAccountId: 源账号id</li>
 *  <li>validDate: 有效期</li>
 * </ul>
 */
public class ExtendedSourceAccountPeriodCommand {
	
	private Long sourceAccountId;
	
	private Long validDate;

	public Long getSourceAccountId() {
		return sourceAccountId;
	}

	public void setSourceAccountId(Long sourceAccountId) {
		this.sourceAccountId = sourceAccountId;
	}
	
	public Long getValidDate() {
		return validDate;
	}

	public void setValidDate(Long validDate) {
		this.validDate = validDate;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
