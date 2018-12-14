package com.everhomes.rentalv2;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.rentalv2.MaxMinPrice;
import com.everhomes.rest.rentalv2.RentalStatisticsDTO;
import com.everhomes.server.schema.tables.pojos.EhRentalv2DayopenTime;

public interface Rentalv2Provider {

	void createRentalSite(RentalResource rentalSite);

	List<RentalItem> findRentalSiteItems(String sourceType,Long sourceId, String resourceType);

	void createRentalSiteItem(RentalItem siteItem);

	void createRentalSiteRule(RentalCell rsr);

	Long createRentalOrder(RentalOrder rentalBill);

	Long createRentalOrderStatistics(RentalOrderStatistics statistics);

	void deleteRentalOrderStatisticsByOrderId(Long orderId);

	Long createRentalItemBill(RentalItemsOrder rib);

	Long createRentalSiteBill(RentalResourceOrder rsb);

	List<RentalResourceOrder> findRentalSiteBillBySiteRuleId(Long siteRuleId, String resourceType);

	RentalResource getRentalSiteById(Long rentalSiteId);

	List<RentalItemsOrder> findRentalItemsBillBySiteBillId(Long rentalBillId, String resourceType);

	RentalItem findRentalSiteItemById(Long rentalSiteItemId);

	RentalItemsOrder findRentalItemBill(Long rentalBillId, Long rentalSiteItemId, String resourceType);

	RentalOrder findRentalBillById(Long rentalBillId);

	RentalOrder findRentalBillByOrderNo(String orderNo);

	void updateRentalSite(RentalResource rentalSite);

	void deleteRentalSiteItemById(Long rentalSiteItemId);

	void updateRentalBill(RentalOrder bill);

	Long createRentalBillAttachment(RentalOrderAttachment rba);

//	Long createRentalBillPaybillMap(RentalOrderPayorderMap billmap);

	List<RentalResourceOrder> findRentalResourceOrderByOrderId(Long id);

	List<RentalOrderAttachment> findRentalBillAttachmentByBillId(Long rentalBillId);

//	RentalOrderPayorderMap findRentalBillPaybillMapByOrderNo(String orderNo);

	List<RentalOrder> listRentalBills(Long id, Long userId, Long rentalSiteId,String resourceType, Long resourceTypeId, ListingLocator locator,
			int count, List<Byte> status, Byte payMode);

	List<RentalOrder> listRentalBills(Long resourceTypeId, Long organizationId,Long communityId, Long rentalSiteId, ListingLocator locator, Byte billStatus,
			String vendorType , Integer pageSize, Long startTime, Long endTime,
			Byte invoiceFlag,Long userId,String payChannel,Byte source);
	List<RentalOrder> listRentalBillsByUserOrgId(Long organizationId ,ListingLocator locator, Integer pageSize );
	List<RentalOrder> listActiveBills(Long rentalSiteId, ListingLocator locator,Integer pageSize, Long startTime, Long endTime,String resourceType);

	List<RentalOrder> listActiveBillsByInterval(Long rentalSiteId,Long startTime, Long endTime);

	List<RentalOrder> listTargetRentalBills(Byte[] status);
	
	List<RentalResource> findRentalSites(Long resourceTypeId, String keyword, ListingLocator locator,
			Integer pageSize, Byte status,List<Long>  siteIds,Long communityId);

	List<RentalResource> findRentalSitesByCommunityId(String resouceType,Long resourceTypeId,Long communityId);

	List<RentalOrder> listRentalBills(Long ownerId, String ownerType,
			String siteType, Long rentalSiteId, Long beginDate, Long endDate);

	List<RentalSiteRange> findRentalSiteOwnersByOwnerTypeAndId(String resourceType, String ownerType, Long ownerId);

	List<RentalResourcePic> findRentalSitePicsByOwnerTypeAndId(String resourceType, String ownerType, Long ownerId);

	List<RentalResourceFile> findRentalSiteFilesByOwnerTypeAndId(String resourceType, String ownerType, Long ownerId);

	RentalItem getRentalSiteItemById(Long id);

    void setAuthDoorId(Long orderId,String AuthDoorId);

	void createRentalDefaultRule(RentalDefaultRule defaultRule);

	void createTimeInterval(RentalTimeInterval timeInterval);

	void createRentalCloseDate(RentalCloseDate rcd);

	void createRentalDayopenTime(RentalDayopenTime rdt);

	void createRentalConfigAttachment(RentalConfigAttachment rca);

	RentalDefaultRule getRentalDefaultRule(String ownerType, Long ownerId, String resourceType, Long resourceTypeId,
										   String sourceType, Long sourceId);

	List<RentalTimeInterval> queryRentalTimeIntervalByOwner(String resourceType, String ownerType,
			Long ownerId);

	List<RentalCloseDate> queryRentalCloseDateByOwner(String resourceType, String ownerType, Long ownerId, Date startTime, Date endTime);

	List<RentalConfigAttachment> queryRentalConfigAttachmentByOwner(String resourceType, String ownerType, Long ownerId,Byte attachmentType);

	List<RentalConfigAttachment> queryRentalConfigAttachmentByIds(List<Long> ids);

	List<RentalDayopenTime> queryRentalDayopenTimeByOwner(String resourceType, String ownerType, Long ownerId,Byte rentalType);

	void createRentalSiteOwner(RentalSiteRange siteOwner);

	void createRentalSitePic(RentalResourcePic detailPic);

	void createRentalSiteFile(RentalResourceFile file);

	void deleteRentalSitePicsBySiteId(String resourceType, Long siteId);

	void deleteRentalSiteFilesBySiteId(String resourceType, Long siteId);

	void deleteRentalSiteOwnersBySiteId(String resourceType, Long siteId);

	void updateRentalSiteItem(RentalItem siteItem);

	Integer countRentalSiteItemSoldCount(Long itemId);

	void updateRentalSiteRule(RentalCell rsr);

	void updateRentalDefaultRule(RentalDefaultRule newDefaultRule);

	Integer deleteTimeIntervalsByOwnerId(String resourceType, String ownerType, Long id);

	Integer deleteRentalCloseDatesByOwnerId(String resourceType, String ownerType, Long id);

	Integer deleteRentalConfigAttachmentsByOwnerId(String resourceType, String ownerType, Long id,Byte attachmentType);

	Integer deleteRentalDayopenTimeByOwnerId(String resourceType, String ownerType, Long id);

	List<RentalSiteRange> findRentalSiteOwnersBySiteId(String resourceType, Long siteId);

	Integer countRentalSiteItemRentalCount(List<Long> rentalBillIds);

//	List<RentalOrderPayorderMap> findRentalBillPaybillMapByBillId(Long id);

	Long createRentalRefundOrder(RentalRefundOrder rentalRefundOrder);

	void updateRentalRefundOrder(RentalRefundOrder rentalRefundOrder);

	List<RentalRefundOrder> getRefundOrderList(String resourceType, Long resourceTypeId,
			CrossShardListingLocator locator, Byte status, String styleNo,
			int pageSize, Long startTime, Long endTime);

	RentalRefundOrder getRentalRefundOrderById(Long rentalRefundOrderId);

	RentalRefundOrder getRentalRefundOrderByRefundNo(String refundOrderNo);

	RentalRefundOrder getRentalRefundOrderByOrderNo(String orderNo);

	RentalResourceType getRentalResourceTypeById(Long rentalResourceTypeId);

	void createRentalResourceType(RentalResourceType rentalResourceType);

	void updateRentalResourceType(RentalResourceType resourceType);

	List<RentalResourceType> findRentalResourceTypes(Integer namespaceId, Byte menuType, String resourceType,
													 ListingLocator locator);

	RentalResourceType findRentalResourceTypes(Integer namespaceId, String resourceType);

	void createRentalResourceNumber(RentalResourceNumber resourceNumber);

	Integer deleteRentalResourceNumbersByOwnerId(String resourceType, String ownerType, Long ownerId);

	List<RentalResourceNumber> queryRentalResourceNumbersByOwner(String resourceType, String ownerType, Long ownerId);

//	void updateRentalOrderPayorderMap(RentalOrderPayorderMap orderMap);

	RentalCell getRentalCellById(Long cellId,Long rentalSiteId,Byte rentalType,String resourceType);
 
	void deleteRentalCellsByResourceId(String resourceType, Long rentalSiteId);

	List<RentalOrder> listSiteRentalByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor, int pageSize);

	List<RentalOrder> listSiteRentalByUpdateTime(Integer namespaceId, Long timestamp, int pageSize);

	List<RentalOrder> findRentalSiteBillBySiteRuleIds(List<Long> siteRuleIds);

	List<RentalCell> getRentalCellsByIds(String resourceType,List<Long> cellIds, Long rentalSiteId,Byte rentalType);

	List<RentalCell> getRentalCellsByRange(String resourceType,Long minId,Long maxId, Long rentalSiteId,Byte rentalType);

	Double countRentalSiteBillBySiteRuleId(Long cellId,RentalResource rentalResource,Byte rentalType);

	Double countRentalSiteBillOfAllScene(RentalResource rentalResource, RentalCell rentalCell, List<Rentalv2PriceRule> priceRules);

	List<RentalResourceOrder> findAllRentalSiteBillByTime(RentalResource rentalResource,Long beginTime,Long endTime);

	MaxMinPrice findMaxMinPrice(String resourceType,Long ownerId, Byte rentalType);

	MaxMinPrice findMaxMinPriceByClassifycation(String resourceType,String ownerType,List<Long> ownerIds,
												String sourceType,Long sourceId,Byte userPriceType,String classification);

	List<RentalPriceClassification> listClassification(String resourceType,String ownerType,Long ownerId,
													   String sourceType,Long sourceId,Byte userPriceType,String classification);

	void deleteClassificationBySourceId(String resourceType,String sourceType,Long sourceId);

	void deleteClassificationByOwnerId(String resourceType,String ownerType,Long ownerId);

	boolean findOtherModeClosed(RentalResource rentalResource, RentalCell rentalCell,
			List<Rentalv2PriceRule> priceRules);

	List<RentalCell> findCellClosedByTimeInterval(String resourceType,Long rentalSiteId,Long startTime,Long endTime);

	RentalResourceType findRentalResourceTypeById(Long resourceTypeId);

	List<Long> listCellPackageId (String resourceType, Long ownerId, Byte rentalType);

	List<Long> listCellId (String resourceType, Long ownerId, Byte rentalType);

	void createRentalOrderRule(RentalOrderRule rule);

	void deleteRentalOrderRules(String resourceType, String ownerType, Long ownerId, Byte handleType);

	List<RentalOrderRule> listRentalOrderRules(String resourceType, String ownerType, Long ownerId, Byte handleType);

	List<RentalOrder> searchRentalOrders(Long resourceTypeId, String resourceType, Long rentalSiteId, Byte billStatus,
										 Long startTime, Long endTime,String tag1, String tag2,String vendorType,String keyword, Long pageAnchor ,
										 Integer pageSize);
	BigDecimal getRentalOrdersTotalAmount(Long resourceTypeId, String resourceType, Long rentalSiteId, Byte billStatus,
										  Long startTime, Long endTime,String tag1, String tag2,String keyword);

	List<String> listOverTimeSpaces(Integer namespaceId, Long resourceTypeId, String resourceType,
									Long rentalSiteId);

	List<RentalOrder> listOverTimeRentalOrders(Integer namespaceId, Long resourceTypeId, String resourceType,
											   Long rentalSiteId, String spaceNo);
	List<String> listParkingNoInUsed(Integer namespaceId, Long resourceTypeId, String resourceType,
									 Long rentalSiteId,List<Long> cellIds);

	BigDecimal countRentalBillAmount(String resourceType,Long resourceTypeId,Long communityId,Long startTime, Long endTime,Long rentalSiteId,Long orgId);

	List<RentalStatisticsDTO> listRentalBillAmountByOrgId(String resourceType, Long resourceTypeId, Long communityId,
														  Long startTime, Long endTime, Integer order);

	Integer countRentalBillNum(String resourceType,Long resourceTypeId,Long communityId,Long startTime, Long endTime,Long rentalSiteId,Long orgId);

	List<RentalStatisticsDTO> listRentalBillNumByOrgId(String resourceType, Long resourceTypeId,Long communityId,
													   Long startTime, Long endTime,Integer order);

	Long countRentalBillValidTime(String resourceType,Long resourceTypeId,Long communityId,Long startTime, Long endTime,Long rentalSiteId,Long orgId);

	List<RentalStatisticsDTO> listRentalBillValidTimeByOrgId(String resourceType, Long resourceTypeId,Long communityId,
															 Long startTime, Long endTime,Integer order);
	String getHolidayCloseDate (Byte holidayType);

	void createRefundTip(RentalRefundTip tip);

	void deleteRefundTip(String resourceType, String sourceType, Long sourceId);

	List<RentalRefundTip> listRefundTips(String resourceType, String sourceType, Long sourceId,Byte refundStrategy);

	List<RentalStructure> listRentalStructures(String sourceType,Long sourceId,String resourceType,Byte isSurport,ListingLocator locator, Integer pageSize);

	List<RentalStructureTemplate> listRentalStructureTemplates();

	RentalStructure getRentalStructureById(Long id);

	void createRentalStructure(RentalStructure rentalStructure);

	void updateRentalStructure(RentalStructure rentalStructure);

	RentalOrder getUserClosestBill(Long userId,Long resourceTypeId);
}
