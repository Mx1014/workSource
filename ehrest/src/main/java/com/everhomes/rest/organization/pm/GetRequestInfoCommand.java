// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;


/**
 * <ul>咨询类的Meta对象，一般应用于：1、请求加入某种资源，如A请求加入B-group时，需要咨询B-group里的所有成员是否通过该请求，
 *     此时resource就是B-group，requestor是A，无target;
 *     2、邀请某人加入某资源，如A邀请B加入C-group时，需要咨询A是否同意加入，此时resource就是C-group，requestor是A，target为B;
 * <li>resourceType: 资源类型，参考{@link com.everhomes.entity.EntityType}</li>
 * <li>resourceId: 资源ID</li>
 * <li>targetType: 目标类型，参考{@link com.everhomes.entity.EntityType}</li>
 * <li>targetId: 目标ID</li>
 * <li>requestorUid: 请求者用户ID</li>
 * <li>requestorNickName: 请求者昵称</li>
 * <li>requestorAvatar: 请求者头像URI</li>
 * <li>requestorAvatarUrl: 请求者头像URL</li>
 * <li>requestTime: 请求时间</li>
 * <li>requestInfo: 申请描述</li>
 * <li>requestId: 申请id</li>
 * </ul>
 */
public class GetRequestInfoCommand {
    private String resourceType;
    private Long resourceId;
    private String targetType;
    private Long targetId;
    private Long requestorUid;
    private String requestorNickName;
    private String requestorAvatar;
    private String requestorAvatarUrl;
    private Long requestTime;
    private String requestInfo;
    private Long requestId;

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
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

	public Long getRequestorUid() {
		return requestorUid;
	}

	public void setRequestorUid(Long requestorUid) {
		this.requestorUid = requestorUid;
	}

	public String getRequestorNickName() {
		return requestorNickName;
	}

	public void setRequestorNickName(String requestorNickName) {
		this.requestorNickName = requestorNickName;
	}

	public String getRequestorAvatar() {
		return requestorAvatar;
	}

	public void setRequestorAvatar(String requestorAvatar) {
		this.requestorAvatar = requestorAvatar;
	}

	public String getRequestorAvatarUrl() {
		return requestorAvatarUrl;
	}

	public void setRequestorAvatarUrl(String requestorAvatarUrl) {
		this.requestorAvatarUrl = requestorAvatarUrl;
	}

	public Long getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Long requestTime) {
		this.requestTime = requestTime;
	}

	public String getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(String requestInfo) {
		this.requestInfo = requestInfo;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
