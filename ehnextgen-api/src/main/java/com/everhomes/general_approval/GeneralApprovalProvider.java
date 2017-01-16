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

}
