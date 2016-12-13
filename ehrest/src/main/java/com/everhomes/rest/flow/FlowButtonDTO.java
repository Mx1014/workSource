package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>needProcessor: 需要选人</li>
 * <li>needProcessor: 需要评论</li>
 * <li>needProcessor + needSubject 有任何一个就要跳到下个界面</li>
 * </ul>
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
    private Byte needProcessor;
    private Byte needSubject;
    private Integer remindCount;
    private String description;

	public Byte getNeedProcessor() {
		return needProcessor;
	}


	public void setNeedProcessor(Byte needProcessor) {
		this.needProcessor = needProcessor;
	}


	public Byte getNeedSubject() {
		return needSubject;
	}


	public void setNeedSubject(Byte needSubject) {
		this.needSubject = needSubject;
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


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Integer getRemindCount() {
		return remindCount;
	}


	public void setRemindCount(Integer remindCount) {
		this.remindCount = remindCount;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

