package com.everhomes.statistics.terminal;


import com.everhomes.server.schema.tables.pojos.EhTerminalAppVersionStatistics;
import com.everhomes.util.StringHelper;

public class TerminalAppVersionStatistics extends EhTerminalAppVersionStatistics {

	/**
	 * 
	 */
	private static final long serialVersionUID = 953109037994486870L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
