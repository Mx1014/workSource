package com.everhomes.yellowPage;

import java.util.List;

import com.everhomes.listing.ListingLocator;

public interface AllianceOnlineServiceProvider {

	AllianceOnlineService getOnlineServiceByUserId(Long serviceAllianceId, Long userId);

	void updateOnlineService(AllianceOnlineService onlineService);

	void createOnlineService(AllianceOnlineService onlineService);

	List<AllianceOnlineService> getOnlineServiceList(Long serviceAllianceId);

}
