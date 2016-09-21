package com.everhomes.user;

import com.everhomes.server.schema.tables.pojos.EhSearchTypes;
import com.everhomes.util.StringHelper;

public class SearchTypes extends EhSearchTypes {

	private static final long serialVersionUID = -7947501191100859535L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
