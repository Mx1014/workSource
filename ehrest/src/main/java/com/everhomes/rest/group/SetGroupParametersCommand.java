// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>namespaceId: 域空间id</li>
 * <li>createFlag: 是否允许创建，参考{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * <li>verifyFlag: 创建是否需要审核，参考{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * <li>memberPostFlag: 是否允许成员发帖，参考{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * <li>memberCommentFlag: 是否允许成员回复，参考{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * <li>adminBroadcastFlag: 是否允许管理发广播，参考{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * <li>broadcastCount: 每天可发广播数</li>
 * </ul>
 */
public class SetGroupParametersCommand {
	
	private Integer namespaceId;
	
	private Byte createFlag;

	private Byte verifyFlag;

	private Byte memberPostFlag;

	private Byte memberCommentFlag;

	private Byte adminBroadcastFlag;

	private Integer broadcastCount;

	public SetGroupParametersCommand() {

	}

	public SetGroupParametersCommand(Integer namespaceId, Byte createFlag, Byte verifyFlag, Byte memberPostFlag,
			Byte memberCommentFlag, Byte adminBroadcastFlag, Integer broadcastCount) {
		super();
		this.namespaceId = namespaceId;
		this.createFlag = createFlag;
		this.verifyFlag = verifyFlag;
		this.memberPostFlag = memberPostFlag;
		this.memberCommentFlag = memberCommentFlag;
		this.adminBroadcastFlag = adminBroadcastFlag;
		this.broadcastCount = broadcastCount;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Byte getCreateFlag() {
		return createFlag;
	}

	public void setCreateFlag(Byte createFlag) {
		this.createFlag = createFlag;
	}

	public Byte getVerifyFlag() {
		return verifyFlag;
	}

	public void setVerifyFlag(Byte verifyFlag) {
		this.verifyFlag = verifyFlag;
	}

	public Byte getMemberPostFlag() {
		return memberPostFlag;
	}

	public void setMemberPostFlag(Byte memberPostFlag) {
		this.memberPostFlag = memberPostFlag;
	}

	public Byte getMemberCommentFlag() {
		return memberCommentFlag;
	}

	public void setMemberCommentFlag(Byte memberCommentFlag) {
		this.memberCommentFlag = memberCommentFlag;
	}

	public Byte getAdminBroadcastFlag() {
		return adminBroadcastFlag;
	}

	public void setAdminBroadcastFlag(Byte adminBroadcastFlag) {
		this.adminBroadcastFlag = adminBroadcastFlag;
	}

	public Integer getBroadcastCount() {
		return broadcastCount;
	}

	public void setBroadcastCount(Integer broadcastCount) {
		this.broadcastCount = broadcastCount;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
