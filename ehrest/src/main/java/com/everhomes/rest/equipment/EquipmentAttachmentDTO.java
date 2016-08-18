package com.everhomes.rest.equipment;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 任务主键id</li>
 *  <li>equipmentId: 设备id</li>
 *  <li>attachmentType: 类型 0: none, 1: 操作图示, 2: 说明书</li>
 *  <li>contentType: 附件类型IMAGE("image"): 图片 AUDIO("audio"): 音频 VIDEO("video"): 视频 </li>
 *  <li>contentUri: 附件访问URI</li>
 *  <li>contentUrl: 附件访问URL</li>
 *  <li>creatorUid: 创建人id</li>
 *  <li>createTime: 创建时间</li>
 * </ul>
 */
public class EquipmentAttachmentDTO {
	private Long id;
	
	private Long equipmentId;
	
	private Byte attachmentType;
	
	private String contentType;
	
	private String contentUri;
	
	private String contentUrl;
	
	private Long creatorUid;
	
	private Timestamp createTime;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public Byte getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(Byte attachmentType) {
		this.attachmentType = attachmentType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentUri() {
		return contentUri;
	}

	public void setContentUri(String contentUri) {
		this.contentUri = contentUri;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	public Long getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
