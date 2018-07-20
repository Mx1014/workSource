// @formatter:off
package com.everhomes.visitorsys;

import java.util.List;

public interface VisitorSysMessageReceiverProvider {

	void createVisitorSysMessageReceiver(VisitorSysMessageReceiver visitorSysMessageReceiver);

	void updateVisitorSysMessageReceiver(VisitorSysMessageReceiver visitorSysMessageReceiver);

	VisitorSysMessageReceiver findVisitorSysMessageReceiverById(Long id);

	List<VisitorSysMessageReceiver> listVisitorSysMessageReceiver();

    VisitorSysMessageReceiver findMessageReceiverByOwner(Integer namespaceId, String ownerType, Long ownerId, Long id);

	void deleteMessageReceiverByOwner(Integer namespaceId, String ownerType, Long ownerId, Long id);

	List<VisitorSysMessageReceiver> listVisitorSysMessageReceiverByOwner(Integer namespaceId, String ownerType, Long ownerId);
}