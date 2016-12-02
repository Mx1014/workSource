package com.everhomes.rest.flow;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul> 每一步创建的评论信息
 * <li> flowEntityId: 实体ID </li>
 * <li> flowEntityType: 实体类型，比如 FlowButton 或者 FlowNode（附言的时候）{@link com.everhomes.rest.flow.FlowEntityType} </li>
 * <li> title: 标题 </li>
 * <li> content: 评论内容 </li>
 * </ul>
 * 
 * @author janson
 *
 */
public class FlowPostSubjectCommand {
	private Long flowEntityId;
	private String flowEntityType;
	
	@ItemType(String.class)
	private List<String> images;
	
	private String title;
	private String content;

	public Long getFlowEntityId() {
		return flowEntityId;
	}

	public void setFlowEntityId(Long flowEntityId) {
		this.flowEntityId = flowEntityId;
	}

	public String getFlowEntityType() {
		return flowEntityType;
	}

	public void setFlowEntityType(String flowEntityType) {
		this.flowEntityType = flowEntityType;
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
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
