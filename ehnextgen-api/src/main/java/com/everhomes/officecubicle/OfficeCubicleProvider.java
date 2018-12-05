package com.everhomes.officecubicle;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.everhomes.community.Community;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleConfigs;

public interface OfficeCubicleProvider {
 

	List<OfficeCubicleCategory> queryCategoriesBySpaceId(Long id);

	void createSpace(OfficeCubicleSpace space);

	void createCategory(OfficeCubicleCategory category);

	OfficeCubicleSpace getSpaceById(Long id);

	void updateSpace(OfficeCubicleSpace space);

	void deleteCategoriesBySpaceId(Long id);

	List<OfficeCubicleOrder> searchOrders(String ownerType,Long ownerId,Long beginDate, Long endDate, String reserveKeyword, String spaceName,
			CrossShardListingLocator locator, Integer pageSize,Integer currentNamespaceId, Byte workFlowStatus);

	List<OfficeCubicleSpace> searchSpaces(String ownerType,Long ownerId,String keyWords, CrossShardListingLocator locator, int pageSize, Integer currentNamespaceId);

	void createOrder(OfficeCubicleOrder order);

	OfficeCubicleOrder getOrderById(Long orderId);

	void updateOrder(OfficeCubicleOrder order);

	List<OfficeCubicleSpace> querySpacesByCityId(String ownerType,Long ownerId,Long cityId, CrossShardListingLocator locator, int i, Integer currentNamespaceId);

	List<OfficeCubicleOrder> queryOrdersByUser(Long userId, Integer currentNamespaceId);

	List<OfficeCubicleOrder> listStationByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor,
			int pageSize);

	List<OfficeCubicleOrder> listStationByUpdateTime(Integer namespaceId, Long timestamp, int pageSize);


	List<OfficeCubicleSpace> listEmptyOwnerSpace();

    List<OfficeCubicleOrder> listEmptyOwnerOrders();

	List<OfficeCubicleSpace> listAllSpaces(long pageAnchor, int pageSize);

	List<OfficeCubicleSpace> querySpacesByCityName(String ownerType, Long ownerId, String provinceName, String cityName,
			CrossShardListingLocator locator, int i, Integer namespaceId);

	void updateSpaceByProvinceAndCity(Integer currentNamespaceId, String provinceName, String cityName);

	void createConfig(OfficeCubicleConfig bean);

	void updateConfig(OfficeCubicleConfig bean);

	OfficeCubicleConfig findConfigByOwnerId(String ownerType,Long ownerId);

	void createCubicleSite(OfficeCubicleStation station);

	List<OfficeCubicleStation> getOfficeCubicleStation(Long owner, String ownerType, Long spaceId, Long roomId, Byte rentFlag,String keyword, Byte status,Long stationId);
	
	List<OfficeCubicleRoom> getOfficeCubicleRoom(Long owner, String ownerType, Long spaceId,Byte rentFlag,Byte status, Long roomId);
	
	List<OfficeCubicleRentOrder> searchCubicleOrders(String ownerType, Long ownerId, Long beginDate, Long endDate,
			 CrossShardListingLocator locator, Integer pageSize, Integer currentNamespaceId,
			String paidType, Byte paidMode, Byte requestType, Byte rentType, Byte orderStatus);

	void createCubicleRoom(OfficeCubicleRoom room);

	void updateRoom(OfficeCubicleRoom room);

	void createCubicleRentOrder(OfficeCubicleRentOrder order);

	void createCubicleStationRent(OfficeCubicleStationRent stationRent);

	List<OfficeCubicleStationRent> searchCubicleStationRent(Long spaceId, Integer currentNamespaceId, Byte rentType,
			Byte stationType);

	void updateCubicleRentOrder(OfficeCubicleRentOrder order);

	OfficeCubicleRentOrder findOfficeCubicleRentOrderByBizOrderNum(String bizOrderNum);

	OfficeCubicleRentOrder findOfficeCubicleRentOrderById(Long orderId);

	void updateCubicle(OfficeCubicleStation station);

	void deleteRoom(OfficeCubicleRoom room);

	void deleteStation(OfficeCubicleStation station);

	void createAttachments(OfficeCubicleAttachment attachment);

	List<OfficeCubicleAttachment> listAttachmentsBySpaceId(Long id, Byte ownerType);

	OfficeCubicleSpace getSpaceByOwnerId(Long ownerId);

	BigDecimal getRoomMinPrice(Long spaceId);

	BigDecimal getRoomMaxPrice(Long spaceId);

	BigDecimal getStationMinPrice(Long spaceId);

	BigDecimal getStationMaxPrice(Long spaceId);

	List<OfficeCubicleStationRent> getOfficeCubicleStationRent(Long spaceId, Byte rentType,Byte stationType,Long staionId);

	void deleteChargeUsers(Long spaceId);

	void createChargeUsers(OfficeCubicleChargeUser user);

	List<OfficeCubicleRefundRule> findRefundRule(Long spaceId);

	void deleteRefundRule(Long spaceId);

	List<OfficeCubicleChargeUser> findChargeUserBySpaceId(Long spaceId);

	void createRefundRule(OfficeCubicleRefundRule refundRule);

	List<OfficeCubicleRefundRule> listRefundRuleBySpaceId(Long spaceId);

	OfficeCubicleStation getOfficeCubicleStationById(Long stationId);

	OfficeCubicleRoom getOfficeCubicleRoomById(Long roomId);

	Map<Long, Community> listCommunitiesByIds(List<Long> ids);

	void deleteAttachmentsBySpaceId(Long id, Byte type);

	List<OfficeCubicleStationRent> getOfficeCubicleStationRentByTime(Long spaceId, Byte rentType, Byte stationType,
			Long beginDate, Long endDate);

}
