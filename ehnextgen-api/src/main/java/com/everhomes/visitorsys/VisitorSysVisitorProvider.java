// @formatter:off
package com.everhomes.visitorsys;

import java.util.List;

public interface VisitorSysVisitorProvider {

	void createVisitorSysVisitor(VisitorSysVisitor visitorSysVisitor);

	void updateVisitorSysVisitor(VisitorSysVisitor visitorSysVisitor);

	VisitorSysVisitor findVisitorSysVisitorById(Integer namespaceId, Long id);

	List<VisitorSysVisitor> listVisitorSysVisitor();

	List<VisitorSysVisitor> listVisitorSysVisitor(ListBookedVisitorParams params);

    VisitorSysVisitor findVisitorSysVisitorByParentId(Integer namespaceId, Long id);

    void deleteVisitorSysVisitor(Integer namespaceId, Long visitorId);

    void deleteVisitorSysVisitorAppoint(Integer namespaceId, Long visitorId);
}