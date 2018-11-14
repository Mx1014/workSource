package com.everhomes.yellowPage.standard;

import java.util.List;

public interface ServiceCategoryMatchProvider {

	void createMatch(ServiceCategoryMatch tmp);

	List<ServiceCategoryMatch> listMatches(String ownerType, Long ownerId, Long type);

	void deleteMathes(String code, Long projectId, Long type);

	ServiceCategoryMatch findMatch(String ownerType, Long ownerId, Long type, Long serviceId);
	
	void updateMatch(ServiceCategoryMatch tmp);

	void updateMatchCategoryName(Long type, Long categoryId, String categoryName);
}
