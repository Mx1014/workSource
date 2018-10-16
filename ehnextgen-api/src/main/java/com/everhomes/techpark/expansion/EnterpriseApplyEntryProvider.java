package com.everhomes.techpark.expansion;

import java.util.List;

import com.everhomes.enterprise.EnterpriseAddress;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;


public interface EnterpriseApplyEntryProvider {
	
	EnterpriseDetail getEnterpriseDetailById(Long id);
	
	List<EnterpriseOpRequest> listApplyEntrys(EnterpriseOpRequest request, ListingLocator locator, Integer pageSize);
	
	boolean createApplyEntry(EnterpriseOpRequest enterpriseOpRequest);
	
	EnterpriseOpRequest getEnterpriseOpRequestByBuildIdAndUserId(Long id, Long userId);
	
	List<LeasePromotion> listLeasePromotions(LeasePromotion leasePromotion,
			ListingLocator locator, int pageSize);

	void createLeasePromotion(LeasePromotion leasePromotion);
	
	LeasePromotion getLeasePromotionById(Long id);
	
	boolean updateLeasePromotion(LeasePromotion leasePromotion);
	
	boolean deleteLeasePromotionAttachment(String ownerTYpe, Long ownerId);
	
	boolean addPromotionAttachment(LeasePromotionAttachment attachment);
	
	EnterpriseOpRequest getApplyEntryById(Long id);
	
	boolean updateLeasePromotionStatus(long id, byte status);
	
	boolean updateApplyEntryStatus(long id, byte status);
	
	boolean deleteLeasePromotion(long id);
	
	boolean deleteApplyEntry(long id);

	LeasePromotion getLeasePromotionByToken(Integer namespaceId, Long communityId, String namespaceType, String namespaceToken);
 

	List<EnterpriseOpRequest> listApplyEntrys(EnterpriseOpRequest request, ListingLocator locator,
											  Integer pageSize, List<Long> idList);

	void updateApplyEntry(EnterpriseOpRequest request);

	List<LeasePromotionAttachment> findAttachmentsByOwnerTypeAndOwnerId(String ownerType, Long ownerId);

	void deleteLeasePromotionByUidAndIssuerType(long id, String issuerType);

	List<LeasePromotion> listLeasePromotionsByUidAndIssuerType(long id, String issuerType);

	void deleteApplyEntrysByLeasePromotionIds(List<Long> idList);

	void createLeaseRequestForm(LeaseFormRequest leaseFormRequest);

	void updateLeaseRequestForm(LeaseFormRequest leaseFormRequest);

	void deleteLeaseRequestForm(LeaseFormRequest leaseFormRequest);

	LeaseFormRequest findLeaseRequestFormById(Long id);

	LeaseFormRequest findLeaseRequestForm(Integer namespaceId, Long ownerId, String ownerType, String sourceType, Long categoryId);
	
	List<LeaseFormRequest> listLeaseRequestForm(Integer namespaceId, Long ownerId, String ownerType);

	void updateApplyEntryTransformFlag(Long applyEntryId, byte transformFlag);
	
}
