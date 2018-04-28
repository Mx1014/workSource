// @formatter:off
package com.everhomes.visitorsys;

import java.util.List;

public interface VisitorSysVisitorProvider {

	void createVisitorSysVisitor(VisitorSysVisitor visitorSysVisitor);

	void updateVisitorSysVisitor(VisitorSysVisitor visitorSysVisitor);

	VisitorSysVisitor findVisitorSysVisitorById(Long id);

	List<VisitorSysVisitor> listVisitorSysVisitor();

}