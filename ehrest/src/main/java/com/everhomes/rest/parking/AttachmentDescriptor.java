// @formatter:off
package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>informationType: 资料类型 {@link com.everhomes.rest.parking.ParkingRequestContentType}</li>
 * <li>contentType: 附件类型，{@link com.everhomes.rest.parking.ParkingContentType}</li>
 * <li>contentUri: 附件访问URI</li>
 * <li>contentUrl: 附件访问URL</li>
 * </ul>
 */
public class AttachmentDescriptor {
	
	private Byte informationType;
	
    private String contentType;
    
    private String contentUri;
    
    private String contentUrl;
    
    public AttachmentDescriptor() {
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

	public Byte getInformationType() {
		return informationType;
	}

	public void setInformationType(Byte informationType) {
		this.informationType = informationType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
