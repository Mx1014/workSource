package com.everhomes.techpark.expansion;

import java.util.List;

import com.everhomes.enterprise.EnterpriseAddress;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;


public interface EnterpriseApplyEntryProvider {
	
	EnterpriseDetail getEnterpriseDetailById(Long id);
	
	List<EnterpriseOpRequest> listApplyEntrys(EnterpriseOpRequest request, ListingLocator locator, int pageSize);
	
	boolean createApplyEntry(EnterpriseOpRequest enterpriseOpRequest);
	
	EnterpriseOpRequest getEnterpriseOpRequestByBuildIdAndUserId(Long id, Long userId);
	
	List<LeasePromotion> listLeasePromotions(LeasePromotion leasePromotion,
			ListingLocator locator, int pageSize);

	LeasePromotion createLeasePromotion(LeasePromotion leasePromotion);
	
	LeasePromotion getLeasePromotionById(Long id);
	
	boolean updateLeasePromotion(LeasePromotion leasePromotion);
	
	boolean deleteLeasePromotionAttachment(Long leaseId);
	
	boolean addPromotionAttachment(LeasePromotionAttachment attachment);
	
	EnterpriseOpRequest getApplyEntryById(Long id);
	
	boolean updateLeasePromotionStatus(long id, byte status);
	
	boolean updateApplyEntryStatus(long id, byte status);
	
	boolean deleteLeasePromotion(long id);
	
	boolean deleteApplyEntry(long id);

	LeasePromotion getLeasePromotionByToken(Integer namespaceId, Long communityId, String namespaceType, String namespaceToken);
 

	List<EnterpriseOpRequest> listApplyEntrys(EnterpriseOpRequest request, ListingLocator locator,
			int pageSize, List<Long> idList);
}
