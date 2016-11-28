// @formatter:off
package com.everhomes.rest.message;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>communityId: 园区id</li>
 * <li>adminFlag: 1是0否</li>
 * <li>businessContactFlag: 1是0否</li>
 * <li>content: 内容</li>
 * </ul>
 */
public class PushMessageToAdminAndBusinessContactsCommand {

	private Long communityId;

	private Byte adminFlag;

	private Byte businessContactFlag;
	
	private String content;

	public PushMessageToAdminAndBusinessContactsCommand() {

	}

	public PushMessageToAdminAndBusinessContactsCommand(Long communityId, Byte adminFlag, Byte businessContactFlag,
			String content) {
		super();
		this.communityId = communityId;
		this.adminFlag = adminFlag;
		this.businessContactFlag = businessContactFlag;
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Byte getAdminFlag() {
		return adminFlag;
	}

	public void setAdminFlag(Byte adminFlag) {
		this.adminFlag = adminFlag;
	}

	public Byte getBusinessContactFlag() {
		return businessContactFlag;
	}

	public void setBusinessContactFlag(Byte businessContactFlag) {
		this.businessContactFlag = businessContactFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
