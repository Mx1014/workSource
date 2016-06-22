package com.everhomes.rest.activity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.forum.AttachmentDTO;
import com.everhomes.util.StringHelper;

/**
 * 
 *<ul>
 * <li>activity: 活动</li>
 * <li>childCount: 孩子数目，如帖子下的评论数目</li>
 * <li>viewCount: 浏览的数目</li>
 * <li>attachments: 帖子或评论的附件信息，参见{@link com.everhomes.rest.forum.AttachmentDTO}</li>
 *</ul>
 */
public class ActivityShareDetailResponse {
	
	private ActivityDTO activity;
	
	private String content;
	
	private Long childCount;
	
	private Long viewCount;
	
	private Integer namespaceId;
	
	@ItemType(AttachmentDTO.class)
    private List<AttachmentDTO> attachments;
	
	public ActivityDTO getActivity() {
		return activity;
	}


	public void setActivity(ActivityDTO activity) {
		this.activity = activity;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public Long getChildCount() {
		return childCount;
	}


	public void setChildCount(Long childCount) {
		this.childCount = childCount;
	}


	public Long getViewCount() {
		return viewCount;
	}


	public void setViewCount(Long viewCount) {
		this.viewCount = viewCount;
	}


	public Integer getNamespaceId() {
		return namespaceId;
	}


	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}


	public List<AttachmentDTO> getAttachments() {
		return attachments;
	}


	public void setAttachments(List<AttachmentDTO> attachments) {
		this.attachments = attachments;
	}

	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
