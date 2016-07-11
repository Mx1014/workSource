package com.everhomes.videoconf;

import com.everhomes.server.schema.tables.pojos.EhConfInvoices;
import com.everhomes.util.StringHelper;

public class ConfInvoices extends EhConfInvoices {

	private static final long serialVersionUID = 4096481203198927514L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
