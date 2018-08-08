// @formatter:off
package com.everhomes.visitorsys;

import java.util.List;

public interface VisitorSysBlackListProvider {

	void createVisitorSysBlackList(VisitorSysBlackList visitorSysBlackList);

	void updateVisitorSysBlackList(VisitorSysBlackList visitorSysBlackList);

	VisitorSysBlackList findVisitorSysBlackListById(Long id);

	List<VisitorSysBlackList> listVisitorSysBlackList();

	List<VisitorSysBlackList> listVisitorSysBlackList(Integer namespaceId, String ownerType, Long ownerId, String keyWords, Integer pageSize, Long pageAnchor);

	void deleteBlackListById(Integer namespaceId, Long id);

    VisitorSysBlackList findVisitorSysBlackListByPhone(Integer namespaceId, String ownerType, Long ownerId, String visitorPhone);
}