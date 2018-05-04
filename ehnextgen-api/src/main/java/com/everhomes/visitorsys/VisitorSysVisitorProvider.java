// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.rest.visitorsys.BaseVisitorDTO;

import java.util.List;

public interface VisitorSysVisitorProvider {

	void createVisitorSysVisitor(VisitorSysVisitor visitorSysVisitor);

	void updateVisitorSysVisitor(VisitorSysVisitor visitorSysVisitor);

	VisitorSysVisitor findVisitorSysVisitorById(Long id);

	List<VisitorSysVisitor> listVisitorSysVisitor();

	List<VisitorSysVisitor> listVisitorSysVisitor(ListBookedVisitorParams params);
}