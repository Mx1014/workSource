// @formatter:off
package com.everhomes.visitorsys;

import java.sql.Timestamp;
import java.util.List;

public interface VisitorSysVisitorProvider {

	void createVisitorSysVisitor(VisitorSysVisitor visitorSysVisitor);

	void updateVisitorSysVisitor(VisitorSysVisitor visitorSysVisitor);

	VisitorSysVisitor findVisitorSysVisitorById(Integer namespaceId, Long id);

	VisitorSysVisitor findVisitorSysVisitorById(Long id);

	List<VisitorSysVisitor> listVisitorSysVisitor();

	List<VisitorSysVisitor> listVisitorSysVisitor(ListBookedVisitorParams params);

    VisitorSysVisitor findVisitorSysVisitorByParentId(Integer namespaceId, Long id);

    void deleteVisitorSysVisitor(Integer namespaceId, Long visitorId);

    void deleteVisitorSysVisitorAppoint(Integer namespaceId, Long visitorId);

	List<VisitorSysVisitor> listVisitorSysVisitorByVisitorPhone(Integer namespaceId, String ownerType, Long ownerId, String visitorPhone, Timestamp startOfDay, Timestamp endOfDay);

	List<VisitorSysVisitor> listFreqVisitor();
}