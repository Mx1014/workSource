// @formatter:off
package com.everhomes.visitorsys;

import java.util.List;

public interface VisitorSysActionProvider {

	void createVisitorSysAction(VisitorSysAction visitorSysAction);

	void updateVisitorSysAction(VisitorSysAction visitorSysAction);

	VisitorSysAction findVisitorSysActionById(Long id);

	List<VisitorSysAction> listVisitorSysAction();

	VisitorSysAction findVisitorSysActionByAction(Long visitorId, Byte actionType);

	List<VisitorSysAction> listVisitorSysActionByVisitorId(Long visitorId);
}