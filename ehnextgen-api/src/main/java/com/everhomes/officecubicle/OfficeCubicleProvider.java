package com.everhomes.officecubicle;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;

public interface OfficeCubicleProvider {
 

	List<OfficeCubicleCategory> queryCategoriesBySpaceId(Long id);

	void createSpace(OfficeCubicleSpace space);

	void createCategory(OfficeCubicleCategory category);

	OfficeCubicleSpace getSpaceById(Long id);

	void updateSpace(OfficeCubicleSpace space);

	void deleteCategoriesBySpaceId(Long id);

	List<OfficeCubicleOrder> searchOrders(Long beginDate, Long endDate, String reserveKeyword, String spaceName,
			CrossShardListingLocator locator, Integer pageSize,Integer currentNamespaceId );

	List<OfficeCubicleSpace> searchSpaces(String keyWords, CrossShardListingLocator locator, int pageSize, Integer currentNamespaceId);

	void createOrder(OfficeCubicleOrder order);

	OfficeCubicleOrder getOrderById(Long orderId);

	void updateOrder(OfficeCubicleOrder order);

	List<OfficeCubicleSpace> querySpacesByCityId(Long cityId, CrossShardListingLocator locator, int i, Integer currentNamespaceId);

	List<OfficeCubicleOrder> queryOrdersByUser(Long userId, Integer currentNamespaceId);

	void deleteAttachmentsBySpaceId(Long id);

	List<OfficeCubicleOrder> listStationByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor,
			int pageSize);

	List<OfficeCubicleOrder> listStationByUpdateTime(Integer namespaceId, Long timestamp, int pageSize); 

}
