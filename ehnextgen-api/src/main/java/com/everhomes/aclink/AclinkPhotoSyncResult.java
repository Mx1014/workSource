// @formatter:off
package com.everhomes.aclink;

import com.everhomes.server.schema.tables.pojos.EhAclinkPhotoSyncResult;
import com.everhomes.util.StringHelper;

public class AclinkPhotoSyncResult extends EhAclinkPhotoSyncResult {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3888745889773246144L;
	public AclinkPhotoSyncResult() {
		
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
