package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>flowNodeId: 节点 ID</li>
 * <li>flowNodeName: 节点名字</li>
 * <li>autoStepMinute: 多久自动走到下一步</li>
 * <li>allowApplierUpdate: 发起人是否可以修改表单</li>
 * <li>autoStepType: 自动跳转的动作 {@link com.everhomes.rest.flow.FlowStepType}</li>
 * <li>allowTimeoutAction: 是否启动 Timeout 动作</li>
 * <li>params: 节点的额为参数</li>
 * <li></li>
 * </ul>
 * @author janson
 *
 */
public class UpdateFlowNodeCommand {
	private Long flowNodeId;
	private String flowNodeName;
	private Integer autoStepMinute;
	private Byte allowApplierUpdate;
	private Byte allowTimeoutAction;
	private String autoStepType;
	private String params;

	public String getAutoStepType() {
		return autoStepType;
	}
	public void setAutoStepType(String autoStepType) {
		this.autoStepType = autoStepType;
	}
	public Integer getAutoStepMinute() {
		return autoStepMinute;
	}
	public void setAutoStepMinute(Integer autoStepMinute) {
		this.autoStepMinute = autoStepMinute;
	}
	public Byte getAllowApplierUpdate() {
		return allowApplierUpdate;
	}
	public void setAllowApplierUpdate(Byte allowApplierUpdate) {
		this.allowApplierUpdate = allowApplierUpdate;
	}
	public Long getFlowNodeId() {
		return flowNodeId;
	}
	public void setFlowNodeId(Long flowNodeId) {
		this.flowNodeId = flowNodeId;
	}
	public String getFlowNodeName() {
		return flowNodeName;
	}
	public void setFlowNodeName(String flowNodeName) {
		this.flowNodeName = flowNodeName;
	}

	public Byte getAllowTimeoutAction() {
		return allowTimeoutAction;
	}
	public void setAllowTimeoutAction(Byte allowTimeoutAction) {
		this.allowTimeoutAction = allowTimeoutAction;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }	
}
