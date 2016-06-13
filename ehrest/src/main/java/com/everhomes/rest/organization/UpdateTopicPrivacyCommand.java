package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>organizationId: 组织id</li>
 * <li>forumId: 论坛id</li>
 * <li>topicId: 帖子id</li>
 * <li>privacy: 是否分开标记，参考{@link com.everhomes.rest.forum.PostPrivacy}</li>
 * </ul>
 */
public class UpdateTopicPrivacyCommand {
	private Long organizationId;
	
	private Long forumId;
	
	private Long topicId;
	
	private Byte privacy;
	
	public Long getOrganizationId() {
		return organizationId;
	}
	
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	
	public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Byte getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Byte privacy) {
        this.privacy = privacy;
    }

    @Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	

}
