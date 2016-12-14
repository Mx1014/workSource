package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class DeleteFlowUserSelectionCommand {
	private Long userSelectionId;

	public Long getUserSelectionId() {
		return userSelectionId;
	}

	public void setUserSelectionId(Long userSelectionId) {
		this.userSelectionId = userSelectionId;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
