package com.everhomes.general_approval;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface GeneralApprovalProvider {

	Long createGeneralApproval(GeneralApproval obj);

	void updateGeneralApproval(GeneralApproval obj);

	void deleteGeneralApproval(GeneralApproval obj);

	GeneralApproval getGeneralApprovalById(Long id);

	List<GeneralApproval> queryGeneralApprovals(ListingLocator locator,
			int count, ListingQueryBuilderCallback queryBuilderCallback);

	List<GeneralApprovalTemplate> listGeneralApprovalTemplateByModuleId(Long moduleId);

    GeneralApproval getGeneralApprovalByName(Long moduleId, Long ownerId, String ownerType, String approvalName);

    GeneralApproval getGeneralApprovalByTemplateId(Long moduleId, Long ownerId, String ownerType, Long templateId);

}
