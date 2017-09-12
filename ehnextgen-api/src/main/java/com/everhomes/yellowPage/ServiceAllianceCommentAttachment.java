// @formatter:off
package com.everhomes.yellowPage;

import com.everhomes.server.schema.tables.pojos.EhServiceAllianceCommentAttachments;
import com.everhomes.util.StringHelper;

public class ServiceAllianceCommentAttachment extends EhServiceAllianceCommentAttachments {
	
	private static final long serialVersionUID = 7733126232900067580L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}