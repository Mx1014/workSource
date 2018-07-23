package com.everhomes.general_approval;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface GeneralApprovalValProvider {

	Long createGeneralApprovalVal(GeneralApprovalVal obj);

	GeneralApprovalVal getSpecificApprovalValByFlowCaseId(Long flowCaseId, String filedType);

	List<GeneralApprovalVal> queryGeneralApprovalVals(ListingLocator locator,
			int count, ListingQueryBuilderCallback queryBuilderCallback);

	List<GeneralApprovalVal> queryGeneralApprovalValsByFlowCaseId(Long id);

	GeneralApprovalVal getGeneralApprovalByFlowCaseAndFeildType(Long id, String fieldType);

	GeneralApprovalVal getGeneralApprovalByFlowCaseAndName(Long id, String fieldName);

	GeneralApprovalVal getGeneralApprovalById(Long id);
}
