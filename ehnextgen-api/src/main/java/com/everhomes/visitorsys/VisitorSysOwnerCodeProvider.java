// @formatter:off
package com.everhomes.visitorsys;

import java.util.List;

public interface VisitorSysOwnerCodeProvider {

	void createVisitorSysOwnerCode(VisitorSysOwnerCode visitorSysOwnerCode);

	void updateVisitorSysOwnerCode(VisitorSysOwnerCode visitorSysOwnerCode);

	VisitorSysOwnerCode findVisitorSysOwnerCodeById(Long id);

	List<VisitorSysOwnerCode> listVisitorSysOwnerCode();

    VisitorSysOwnerCode findVisitorSysCodeByOwner(Integer namespaceId, String ownerType, Long ownerId);

	VisitorSysOwnerCode findVisitorSysCodingByRandomCode(String randomCode);
}