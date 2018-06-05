// @formatter:off
package com.everhomes.aclink;

import com.everhomes.server.schema.tables.pojos.EhFaceRecognitionPhotos;
import com.everhomes.util.StringHelper;

public class FaceRecognitionPhoto extends EhFaceRecognitionPhotos {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7536887322075975280L;

	public FaceRecognitionPhoto(){
		
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
