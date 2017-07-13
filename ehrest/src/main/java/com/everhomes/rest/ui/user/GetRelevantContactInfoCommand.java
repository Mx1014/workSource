package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>targetId: 联系人的uuid</li>
 *</ul>
 */
public class GetRelevantContactInfoCommand {
	 
	private Long targetId;

	public GetRelevantContactInfoCommand() {
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
