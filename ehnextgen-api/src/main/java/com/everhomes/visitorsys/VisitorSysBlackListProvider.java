// @formatter:off
package com.everhomes.visitorsys;

import java.util.List;

public interface VisitorSysBlackListProvider {

	void createVisitorSysBlackList(VisitorSysBlackList visitorSysBlackList);

	void updateVisitorSysBlackList(VisitorSysBlackList visitorSysBlackList);

	VisitorSysBlackList findVisitorSysBlackListById(Long id);

	List<VisitorSysBlackList> listVisitorSysBlackList();

}