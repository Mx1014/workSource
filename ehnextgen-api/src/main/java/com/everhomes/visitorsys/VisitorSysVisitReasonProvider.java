// @formatter:off
package com.everhomes.visitorsys;

import java.util.List;

public interface VisitorSysVisitReasonProvider {

	void createVisitorSysVisitReason(VisitorSysVisitReason visitorSysVisitReason);

	void updateVisitorSysVisitReason(VisitorSysVisitReason visitorSysVisitReason);

	VisitorSysVisitReason findVisitorSysVisitReasonById(Long id);

	List<VisitorSysVisitReason> listVisitorSysVisitReason(Integer namespaceId,Byte communityType);

}