package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class ListButtonProcessorSelectionsCommand {
	private Long buttonId;

	public Long getButtonId() {
		return buttonId;
	}

	public void setButtonId(Long buttonId) {
		this.buttonId = buttonId;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
