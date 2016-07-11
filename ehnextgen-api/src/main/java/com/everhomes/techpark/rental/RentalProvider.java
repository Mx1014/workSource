package com.everhomes.techpark.rental;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

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
			String ruleDate, Timestamp beginDate, Byte rentalType, Byte dateLength);

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

	List<RentalBill> listRentalBills(Long userId, Long ownerId,
			String ownerType, String siteType, ListingLocator locator,
			int count, Byte status);

	int countRentalSites(Long ownerId, String ownerType, String siteType,
			String keyword,List<Byte>  status);

	int countRentalBills(Long ownerId, String ownerType, String siteType,
			Long rentalSiteId, Byte billStatus, Long startTime, Long endTime,
			Byte invoiceFlag,Long userId);

	List<RentalBill> listRentalBills(Long ownerId, String ownerType,
			String siteType, Long rentalSiteId, Byte billStatus,
			Integer pageOffset, Integer pageSize, Long startTime, Long endTime,
			Byte invoiceFlag,Long userId);

	List<RentalSite> findRentalSites(Long ownerId, String ownerType,
			String siteType, String keyword, Integer pageOffset,
			Integer pageSize,List<Byte>  status);

	RentalRule getRentalRule(Long ownerId, String ownerType, String siteType);


	List<RentalBill> listRentalBills(Long ownerId, String ownerType,
			String siteType, Long rentalSiteId, Long beginDate, Long endDate);


	RentalSiteItem getRentalSiteItemById(Long rentalSiteItemId);
 


}
