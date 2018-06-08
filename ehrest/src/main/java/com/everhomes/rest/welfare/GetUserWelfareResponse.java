// @formatter:off
package com.everhomes.rest.welfare;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>id: 福利Id</li>
 * <li>ownerType: 所属类型,填organization</li>
 * <li>ownerId: 公司id</li>
 * <li>subject:  主题名称</li>
 * <li>content: 祝福语</li>
 * <li>senderName:发放者姓名</li>
 * <li>senderUid:发放者userId</li>
 * <li>senderDetailId: 发放者detailId</li>
 * <li>senderName: 发放者姓名</li>
 * <li>attachmentImgUri: 图片uri</li>
 * <li>attachmentImgUrl: 图片url</li> 
 * <li>sendTime: 发送时间</li>
 * </ul>
 */
public class GetUserWelfareResponse {

    private Long id;
    private String ownerType;
    private Long ownerId;
    private String subject;
    private String content;
    private String senderName;
    private Long senderUid;
    private Long senderDetailId;
    private String attachmentImgUri;
    private String attachmentImgUrl; 
    private Long sendTime; 

	public GetUserWelfareResponse() {

	}
 

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getOwnerType() {
		return ownerType;
	}


	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}


	public Long getOwnerId() {
		return ownerId;
	}


	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
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


	public String getAttachmentImgUri() {
		return attachmentImgUri;
	}


	public void setAttachmentImgUri(String attachmentImgUri) {
		this.attachmentImgUri = attachmentImgUri;
	}


	public String getAttachmentImgUrl() {
		return attachmentImgUrl;
	}


	public void setAttachmentImgUrl(String attachmentImgUrl) {
		this.attachmentImgUrl = attachmentImgUrl;
	}


	public Long getSendTime() {
		return sendTime;
	}


	public void setSendTime(Long sendTime) {
		this.sendTime = sendTime;
	}

}
