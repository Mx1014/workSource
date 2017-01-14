package com.everhomes.rest.flow;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nodeId: 节点ID</li>
 * <li>nodeLevel: 节点层数</li>
 * <li>nodeName: 节点名字</li>
 * <li>allowComment: 是否可以评论</li>
 * <li>logs: 详细日志信息，目前仅有 logContent 有用 </li>
 * </ul>
 * @author janson
 *
 */
public class FlowNodeLogDTO {
	private Long nodeId;
	private Integer nodeLevel;
	private String nodeName;
	private Byte allowComment;
	private Byte isCurrentNode;
	private Long commentButtonId;
	private String params;

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

	public void setCommentButtonId(Long commentButtonId) {
		this.commentButtonId = commentButtonId;
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
