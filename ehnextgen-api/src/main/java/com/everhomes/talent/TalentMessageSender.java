// @formatter:off
package com.everhomes.talent;

import com.everhomes.server.schema.tables.pojos.EhTalentMessageSenders;
import com.everhomes.util.StringHelper;

public class TalentMessageSender extends EhTalentMessageSenders {
	
	private static final long serialVersionUID = 6606439905944345838L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}