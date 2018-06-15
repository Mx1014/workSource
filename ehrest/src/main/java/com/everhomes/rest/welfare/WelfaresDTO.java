package com.everhomes.rest.welfare;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: 福利Id</li>
 * <li>ownerType: 所属类型,填organization</li>
 * <li>ownerId: 公司id</li>
 * <li>subject:  主题名称</li>
 * <li>content: 祝福语</li>
 * <li>senderName:发放者姓名</li>
 * <li>senderUid:发放者userId</li>
 * <li>senderDetailId: 发放者detailId</li>
 * <li>senderName: 发放者姓名</li>
 * <li>imgUri: 图片uri</li>
 * <li>imgUrl: 图片url</li>
 * <li>status: 状态:0-草稿 1-已发送 </li>
 * <li>sendTime: 发送时间</li>
 * <li>creatorName: 创建者</li>
 * <li>creatorUid: 创建者uId</li>
 * <li>createTime: 创建时间</li>
 * <li>operatorName: 操作者</li>
 * <li>operatorUid: 操作者uId</li>
 * <li>updateTime: 操作时间</li>
 * <li>receivers: 接收人列表 参考{@link com.everhomes.rest.welfare.WelfareReceiverDTO}</li>
 * <li>items: 福利项列表 参考{@link com.everhomes.rest.welfare.WelfareItemDTO}</li>
 * </ul>
 */
public class WelfaresDTO {
    private Long id;
    private String ownerType;
    private Long ownerId;
    private String subject;
    private String content;
    private String senderName;
    private Long senderUid;
    private Long senderDetailId;
    private String imgUri;
    private String imgUrl;
    private Byte status;
    private Long sendTime;
    private String creatorName;
    private Long creatorUid;
    private Long createTime;
    private String operatorName;
    private Long operatorUid;
    private Long updateTime;
    private List<WelfareReceiverDTO> receivers;
    private List<WelfareItemDTO> items;

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

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Long getSendTime() {
		return sendTime;
	}

	public void setSendTime(Long sendTime) {
		this.sendTime = sendTime;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Long getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Long getOperatorUid() {
		return operatorUid;
	}

	public void setOperatorUid(Long operatorUid) {
		this.operatorUid = operatorUid;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public List<WelfareReceiverDTO> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<WelfareReceiverDTO> receivers) {
		this.receivers = receivers;
	}

	public List<WelfareItemDTO> getItems() {
		return items;
	}

	public void setItems(List<WelfareItemDTO> items) {
		this.items = items;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
}
