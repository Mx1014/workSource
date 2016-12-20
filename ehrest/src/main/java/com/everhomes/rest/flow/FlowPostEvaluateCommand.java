package com.everhomes.rest.flow;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul> 在某节点进行评论
 * <li>userId: 当前登录用户 ID ，可以不传</li>
 * <li> flowCaseId: 当前 case 的 ID </li>
 * <li> flowNodeId: 当前节点的 ID </li>
 * </ul>
 * @author janson
 *
 */
public class FlowPostEvaluateCommand {
	private Long     userId;
	private Long     flowCaseId;
	private Long flowNodeId;
	
	@ItemType(FlowEvaluateItemStar.class)
	List<FlowEvaluateItemStar> stars;

	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getFlowCaseId() {
		return flowCaseId;
	}
	public void setFlowCaseId(Long flowCaseId) {
		this.flowCaseId = flowCaseId;
	}
	public Long getFlowNodeId() {
		return flowNodeId;
	}
	public void setFlowNodeId(Long flowNodeId) {
		this.flowNodeId = flowNodeId;
	}

	public List<FlowEvaluateItemStar> getStars() {
		return stars;
	}
	public void setStars(List<FlowEvaluateItemStar> stars) {
		this.stars = stars;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
