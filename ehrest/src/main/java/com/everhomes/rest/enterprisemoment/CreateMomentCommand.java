// @formatter:off
package com.everhomes.rest.enterprisemoment;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>content: 内容</li>
 * <li>scopes: 可见范围列表 参考{@link com.everhomes.rest.enterprisemoment.MomentScopeDTO}</li>
 * <li>contentType: 内容类型， 目前都是text，参考{@link com.everhomes.rest.enterprisemoment.ContentType}</li>
 * <li>tagId: 标签id</li>
 * <li>longitude: 经度</li>
 * <li>latitude: 纬度</li>
 * <li>location: 坐标信息</li>
 * <li>attachments: 附件列表 目前都是图片 最多九张可能为空  参考{@link com.everhomes.rest.enterprisemoment.MommentAttachmentDTO}</li>
 * </ul>
 */
public class CreateMomentCommand {
	private Long organizationId;
	private List<MomentScopeDTO> scopes;
    private String content;
    private String contentType;
    private Long tagId;
    private Double longitude;
    private Double latitude;
    private String location;
    private List<MommentAttachmentDTO> attachments;


	public CreateMomentCommand() {
		this.attachments = new ArrayList<>();
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public List<MomentScopeDTO> getScopes() {
		return scopes;
	}

	public void setScopes(List<MomentScopeDTO> scopes) {
		this.scopes = scopes;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<MommentAttachmentDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<MommentAttachmentDTO> attachments) {
		this.attachments = attachments;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
