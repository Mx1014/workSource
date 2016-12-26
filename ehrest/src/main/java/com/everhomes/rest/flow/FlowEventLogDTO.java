package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>isEvaluate: 是否是评价节点</li>
 * </ul>
 * @author janson
 *
 */
public class FlowEventLogDTO {
    private Long     id;
    private Long     flowMainId;
    private String     logType;
    private String     logContent;
    private Long     flowActionId;
    private String     logTitle;
    private Long     flowUserId;
    private Long     subjectId;
    private Long     flowSelectionId;
    private Long     flowNodeId;
    private Long     parentId;
    private Integer     flowVersion;
    private Integer     namespaceId;
    private Long     flowCaseId;
    private Long     flowButtonId;
    private Timestamp createTime;
    private Byte isEvaluate;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFlowMainId() {
		return flowMainId;
	}

	public void setFlowMainId(Long flowMainId) {
		this.flowMainId = flowMainId;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getLogContent() {
		return logContent;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

	public Long getFlowActionId() {
		return flowActionId;
	}

	public void setFlowActionId(Long flowActionId) {
		this.flowActionId = flowActionId;
	}

	public String getLogTitle() {
		return logTitle;
	}

	public void setLogTitle(String logTitle) {
		this.logTitle = logTitle;
	}

	public Long getFlowUserId() {
		return flowUserId;
	}

	public void setFlowUserId(Long flowUserId) {
		this.flowUserId = flowUserId;
	}

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public Long getFlowSelectionId() {
		return flowSelectionId;
	}

	public void setFlowSelectionId(Long flowSelectionId) {
		this.flowSelectionId = flowSelectionId;
	}

	public Long getFlowNodeId() {
		return flowNodeId;
	}

	public void setFlowNodeId(Long flowNodeId) {
		this.flowNodeId = flowNodeId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getFlowVersion() {
		return flowVersion;
	}

	public void setFlowVersion(Integer flowVersion) {
		this.flowVersion = flowVersion;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getFlowCaseId() {
		return flowCaseId;
	}

	public void setFlowCaseId(Long flowCaseId) {
		this.flowCaseId = flowCaseId;
	}

	public Long getFlowButtonId() {
		return flowButtonId;
	}

	public void setFlowButtonId(Long flowButtonId) {
		this.flowButtonId = flowButtonId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Byte getIsEvaluate() {
		return isEvaluate;
	}

	public void setIsEvaluate(Byte isEvaluate) {
		this.isEvaluate = isEvaluate;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

