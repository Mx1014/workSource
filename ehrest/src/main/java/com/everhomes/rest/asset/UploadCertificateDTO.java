//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>uri:凭证图片uri</li>
 * <li>url:凭证图片url</li>
 *</ul>
 */
public class UploadCertificateDTO {
	
	private String uri;
	private String url;
	
	public UploadCertificateDTO() {

	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
