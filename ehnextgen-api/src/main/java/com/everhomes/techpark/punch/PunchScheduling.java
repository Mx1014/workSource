package com.everhomes.techpark.punch;

import com.everhomes.server.schema.tables.pojos.EhPunchSchedulings;
import com.everhomes.util.StringHelper;

public class PunchScheduling extends EhPunchSchedulings {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3106989681273385968L;

	/**
	 * @author Wuhan
	 */ 

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
