package com.everhomes.general_approval;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface GeneralApprovalValProvider {

	Long createGeneralApprovalVal(GeneralApprovalVal obj);

	void updateGeneralApprovalVal(GeneralApprovalVal obj);

	void deleteGeneralApprovalVal(GeneralApprovalVal obj);

	GeneralApprovalVal getGeneralApprovalValById(Long id);

	List<GeneralApprovalVal> queryGeneralApprovalVals(ListingLocator locator,
			int count, ListingQueryBuilderCallback queryBuilderCallback);

	List<GeneralApprovalVal> queryGeneralApprovalValsByFlowCaseId(Long id);

	GeneralApprovalVal getGeneralApprovalByFlowCaseAndName(Long id, String fieldName);

}
