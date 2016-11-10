package com.everhomes.dbsync;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSDataQueryObject extends HashMap<String, List<JSQueryObjectItem>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2314704224665432464L;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
