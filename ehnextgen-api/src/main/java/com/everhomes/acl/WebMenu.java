package com.everhomes.acl;

import com.everhomes.server.schema.tables.pojos.EhWebMenus;
import com.everhomes.util.StringHelper;

public class WebMenu extends EhWebMenus {
	
	private static final long serialVersionUID = -1852518988310908484L;

	public WebMenu() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
