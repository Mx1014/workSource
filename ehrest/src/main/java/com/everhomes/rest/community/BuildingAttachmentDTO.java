package com.everhomes.rest.community;

/**
 * <ul>
 * <li>id: 附件ID</li>
 * <li>buildingId: 楼栋ID</li>
 * <li>contentType: 附件类型，{@link com.everhomes.rest.forum.PostContentType}</li>
 * <li>contentUri: 附件访问URI</li>
 * </ul>
 */
public class BuildingAttachmentDTO {
	
	private Long id;
	    
    private Long buildingId;
    
    private String contentType;
    
    private String contentUri;
    
    private String contentUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
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

}
