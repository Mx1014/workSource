package com.everhomes.rest.techpark.rental;

import com.everhomes.util.StringHelper;


import com.everhomes.util.StringHelper;
/**
 * <ul>
*<li>id：id</li>
 * <li>uri：图片uri</li>
 * <li>url：图片url</li> 
 * </ul>
 */
public class RentalSitePicDTO {
	private java.lang.Long   id;
	private java.lang.String uri;
	private java.lang.String url;
	
	
	public java.lang.Long getId() {
		return id;
	}


	public void setId(java.lang.Long id) {
		this.id = id;
	}


	public java.lang.String getUri() {
		return uri;
	}


	public void setUri(java.lang.String uri) {
		this.uri = uri;
	}


	public java.lang.String getUrl() {
		return url;
	}


	public void setUrl(java.lang.String url) {
		this.url = url;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
