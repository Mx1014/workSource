package com.everhomes.rentalv2;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.rentalv2.MaxMinPrice;
import com.everhomes.server.schema.tables.pojos.EhRentalv2Cells;

public interface Rentalv2Provider {
 
 

	Long createRentalSite(RentalResource rentalsite);
 

	List<RentalItem> findRentalSiteItems(Long rentalSiteId);

	void createRentalSiteItem(RentalItem siteItem);

	void createRentalSiteRule(RentalCell rsr);

	List<RentalCell> findRentalSiteRules(Long rentalSiteId,
			String ruleDate, Timestamp beginDate, Byte rentalType, Byte dateLength,Byte status);

	RentalCell findRentalSiteRuleById(Long siteRuleId);

	Long createRentalOrder(RentalOrder rentalBill);

	Long createRentalItemBill(RentalItemsOrder rib);

	Long createRentalSiteBill(RentalResourceOrder rsb);

	List<RentalResourceOrder> findRentalSiteBillBySiteRuleId(Long siteRuleId);

	List<RentalItemsOrder> findRentalItemsBillByItemsId(Long siteItemId);

	Integer deleteResourceCells(Long rentalSiteId, Long beginDate, Long endDate);

	 
	RentalResource getRentalSiteById(Long rentalSiteId);

	Integer getSumSitePrice(Long rentalBillId);

	List<RentalItemsOrder> findRentalItemsBillBySiteBillId(Long rentalBillId);

	RentalItem findRentalSiteItemById(Long rentalSiteItemId);

	RentalItemsOrder findRentalItemBill(Long rentalBillId, Long rentalSiteItemId);

	RentalOrder findRentalBillById(Long rentalBillId);

	void cancelRentalBillById(Long rentalBillId);

	Integer countRentalSiteBills(Long rentalSiteId, Long beginDate, Long endDate,Time beginTime,Time endTime);

	void updateRentalSite(RentalResource rentalsite);

	void deleteResource(Long rentalSiteId);

	void updateRentalSiteStatus(Long rentalSiteId, byte status);

	 

	Integer countRentalSiteItemBills(Long rentalSiteItemId);

	void deleteRentalSiteItemById(Long rentalSiteItemId);

	Double sumRentalRuleBillSumCounts(Long siteRuleId);
  
	 
	Integer updateBillInvoice(Long rentalBillId, Byte invoiceFlag);

	void updateRentalBill(RentalOrder bill);

	void deleteRentalBillById(Long rentalBillId);

	Long createRentalBillAttachment(RentalOrderAttachment rba);

	Long createRentalBillPaybillMap(RentalOrderPayorderMap billmap);

	List<RentalResourceOrder> findRentalResourceOrderByOrderId(Long id);

	List<RentalCell> findRentalSiteRulesByRuleIds(List<Long> siteRuleIds);

	List<RentalOrderAttachment> findRentalBillAttachmentByBillId(
			Long rentalBillId);

	RentalOrderPayorderMap findRentalBillPaybillMapByOrderNo(String orderNo);

	List<RentalOrder> listRentalBills(Long userId, Long resourceTypeId, ListingLocator locator,
			int count, List<Byte> status, Byte payMode);

	int countRentalSites(Long  resourceTypeId,String keyword,List<Byte>  status,List<Long>  siteIds);

	int countRentalBills(Long ownerId, String ownerType, String siteType,
			Long rentalSiteId, Byte billStatus, Long startTime, Long endTime,
			Byte invoiceFlag,Long userId);

	List<RentalOrder> listRentalBills(Long resourceTypeId, Long organizationId, Long rentalSiteId, ListingLocator locator, Byte billStatus,
			String vendorType , Integer pageSize, Long startTime, Long endTime,
			Byte invoiceFlag,Long userId);

	List<RentalOrder> listSuccessRentalBills();
	
	List<RentalResource> findRentalSites(Long  resourceTypeId, String keyword, ListingLocator locator,
			Integer pageSize,List<Byte>  status,List<Long>  siteIds,Long communityId);
 

	List<RentalOrder> listRentalBills(Long ownerId, String ownerType,
			String siteType, Long rentalSiteId, Long beginDate, Long endDate);


	List<RentalSiteRange> findRentalSiteOwnersByOwnerTypeAndId(
			String ownerType, Long ownerId);


	List<RentalResourcePic> findRentalSitePicsByOwnerTypeAndId(String ownerType,
			Long ownerId);


	RentalItem getRentalSiteItemById(Long id);

 

	void createRentalDefaultRule(RentalDefaultRule defaultRule);


	void createTimeInterval(RentalTimeInterval timeInterval);


	void createRentalCloseDate(RentalCloseDate rcd);


	void createRentalConfigAttachment(RentalConfigAttachment rca);


	RentalDefaultRule getRentalDefaultRule(String ownerType, Long ownerId,
			Long resourceTypeId);


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


	List<RentalCell> findRentalSiteRuleByDate(Long rentalSiteId,
			String siteNumber, Timestamp beginTime, Timestamp endTime,
			List<Byte> ampmList,String rentalDate) throws ParseException;


	void updateRentalSiteRule(RentalCell rsr);


	void updateRentalDefaultRule(RentalDefaultRule newDefaultRule);


	Integer deleteTimeIntervalsByOwnerId(String ownerType, Long id);


	Integer deleteRentalCloseDatesByOwnerId(String ownerType, Long id);


	Integer deleteRentalConfigAttachmentsByOwnerId(String ownerType, Long id);


	List<RentalSiteRange> findRentalSiteOwnersBySiteId(Long siteId);


	RentalCell findRentalSiteRulesByRuleId(Long rentalSiteRuleId);


	Integer countRentalSiteItemRentalCount(List<Long> rentalBillIds);


	List<RentalOrderPayorderMap> findRentalBillPaybillMapByBillId(Long id);


	Long createRentalRefundOrder(RentalRefundOrder rentalRefundOrder);


	void deleteRentalRefundOrder(RentalRefundOrder rentalRefundOrder);

	void deleteRentalRefundOrder(Long rentalRefundOrderId);


	void updateRentalRefundOrder(RentalRefundOrder rentalRefundOrder);


	List<RentalRefundOrder> getRefundOrderList(Long resourceTypeId,
			CrossShardListingLocator locator, Byte status, String styleNo,
			int pageSize, Long startTime, Long endTime);


	RentalRefundOrder getRentalRefundOrderById(Long rentalRefundOrderId);


	RentalRefundOrder getRentalRefundOrderByRefoundNo(String refundOrderNo);


	RentalResourceType getRentalResourceTypeById(Long rentalResourceTypeId);


	void createRentalResourceType(RentalResourceType rentalResourceType);


	void deleteRentalResourceType(Long resoureceTypeId);


	void updateRentalResourceType(RentalResourceType resourceType);


	List<RentalResourceType> findRentalResourceTypes(Integer namespaceId, Byte status, ListingLocator locator);


	List<RentalCell> findRentalCellBetweenDates(Long rentalSiteId,
			String beginTime, String endTime) throws ParseException;


	void createRentalResourceNumber(RentalResourceNumber resourceNumber);


	Integer deleteRentalResourceNumbersByOwnerId(String simpleName, Long id);


	List<RentalResourceNumber> queryRentalResourceNumbersByOwner(
			String simpleName, Long id);


	void updateRentalOrderPayorderMap(RentalOrderPayorderMap ordeMap);


	void batchCreateRentalCells(List<EhRentalv2Cells> list);


	RentalCell getRentalCellById(Long cellId);


	String getPriceStringByResourceId(Long rentalSiteId);

 
	void deleteRentalCellsByResourceId(Long rentalSiteId); 

	List<RentalOrder> listSiteRentalByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor, int pageSize);


	List<RentalOrder> listSiteRentalByUpdateTime(Integer namespaceId, Long timestamp, int pageSize);


	List<RentalOrder> findRentalSiteBillBySiteRuleIds(List<Long> siteRuleIds);

	List<RentalCell> getRentalCellsByIds(List<Long> cellIds);


	Double countRentalSiteBillBySiteRuleId(Long cellId);


	Double countRentalSiteBillOfAllScene(RentalResource rentalResource, RentalCell rentalCell, List<Rentalv2PriceRule> priceRules);


	MaxMinPrice findMaxMinPrice(Long ownerId, Byte rentalType);


	boolean findOtherModeClosed(RentalResource rentalResource, RentalCell rentalCell,
			List<Rentalv2PriceRule> priceRules);

}
