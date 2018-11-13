// @formatter:off
package com.everhomes.rest.welfare;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: 福利Id 有就填没有不填</li>
 * <li>organizationId: 公司id 必填</li>
 * <li>subject:  主题名称 可不填</li>
 * <li>content: 祝福语 可不填</li>
 * <li>senderName:发放者姓名 可不填</li>
 * <li>senderUid:发放者userId 可不填</li>
 * <li>senderDetailId: 发放者detailId 可不填</li>
 * <li>senderName: 发放者姓名 必填</li>
 * <li>imgUri: 图片uri 可不填</li>
 * </ul>
 */
public class DraftWelfareCommand {
    private Long id;
    private Long organizationId;
    private String subject;
    private String content;
    private String senderName;
    private Long senderUid;
	private Long senderDetailId;
    private String imgUri;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public Long getSenderUid() {
		return senderUid;
	}

	public void setSenderUid(Long senderUid) {
		this.senderUid = senderUid;
	}

	public Long getSenderDetailId() {
		return senderDetailId;
	}

	public void setSenderDetailId(Long senderDetailId) {
		this.senderDetailId = senderDetailId;
	}

	public String getImgUri() {
		return imgUri;
	}

	public void setImgUri(String imgUri) {
		this.imgUri = imgUri;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
