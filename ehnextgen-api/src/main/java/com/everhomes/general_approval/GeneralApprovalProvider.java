package com.everhomes.general_approval;

import java.util.List;

import com.everhomes.enterpriseApproval.EnterpriseApprovalTemplate;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface GeneralApprovalProvider {

	Long createGeneralApproval(GeneralApproval obj);

	Long updateGeneralApproval(GeneralApproval obj);

	void deleteGeneralApproval(GeneralApproval obj);

	GeneralApproval getGeneralApprovalById(Long id);

	List<GeneralApproval> queryGeneralApprovals(ListingLocator locator,
			int count, ListingQueryBuilderCallback queryBuilderCallback);

    GeneralApproval getGeneralApprovalByName(Integer namespaceId, Long moduleId, Long ownerId, String ownerType, String approvalName);

    GeneralApproval getGeneralApprovalByNameAndRunning(Integer namespaceId, Long moduleId, Long ownerId, String ownerType);

	GeneralApproval getGeneralApprovalByAttribute(Integer namespaceId, Long ownerId, String attribute);

	void disableApprovalByFormOriginId(Long formOriginId, Long moduleId, String moduleType);

	void createGeneralApprovalScopeMap(GeneralApprovalScopeMap scope);

    void updateGeneralApprovalScopeMap(GeneralApprovalScopeMap scope);

    GeneralApprovalScopeMap findGeneralApprovalScopeMap(Integer namespaceId, Long approvalId, Long sourceId, String sourceType);

	void deleteApprovalScopeMapByApprovalId(Long approvalId);

    void deleteOddGeneralApprovalScope(Integer namespaceId, Long approvalId, String sourceType, List<Long> detailIds);

	List<GeneralApprovalScopeMap> listGeneralApprovalScopes(Integer namespaceId, Long approvalId);
}
