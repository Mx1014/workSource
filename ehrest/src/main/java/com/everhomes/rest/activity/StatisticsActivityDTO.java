package com.everhomes.rest.activity;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	   <li>activityId: 活动Id</li>
 * 	   <li>forumId: 论坛Id</li>
 *     <li>postId: 帖子Id</li>
 *     <li>subject: 活动名称</li>
 *     <li>enrollUserCount: 报名人数总数</li>
 *     <li>createTime: 创建时间</li>
 * </ul>
 */
public class StatisticsActivityDTO {

	private Long activityId;
	
	private Long forumId;
	
	private Long postId;
	
	private String subject;
	
	private Integer enrollUserCount;
	
	private Long createTime;

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public Long getForumId() {
		return forumId;
	}

	public void setForumId(Long forumId) {
		this.forumId = forumId;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Integer getEnrollUserCount() {
		return enrollUserCount;
	}

	public void setEnrollUserCount(Integer enrollUserCount) {
		this.enrollUserCount = enrollUserCount;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
        return StringHelper.toJsonString(this);
    }
}
