// @formatter:off
package com.everhomes.rest.forum;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <p>物业帖子或评论信息：</p>
 * <ul>
 * <li>id: 帖子或评论ID</li>
 * <li>parentPostId: 帖子或评论的父亲ID</li>
 * <li>forumId: 论坛ID</li>
 * <li>creatorUid: 创建者ID</li>
 * <li>creatorNickName: 创建者在圈内的昵称</li>
 * <li>creatorAvatar: 创建者在圈内的头像URI</li>
 * <li>creatorAvatarUrl: 创建者在圈内的头像URL</li>
 * <li>creatorAdminFlag: 创建者是否为圈的管理员</li>
 * <li>creatorTag: 创建者标签，参考{@link com.everhomes.rest.forum.PostEntityTag}</li>
 * <li>targetTag: 创建者标签，参考{@link com.everhomes.rest.forum.PostEntityTag}</li>
 * <li>contentCategory: 内容类型ID，含类和子类</li>
 * <li>actionCategory: 操作类型ID，如拼车中的“我搭车”、“我开车”</li>
 * <li>visibleRegionType: 区域范围类型，{@link com.everhomes.rest.visibility.VisibleRegionType}</li>
 * <li>visibleRegionId: 区域范围类型对应的ID</li>
 * <li>longitude: 帖子或评论内容涉及到的经度如活动</li>
 * <li>latitude: 帖子或评论内容涉及到的纬度如活动</li>
 * <li>subject: 帖子或评论标题</li>
 * <li>contentType: 帖子或评论内容类型，{@link com.everhomes.rest.forum.PostContentType}</li>
 * <li>content: 帖子或评论内容</li>
 * <li>embeddedAppId: 内嵌对象对应的App ID，{@link com.everhomes.rest.app.AppConstants}</li>
 * <li>embeddedId: 内嵌对象ID</li>
 * <li>embeddedJson: 内嵌对象列表对应的json字符串</li>
 * <li>isForwarded: 是否是转发帖的标记</li>
 * <li>childCount: 孩子数目，如帖子下的评论数目</li>
 * <li>forwardCount: 帖子或评论的转发数目</li>
 * <li>likeCount: 帖子或评论赞的数目</li>
 * <li>dislikeCount: 帖子或评论踩的数目</li>
 * <li>updateTime: 帖子或评论更新时间</li>
 * <li>createTime: 帖子或评论创建时间</li>
 * <li>attachments: 帖子或评论的附件信息，参见{@link com.everhomes.rest.forum.AttachmentDTO}</li>
 * <li>assignedFlag: 是否推荐帖，参见{@link com.everhomes.rest.forum.PostAssignedFlag}</li>
 * 
 * <li>taskId: 帖子任务ID</li>
 * <li>communityId: 帖子的小区id </li>
 * <li>entityType: 任务的实体类型：topic</li>
 * <li>entityId: 帖子id</li>
 * <li>targetType: 帖子任务目标类型user</li>
 * <li>targetId: userId</li>
 * <li>taskType: 帖子任务所属类型  参见{@link com.everhomes.OrganizationTaskType.PmTaskType}</li>
 * <li>taskStatus: 帖子任务状态 参见{@link com.everhomes.rest.organization.OrganizationTaskStatus.PmTaskStatus}</li>
 * </ul>
 */
public class PropertyPostDTO  extends PostDTO{

    private Long   taskId;
    private Long   communityId;
   	private String entityType;
   	private Long   entityId;
   	private String targetType;
   	private Long   targetId;
   	private String taskType;
   	private Byte   taskStatus;

    public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public Byte getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Byte taskStatus) {
		this.taskStatus = taskStatus;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
