// @formatter:off
package com.everhomes.rest.openapi.jindi;

/**
 * 
 * <ul>金地同步数据的活动报名数据
 * <li>: </li>
 * </ul>
 */
public class JindiActionActivitySignupDTO extends JindiDataDTO {
	private Long id;
	private Long userId;
	private Long activityId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getActivityId() {
		return activityId;
	}
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

}
