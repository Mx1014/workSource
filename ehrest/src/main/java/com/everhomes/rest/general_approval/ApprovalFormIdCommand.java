package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>formOriginId: 原始formid</li>
 * </ul>
 * @author janson
 *
 */
public class ApprovalFormIdCommand {
	private Long formOriginId;

	public Long getFormOriginId() {
		return formOriginId;
	}

	public void setFormOriginId(Long formOriginId) {
		this.formOriginId = formOriginId;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
