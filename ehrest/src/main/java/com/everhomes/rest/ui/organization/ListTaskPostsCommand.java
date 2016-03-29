// @formatter:off
package com.everhomes.rest.ui.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>sceneToken: 场景标识，用一个标识代替原来用多个字段共同表示的标识，以使传参数简单一些（只需要传一个参数）</li>
 *  <li>taskType : 任务类型 ，详情{@link com.everhomes.rest.organization.OrganizationTaskType}</li>
 *  <li>taskStatus : 任务状态,查询全部就不要传参 ，详情{@link com.everhomes.rest.organization.OrganizationTaskStatus}</li>
 *  <li>communityId : 小区id</li>
 * 	<li>pageAnchor : 页码</li>
 *	<li>pageSize : 页大小</li>
 *	<li>option :  process（处理） 和 grab（抢单） none（无操作）</li>
 *	<li>entrancePrivilege标识 :  权限标识</li>
 * </ul>
 *
 */
public class ListTaskPostsCommand {
	
	private String sceneToken;
	
	private Long communityId;
	
	private String taskType; 
	
	private Byte taskStatus;
	
	private Long pageAnchor;
	
	private Integer pageSize;
	
	private String option;
	
	private String entrancePrivilege;

	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
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

	
	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	
	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	
	public String getEntrancePrivilege() {
		return entrancePrivilege;
	}

	public void setEntrancePrivilege(String entrancePrivilege) {
		this.entrancePrivilege = entrancePrivilege;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
