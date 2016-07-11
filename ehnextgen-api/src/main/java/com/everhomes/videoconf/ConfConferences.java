package com.everhomes.videoconf;

import com.everhomes.server.schema.tables.pojos.EhConfConferences;
import com.everhomes.util.StringHelper;

public class ConfConferences extends EhConfConferences {

	private static final long serialVersionUID = -9139343044072551387L;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
