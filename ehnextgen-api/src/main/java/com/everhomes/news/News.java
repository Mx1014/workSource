// @formatter:off
package com.everhomes.news;

import com.everhomes.server.schema.tables.pojos.EhNews;
import com.everhomes.util.StringHelper;

public class News extends EhNews{
	private static final long serialVersionUID = -931770711864372078L;
	
	public News() {
		super();
	}
	
	public News(Long id){
		setId(id);
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
