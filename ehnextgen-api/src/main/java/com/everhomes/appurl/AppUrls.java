package com.everhomes.appurl;

import com.everhomes.server.schema.tables.pojos.EhAppUrls;
import com.everhomes.util.StringHelper;

public class AppUrls extends EhAppUrls {

	private static final long serialVersionUID = -2544924865130025306L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
