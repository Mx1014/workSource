// @formatter:off
package com.everhomes.visitorsys;

import java.util.List;

public interface VisitorSysCodingProvider {

	void createVisitorSysCoding(VisitorSysCoding visitorSysCoding);

	void updateVisitorSysCoding(VisitorSysCoding visitorSysCoding);

	VisitorSysCoding findVisitorSysCodingById(Long id);

	List<VisitorSysCoding> listVisitorSysCoding();

	VisitorSysCoding findVisitorSysCodingByOwner(Integer namespaceId, String ownerType, Long ownerId,String dayRemark);

//	VisitorSysCoding findVisitorSysCodingByRandomCode(String randomCode);
}