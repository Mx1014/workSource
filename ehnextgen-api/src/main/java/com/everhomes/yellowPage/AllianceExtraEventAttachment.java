// @formatter:off
package com.everhomes.yellowPage;

import com.everhomes.server.schema.tables.pojos.EhAllianceExtraEventAttachment;
import com.everhomes.util.StringHelper;

public class AllianceExtraEventAttachment extends EhAllianceExtraEventAttachment {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8888737830200352321L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}