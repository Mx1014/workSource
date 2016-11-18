package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class FlowActionDTO {
    private Byte     status;
    private String     renderText;
    private Long     flowMainId;
    private String     stepType;
    private Integer     namespaceId;
    private String     actionType;
    private Timestamp     createTime;
    private Integer     flowVersion;
    private Long     id;
    private Long     belongTo;
    private String     belongType;

    public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getRenderText() {
		return renderText;
	}

	public void setRenderText(String renderText) {
		this.renderText = renderText;
	}

	public Long getFlowMainId() {
		return flowMainId;
	}

	public void setFlowMainId(Long flowMainId) {
		this.flowMainId = flowMainId;
	}

	public String getStepType() {
		return stepType;
	}

	public void setStepType(String stepType) {
		this.stepType = stepType;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getFlowVersion() {
		return flowVersion;
	}

	public void setFlowVersion(Integer flowVersion) {
		this.flowVersion = flowVersion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBelongTo() {
		return belongTo;
	}

	public void setBelongTo(Long belongTo) {
		this.belongTo = belongTo;
	}

	public String getBelongType() {
		return belongType;
	}

	public void setBelongType(String belongType) {
		this.belongType = belongType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

