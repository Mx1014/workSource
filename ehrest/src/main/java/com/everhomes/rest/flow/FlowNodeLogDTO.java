package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.GeneralFormFieldsConfigDTO;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>nodeId: 节点ID</li>
 *     <li>laneId: 泳道id</li>
 *     <li>flowCaseId: flowCaseId</li>
 *     <li>stepCount: stepCount</li>
 *     <li>nodeLevel: 节点层数</li>
 *     <li>nodeName: 节点名字</li>
 *     <li>allowComment: 是否可以评论</li>
 *     <li>isCurrentNode: isCurrentNode</li>
 *     <li>isRejectNode: isRejectNode</li>
 *     <li>commentButtonId: commentButtonId</li>
 *     <li>params: params</li>
 *     <li>needSelectNextNode: needSelectNextNode</li>
 *     <li>nodeEnterTime: 节点进入时间</li>
 *     <li>formOriginId: 表单id, 后期会废弃这个字段,见 formDirectRelation</li>
 *     <li>formVersion: 表单版本, 后期会废弃这个字段,见 formDirectRelation</li>
 *     <li>formRelationType: 表单关联类型 {@link com.everhomes.rest.flow.FlowFormRelationType}</li>
 *     <li>formDirectRelation: 直接关联表单 {@link com.everhomes.rest.flow.FlowFormRelationDataDirectRelation}</li>
 *     <li>formConfigDTO: 关联表单字段 {@link com.everhomes.rest.general_approval.GeneralFormFieldsConfigDTO}</li>
 *     <li>logs: 详细日志信息，目前仅有 logContent 有用 {@link com.everhomes.rest.flow.FlowEventLogDTO}</li>
 * </ul>
 */
public class FlowNodeLogDTO {

	private Long nodeId;
	private Long laneId;
	private Long flowCaseId;
	private Long stepCount;
	private Integer nodeLevel;
	private String nodeName;
	private Byte allowComment;
	private Byte isCurrentNode;
	private Byte isRejectNode;
	private Long commentButtonId;
	private String params;
	private Byte needSelectNextNode;
	private Long nodeEnterTime;
	private Long formOriginId;
	private Long formVersion;

	private Byte formRelationType;
	private FlowFormRelationDataDirectRelation formDirectRelation;
	private GeneralFormFieldsConfigDTO formConfigDTO;

	@ItemType(FlowEventLogDTO.class)
	private List<FlowEventLogDTO> logs;

	public FlowNodeLogDTO() {
		logs = new ArrayList<>();
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public List<FlowEventLogDTO> getLogs() {
		return logs;
	}

	public void setLogs(List<FlowEventLogDTO> logs) {
		this.logs = logs;
	}

	public Byte getAllowComment() {
		return allowComment;
	}

	public void setAllowComment(Byte allowComment) {
		this.allowComment = allowComment;
	}

	public Integer getNodeLevel() {
		return nodeLevel;
	}

	public void setNodeLevel(Integer nodeLevel) {
		this.nodeLevel = nodeLevel;
	}

	public Byte getIsCurrentNode() {
		return isCurrentNode;
	}

	public void setIsCurrentNode(Byte isCurrentNode) {
		this.isCurrentNode = isCurrentNode;
	}

	public Long getCommentButtonId() {
		return commentButtonId;
	}

	public Long getLaneId() {
		return laneId;
	}

	public void setLaneId(Long laneId) {
		this.laneId = laneId;
	}

	public void setCommentButtonId(Long commentButtonId) {
		this.commentButtonId = commentButtonId;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public void setNeedSelectNextNode(Byte needSelectNextNode) {
		this.needSelectNextNode = needSelectNextNode;
	}

	public Byte getNeedSelectNextNode() {
		return needSelectNextNode;
	}

	public Long getNodeEnterTime() {
		return nodeEnterTime;
	}

	public void setNodeEnterTime(Long nodeEnterTime) {
		this.nodeEnterTime = nodeEnterTime;
	}

	public Byte getIsRejectNode() {
		return isRejectNode;
	}

	public Long getFormOriginId() {
		return formOriginId;
	}

	public void setFormOriginId(Long formOriginId) {
		this.formOriginId = formOriginId;
	}

	public Long getFormVersion() {
		return formVersion;
	}

	public void setFormVersion(Long formVersion) {
		this.formVersion = formVersion;
	}

	public void setIsRejectNode(Byte isRejectNode) {
		this.isRejectNode = isRejectNode;
	}

	public Long getFlowCaseId() {
		return flowCaseId;
	}

	public void setFlowCaseId(Long flowCaseId) {
		this.flowCaseId = flowCaseId;
	}

	public Long getStepCount() {
		return stepCount;
	}

	public void setStepCount(Long stepCount) {
		this.stepCount = stepCount;
	}

	public Byte getFormRelationType() {
		return formRelationType;
	}

	public void setFormRelationType(Byte formRelationType) {
		this.formRelationType = formRelationType;
	}

	public FlowFormRelationDataDirectRelation getFormDirectRelation() {
		return formDirectRelation;
	}

	public void setFormDirectRelation(FlowFormRelationDataDirectRelation formDirectRelation) {
		this.formDirectRelation = formDirectRelation;
	}

	public GeneralFormFieldsConfigDTO getFormConfigDTO() {
		return formConfigDTO;
	}

	public void setFormConfigDTO(GeneralFormFieldsConfigDTO formConfigDTO) {
		this.formConfigDTO = formConfigDTO;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
