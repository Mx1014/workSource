// @formatter:off
package com.everhomes.news;

import com.everhomes.server.schema.tables.pojos.EhNews;
import com.everhomes.server.schema.tables.pojos.EhNewsCommunities;
import com.everhomes.util.StringHelper;

public class NewsCommunity extends EhNewsCommunities{
	private static final long serialVersionUID = -931770711864372078L;

	public NewsCommunity() {
		super();
	}

	public NewsCommunity(Long id){
		setId(id);
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
