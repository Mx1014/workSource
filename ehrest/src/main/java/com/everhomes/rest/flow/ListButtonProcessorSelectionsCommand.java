package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul><li>
 * FlowUserType: 用户的选择类型：{@link com.everhomes.rest.flow.FlowUserType}
 * </li></ul>
 * @author janson
 *
 */
public class ListButtonProcessorSelectionsCommand {
	private Long buttonId;
	private String flowUserType;

	public Long getButtonId() {
		return buttonId;
	}

	public void setButtonId(Long buttonId) {
		this.buttonId = buttonId;
	}

	public String getFlowUserType() {
		return flowUserType;
	}

	public void setFlowUserType(String flowUserType) {
		this.flowUserType = flowUserType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
