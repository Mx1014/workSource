package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 * <li>nodeId: 节点ID</li>
 * <li>nodeLevel: 节点层数</li>
 * <li>laneId: 泳道id</li>
 * <li>nodeName: 节点名字</li>
 * <li>allowComment: 是否可以评论</li>
 * <li>logs: 详细日志信息，目前仅有 logContent 有用 </li>
 * <li>nodeEnterTime: 节点进入时间 </li>
 * </ul>
 * @author janson
 *
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
