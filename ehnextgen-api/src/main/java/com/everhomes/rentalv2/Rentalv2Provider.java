package com.everhomes.rentalv2;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.rentalv2.MaxMinPrice;

public interface Rentalv2Provider {

	void createRentalSite(RentalResource rentalsite);

	List<RentalItem> findRentalSiteItems(Long rentalSiteId);

	void createRentalSiteItem(RentalItem siteItem);

	void createRentalSiteRule(RentalCell rsr);

	Long createRentalOrder(RentalOrder rentalBill);

	Long createRentalItemBill(RentalItemsOrder rib);

	Long createRentalSiteBill(RentalResourceOrder rsb);

	List<RentalResourceOrder> findRentalSiteBillBySiteRuleId(Long siteRuleId);

	RentalResource getRentalSiteById(Long rentalSiteId);

	List<RentalItemsOrder> findRentalItemsBillBySiteBillId(Long rentalBillId);

	RentalItem findRentalSiteItemById(Long rentalSiteItemId);

	RentalItemsOrder findRentalItemBill(Long rentalBillId, Long rentalSiteItemId);

	RentalOrder findRentalBillById(Long rentalBillId);

	void updateRentalSite(RentalResource rentalsite);

	void deleteRentalSiteItemById(Long rentalSiteItemId);

	Double sumRentalRuleBillSumCounts(Long siteRuleId);

	void updateRentalBill(RentalOrder bill);

	Long createRentalBillAttachment(RentalOrderAttachment rba);

	Long createRentalBillPaybillMap(RentalOrderPayorderMap billmap);

	List<RentalResourceOrder> findRentalResourceOrderByOrderId(Long id);

	List<RentalOrderAttachment> findRentalBillAttachmentByBillId(Long rentalBillId);

	RentalOrderPayorderMap findRentalBillPaybillMapByOrderNo(String orderNo);

	List<RentalOrder> listRentalBills(Long id, Long userId, String resourceType, Long resourceTypeId, ListingLocator locator,
			int count, List<Byte> status, Byte payMode);

	List<RentalOrder> listRentalBills(Long resourceTypeId, Long organizationId, Long rentalSiteId, ListingLocator locator, Byte billStatus,
			String vendorType , Integer pageSize, Long startTime, Long endTime,
			Byte invoiceFlag,Long userId);

	List<RentalOrder> listSuccessRentalBills();
	
	List<RentalResource> findRentalSites(Long  resourceTypeId, String keyword, ListingLocator locator,
			Integer pageSize, Byte status,List<Long>  siteIds,Long communityId);

	List<RentalOrder> listRentalBills(Long ownerId, String ownerType,
			String siteType, Long rentalSiteId, Long beginDate, Long endDate);

	List<RentalSiteRange> findRentalSiteOwnersByOwnerTypeAndId(
			String ownerType, Long ownerId);

	List<RentalResourcePic> findRentalSitePicsByOwnerTypeAndId(String ownerType, Long ownerId);

	RentalItem getRentalSiteItemById(Long id);

    void setAuthDoorId(Long rentalId,Long AuthDoorId);

	void createRentalDefaultRule(RentalDefaultRule defaultRule);

	void createTimeInterval(RentalTimeInterval timeInterval);

	void createRentalCloseDate(RentalCloseDate rcd);

	void createRentalConfigAttachment(RentalConfigAttachment rca);

	RentalDefaultRule getRentalDefaultRule(String ownerType, Long ownerId, String resourceType, Long resourceTypeId,
										   String sourceType, Long sourceId);

	List<RentalTimeInterval> queryRentalTimeIntervalByOwner(String ownerType,
			Long ownerId);

	List<RentalCloseDate> queryRentalCloseDateByOwner(String ownerType, Long ownerId);

	List<RentalConfigAttachment> queryRentalConfigAttachmentByOwner(String ownerType, Long ownerId);

	List<RentalConfigAttachment> queryRentalConfigAttachmentByIds(List<Long> ids);

	void createRentalSiteOwner(RentalSiteRange siteOwner);

	void createRentalSitePic(RentalResourcePic detailPic);

	void deleteRentalSitePicsBySiteId(Long siteId);

	void deleteRentalSiteOwnersBySiteId(Long siteId);

	void updateRentalSiteItem(RentalItem siteItem);

	Integer countRentalSiteItemSoldCount(Long itemId);

	void updateRentalSiteRule(RentalCell rsr);

	void updateRentalDefaultRule(RentalDefaultRule newDefaultRule);

	Integer deleteTimeIntervalsByOwnerId(String ownerType, Long id);

	Integer deleteRentalCloseDatesByOwnerId(String ownerType, Long id);

	Integer deleteRentalConfigAttachmentsByOwnerId(String ownerType, Long id);

	List<RentalSiteRange> findRentalSiteOwnersBySiteId(Long siteId);

	Integer countRentalSiteItemRentalCount(List<Long> rentalBillIds);

	List<RentalOrderPayorderMap> findRentalBillPaybillMapByBillId(Long id);

	Long createRentalRefundOrder(RentalRefundOrder rentalRefundOrder);

	void updateRentalRefundOrder(RentalRefundOrder rentalRefundOrder);

	List<RentalRefundOrder> getRefundOrderList(Long resourceTypeId,
			CrossShardListingLocator locator, Byte status, String styleNo,
			int pageSize, Long startTime, Long endTime);

	RentalRefundOrder getRentalRefundOrderById(Long rentalRefundOrderId);

	RentalRefundOrder getRentalRefundOrderByRefoundNo(String refundOrderNo);

	RentalResourceType getRentalResourceTypeById(Long rentalResourceTypeId);

	void createRentalResourceType(RentalResourceType rentalResourceType);

	void updateRentalResourceType(RentalResourceType resourceType);

	List<RentalResourceType> findRentalResourceTypes(Integer namespaceId, Byte menuType, ListingLocator locator);

	void createRentalResourceNumber(RentalResourceNumber resourceNumber);

	Integer deleteRentalResourceNumbersByOwnerId(String simpleName, Long id);

	List<RentalResourceNumber> queryRentalResourceNumbersByOwner(
			String simpleName, Long id);

	void updateRentalOrderPayorderMap(RentalOrderPayorderMap orderMap);

	RentalCell getRentalCellById(Long cellId);
 
	void deleteRentalCellsByResourceId(String resourceType, Long rentalSiteId);

	List<RentalOrder> listSiteRentalByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor, int pageSize);

	List<RentalOrder> listSiteRentalByUpdateTime(Integer namespaceId, Long timestamp, int pageSize);

	List<RentalOrder> findRentalSiteBillBySiteRuleIds(List<Long> siteRuleIds);

	List<RentalCell> getRentalCellsByIds(List<Long> cellIds);

	Double countRentalSiteBillBySiteRuleId(Long cellId);

	Double countRentalSiteBillOfAllScene(RentalResource rentalResource, RentalCell rentalCell, List<Rentalv2PriceRule> priceRules);

	MaxMinPrice findMaxMinPrice(Long ownerId, Byte rentalType);

	boolean findOtherModeClosed(RentalResource rentalResource, RentalCell rentalCell,
			List<Rentalv2PriceRule> priceRules);

	RentalResourceType findRentalResourceTypeById (Long resourceTypeId);

	List<Long> listCellPackageId (Long ownerId, Byte rentalType);

	void createRentalOrderRule(RentalOrderRule rule);

	void deleteRentalOrderRules(String ownerType, Long ownerId, Byte handleType);

	List<RentalOrderRule> listRentalOrderRules(String ownerType, Long ownerId, Byte handleType);

	List<RentalOrder> searchRentalOrders(Long resourceTypeId, String resourceType, Long rentalSiteId, Byte billStatus,
										 Long startTime, Long endTime, String tag1, String tag2, Long pageAnchor ,
										 Integer pageSize);
}
