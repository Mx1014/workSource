// @formatter:off
package com.everhomes.broadcast;

import java.util.List;

public interface BroadcastProvider {

	void createBroadcast(Broadcast broadcast);

	void updateBroadcast(Broadcast broadcast);

	Broadcast findBroadcastById(Long id);

	List<Broadcast> listBroadcast();

	List<Broadcast> listBroadcastByOwner(String ownerType, Long ownerId, Long pageAnchor, int pageSize);

	Integer selectBroadcastCountToday(Integer namespaceId, String ownerType, Long ownerId);

    void deleteBroadcast(Broadcast broadcast);
}