package com.everhomes.techpark.rental;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.util.StringHelper;

public interface RentalProvider {

	List<String> findRentalSiteTypes();
	  

	void updateRentalRule(RentalRule rentalRule);
	
	void createRentalRule(RentalRule rentalRule);

	Long createRentalSite(RentalSite rentalsite);
 

	List<RentalSiteItem> findRentalSiteItems(Long rentalSiteId);

	void createRentalSiteItem(RentalSiteItem siteItem);

	void createRentalSiteRule(RentalSiteRule rsr);

	List<RentalSiteRule> findRentalSiteRules(Long rentalSiteId,
			String ruleDate, Timestamp beginDate, Byte rentalType, Byte dateLength,Byte status);

	RentalSiteRule findRentalSiteRuleById(Long siteRuleId);

	Long createRentalBill(RentalBill rentalBill);

	Long createRentalItemBill(RentalItemsBill rib);

	Long createRentalSiteBill(RentalSitesBill rsb);

	List<RentalSitesBill> findRentalSiteBillBySiteRuleId(Long siteRuleId);

	List<RentalItemsBill> findRentalItemsBillByItemsId(Long siteItemId);

	Integer deleteRentalSiteRules(Long rentalSiteId, Long beginDate, Long endDate);

	 
	RentalSite getRentalSiteById(Long rentalSiteId);

	Integer getSumSitePrice(Long rentalBillId);

	List<RentalItemsBill> findRentalItemsBillBySiteBillId(Long rentalBillId);

	RentalSiteItem findRentalSiteItemById(Long rentalSiteItemId);

	RentalItemsBill findRentalItemBill(Long rentalBillId, Long rentalSiteItemId);

	RentalBill findRentalBillById(Long rentalBillId);

	void cancelRentalBillById(Long rentalBillId);

	Integer countRentalSiteBills(Long rentalSiteId, Long beginDate, Long endDate,Time beginTime,Time endTime);

	void updateRentalSite(RentalSite rentalsite);

	void deleteRentalSite(Long rentalSiteId);

	void updateRentalSiteStatus(Long rentalSiteId, byte status);

	 

	Integer countRentalSiteItemBills(Long rentalSiteItemId);

	void deleteRentalSiteItemById(Long rentalSiteItemId);

	Double sumRentalRuleBillSumCounts(Long siteRuleId);
  
	 
	Integer updateBillInvoice(Long rentalBillId, Byte invoiceFlag);

	void updateRentalBill(RentalBill bill);

	void deleteRentalBillById(Long rentalBillId);

	Long createRentalBillAttachment(RentalBillAttachment rba);

	Long createRentalBillPaybillMap(RentalBillPaybillMap billmap);

	List<RentalSitesBill> findRentalSitesBillByBillId(Long id);

	List<RentalSiteRule> findRentalSiteRulesByRuleIds(List<Long> siteRuleIds);

	List<RentalBillAttachment> findRentalBillAttachmentByBillId(
			Long rentalBillId);

	RentalBillPaybillMap findRentalBillPaybillMapByOrderNo(String orderNo);

	List<RentalBill> listRentalBills(Long userId, Long resourceTypeId, ListingLocator locator,
			int count, List<Byte> status);

	int countRentalSites(Long  resourceTypeId,String keyword,List<Byte>  status,List<Long>  siteIds);

	int countRentalBills(Long ownerId, String ownerType, String siteType,
			Long rentalSiteId, Byte billStatus, Long startTime, Long endTime,
			Byte invoiceFlag,Long userId);

	List<RentalBill> listRentalBills(Long resourceTypeId, Long organizationId, Long rentalSiteId, ListingLocator locator, Byte billStatus,
			String vendorType , Integer pageSize, Long startTime, Long endTime,
			Byte invoiceFlag,Long userId);

	List<RentalSite> findRentalSites(Long  resourceTypeId, String keyword, ListingLocator locator,
			Integer pageSize,List<Byte>  status,List<Long>  siteIds,Long communityId);

	RentalRule getRentalRule(Long ownerId, String ownerType, String siteType);


	List<RentalBill> listRentalBills(Long ownerId, String ownerType,
			String siteType, Long rentalSiteId, Long beginDate, Long endDate);


	List<RentalSiteOwner> findRentalSiteOwnersByOwnerTypeAndId(
			String ownerType, Long ownerId);


	List<RentalSitePic> findRentalSitePicsByOwnerTypeAndId(String ownerType,
			Long ownerId);


	RentalSiteItem getRentalSiteItemById(Long id);


	List<RentalSitesBillNumber> findSitesBillNumbersBySiteId(Long siteRuleId);


	void createRentalSitesBillNumber(RentalSitesBillNumber sitesBillNumber);


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


	List<RentalSitesBillNumber> findSitesBillNumbersByBillId(Long siteBillId);


	void createRentalSiteOwner(RentalSiteOwner siteOwner);


	void createRentalSitePic(RentalSitePic detailPic);


	void deleteRentalSitePicsBySiteId(Long siteId);


	void deleteRentalSiteOwnersBySiteId(Long siteId);


	void updateRentalSiteItem(RentalSiteItem siteItem);


	Integer countRentalSiteItemSoldCount(Long itemId);


	List<RentalSiteRule> findRentalSiteRuleByDate(Long rentalSiteId,
			String siteNumber, Timestamp beginTime, Timestamp endTime,
			List<Byte> ampmList,String rentalDate) throws ParseException;


	void updateRentalSiteRule(RentalSiteRule rsr);


	void updateRentalDefaultRule(RentalDefaultRule newDefaultRule);


	Integer deleteTimeIntervalsByOwnerId(String ownerType, Long id);


	Integer deleteRentalCloseDatesByOwnerId(String ownerType, Long id);


	Integer deleteRentalConfigAttachmentsByOwnerId(String ownerType, Long id);


	List<RentalSiteOwner> findRentalSiteOwnersBySiteId(Long siteId);


	RentalSiteRule findRentalSiteRulesByRuleId(Long rentalSiteRuleId);


	Integer countRentalSiteItemRentalCount(List<Long> rentalBillIds);


	List<RentalBillPaybillMap> findRentalBillPaybillMapByBillId(Long id);


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


	List<RentalResourceType> findRentalResourceTypes(Integer namespaceId, ListingLocator locator);

 

 
 


}
