package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;
/**
 * <li>contentType: 附件类型，{@link com.everhomes.rest.forum.PostContentType}</li>
 * <li>contentUri: 附件访问URI</li>
 * <li>contentUrl: 附件访问URl</li>
 * 
 * 
 * */
public class YellowPageAattchmentDTO {

	private java.lang.Long     id;
	private java.lang.Long     ownerId;
	private java.lang.String   contentType;
	private java.lang.String   contentUri; 
	private String contentUrl;
	
	


	public java.lang.Long getId() {
		return id;
	}




	public void setId(java.lang.Long id) {
		this.id = id;
	}




	public java.lang.Long getOwnerId() {
		return ownerId;
	}




	public void setOwnerId(java.lang.Long ownerId) {
		this.ownerId = ownerId;
	}




	public java.lang.String getContentType() {
		return contentType;
	}




	public void setContentType(java.lang.String contentType) {
		this.contentType = contentType;
	}




	public java.lang.String getContentUri() {
		return contentUri;
	}




	public void setContentUri(java.lang.String contentUri) {
		this.contentUri = contentUri;
	}




	public java.lang.String getContentUrl() {
		return contentUrl;
	}




	public void setContentUrl(java.lang.String contentUrl) {
		this.contentUrl = contentUrl;
	}




	@Override
	    public String toString() {
	        return StringHelper.toJsonString(this);
	    }
}
