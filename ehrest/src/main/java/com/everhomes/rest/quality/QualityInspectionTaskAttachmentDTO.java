package com.everhomes.rest.quality;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 任务主键id</li>
 *  <li>recordId: 记录id</li>
 *  <li>contentType: 附件类型IMAGE("image"): 图片 AUDIO("audio"): 音频 VIDEO("video"): 视频 </li>
 *  <li>contentUri: 附件访问URI</li>
 *  <li>contentUrl: 附件访问URL</li>
 *  <li>creatorUid: 创建人id</li>
 *  <li>createTime: 创建时间</li>
 * </ul>
 */
public class QualityInspectionTaskAttachmentDTO {

	private Long id;
	
	private Long recordId;
	
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

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
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
