package com.everhomes.link;

import com.everhomes.server.schema.tables.pojos.EhLinks;
import com.everhomes.util.StringHelper;

public class Link extends EhLinks{
	private static final long serialVersionUID = -7650011925665954137L;

	public Link() {
	}
	
	 @Override
	    public String toString() {
	        return StringHelper.toJsonString(this);
	    }
	
}
