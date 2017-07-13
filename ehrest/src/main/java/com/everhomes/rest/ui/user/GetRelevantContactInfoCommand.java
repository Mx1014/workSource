package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>detailId: 联系人的档案id</li>
 *</ul>
 */
public class GetRelevantContactInfoCommand {
	 
	private Long detailId;

	public GetRelevantContactInfoCommand() {
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
