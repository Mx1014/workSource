package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul> 创建用户选择
 * <li>belongTo: 属于的上级对象ID flow, flow_node, flow_action, flow_button, flow_selection, flow_user 的ID</li>
 * <li>flowEntityType: {@link com.everhomes.rest.flow.FlowEntityType} </li>
 * <li>flowUserType: supervisor 或者 processor</li>
 * <li>selections: 用户选择的信息列表{@link com.everhomes.rest.flow.FlowSingleUserSelectionCommand} </li>
 * </ul>
 * @author janson
 *
 */
public class CreateFlowUserSelectionCommand {
	private Long belongTo;
	private String flowUserType;
	private String flowEntityType;
	
	@ItemType(FlowSingleUserSelectionCommand.class)
	List<FlowSingleUserSelectionCommand> selections;
	
	public CreateFlowUserSelectionCommand() {
		selections = new ArrayList<>();
	}

	public Long getBelongTo() {
		return belongTo;
	}

	public void setBelongTo(Long belongTo) {
		this.belongTo = belongTo;
	}

	public String getFlowUserType() {
		return flowUserType;
	}

	public void setFlowUserType(String flowUserType) {
		this.flowUserType = flowUserType;
	}

	public String getFlowEntityType() {
		return flowEntityType;
	}

	public void setFlowEntityType(String flowEntityType) {
		this.flowEntityType = flowEntityType;
	}

	public List<FlowSingleUserSelectionCommand> getSelections() {
		return selections;
	}

	public void setSelections(List<FlowSingleUserSelectionCommand> selections) {
		this.selections = selections;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
