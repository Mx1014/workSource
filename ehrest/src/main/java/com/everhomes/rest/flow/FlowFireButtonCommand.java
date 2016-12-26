package com.everhomes.rest.flow;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul> 按钮时间触发
 * <li>flowCaseId: 工作流任务 ID </li>
 * <li>buttonId: 工作流按钮 ID </li>
 * <li>stepCount: 当前节点的 stepCount，用来防止重复提交</li>
 * <li>title: 评论标题 </li>
 * <li>content: 评论内容</li>
 * <li>entityId: 用户 ID 或者 选择人员的 ID </li>
 * <li>flowEntityType: 客户端对象选择下个节点的用户 ID 或者 用户选择类型 ID</li>
 * <li>
 * </ul>
 * @author janson
 *
 */
public class FlowFireButtonCommand {
	private Long flowCaseId;
	private Long buttonId;
	private Long stepCount;
	
	@ItemType(String.class)
	private List<String> images;
	
	private String title;
	private String content;
	
	private Long entityId;
	private String flowEntityType;
	
	public FlowFireButtonCommand() {
		images = new ArrayList<String>();
	}

	public Long getFlowCaseId() {
		return flowCaseId;
	}

	public void setFlowCaseId(Long flowCaseId) {
		this.flowCaseId = flowCaseId;
	}

	public Long getButtonId() {
		return buttonId;
	}

	public void setButtonId(Long buttonId) {
		this.buttonId = buttonId;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getFlowEntityType() {
		return flowEntityType;
	}

	public void setFlowEntityType(String flowEntityType) {
		this.flowEntityType = flowEntityType;
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
