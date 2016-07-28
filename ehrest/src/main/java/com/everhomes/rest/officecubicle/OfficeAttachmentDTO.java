package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>id: 附件ID</li>
 * <li>ownerId: office ID</li>
 * <li>contentType: 附件类型，{@link com.everhomes.rest.forum.PostContentType}</li>
 * <li>contentUri: 附件访问URI</li>
 * <li>contentUrl: 附件访问URl</li>
 * </ul>
 */
public class OfficeAttachmentDTO {
    private Long id;
    private Long ownerId;
    private String contentType;
    private String contentUri; 
    private String contentUrl;

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
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
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
