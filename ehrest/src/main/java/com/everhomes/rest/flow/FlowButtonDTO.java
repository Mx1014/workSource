package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

/**
 * needSelection + needComment 有任何一个就要跳到下个界面
 * @author janson
 *
 */
public class FlowButtonDTO {
    private Byte     status;
    private Integer     gotoLevel;
    private Long     flowNodeId;
    private Long     flowMainId;
    private Timestamp     createTime;
    private Integer     namespaceId;
    private String     flowStepType;
    private Integer     flowVersion;
    private Long     id;
    private String     buttonName;
    private Byte needSelection;//TODO
    private Byte needComment;//TODO


    public Byte getNeedComment() {
		return needComment;
	}


	public void setNeedComment(Byte needComment) {
		this.needComment = needComment;
	}


	public Byte getNeedSelection() {
		return needSelection;
	}


	public void setNeedSelection(Byte needSelection) {
		this.needSelection = needSelection;
	}


	public Byte getStatus() {
		return status;
	}


	public void setStatus(Byte status) {
		this.status = status;
	}


	public Integer getGotoLevel() {
		return gotoLevel;
	}


	public void setGotoLevel(Integer gotoLevel) {
		this.gotoLevel = gotoLevel;
	}


	public Long getFlowNodeId() {
		return flowNodeId;
	}


	public void setFlowNodeId(Long flowNodeId) {
		this.flowNodeId = flowNodeId;
	}


	public Long getFlowMainId() {
		return flowMainId;
	}


	public void setFlowMainId(Long flowMainId) {
		this.flowMainId = flowMainId;
	}


	public Timestamp getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}


	public Integer getNamespaceId() {
		return namespaceId;
	}


	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}


	public String getFlowStepType() {
		return flowStepType;
	}


	public void setFlowStepType(String flowStepType) {
		this.flowStepType = flowStepType;
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


	public String getButtonName() {
		return buttonName;
	}


	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

