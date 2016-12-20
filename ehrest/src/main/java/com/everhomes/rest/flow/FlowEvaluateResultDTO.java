package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>evaluateItemId: itemId</li>
 * <li>flowNodeId: 节点 ID</li>
 * <li>name: 节点名字</li>
 * <li>star: 评分</li>
 * </ul>
 * @author janson
 *
 */
public class FlowEvaluateResultDTO {
	private Long evaluateItemId;
	private Long flowNodeId;
	private String name;
	private Byte star;

	public Long getEvaluateItemId() {
		return evaluateItemId;
	}

	public void setEvaluateItemId(Long evaluateItemId) {
		this.evaluateItemId = evaluateItemId;
	}

	public Long getFlowNodeId() {
		return flowNodeId;
	}

	public void setFlowNodeId(Long flowNodeId) {
		this.flowNodeId = flowNodeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getStar() {
		return star;
	}

	public void setStar(Byte star) {
		this.star = star;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
