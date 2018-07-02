package com.everhomes.rest.wx;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li> mediaId: 文件的id</li>
 * </ul>
 */
public class GetContentServerUriCommand {

	private String mediaId;

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
