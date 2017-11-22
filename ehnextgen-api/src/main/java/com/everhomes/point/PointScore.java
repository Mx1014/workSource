// @formatter:off
package com.everhomes.point;

import com.everhomes.server.schema.tables.pojos.EhPointScores;
import com.everhomes.util.StringHelper;

public class PointScore extends EhPointScores {
	
	private static final long serialVersionUID = -4971163021928402978L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}