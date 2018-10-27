package com.everhomes.general_approval;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface GeneralApprovalValProvider {

	Long createGeneralApprovalVal(GeneralApprovalVal obj);

	void updateGeneralApprovalVal(GeneralApprovalVal obj);

	GeneralApprovalVal getSpecificApprovalValByFlowCaseId(Long flowCaseId, String filedType);

	List<GeneralApprovalVal> queryGeneralApprovalVals(ListingLocator locator,
			int count, ListingQueryBuilderCallback queryBuilderCallback);

	List<GeneralApprovalVal> queryGeneralApprovalValsByFlowCaseId(Long id);

	GeneralApprovalVal getGeneralApprovalByFlowCaseAndFeildType(Long id, String fieldType);

	GeneralApprovalVal getGeneralApprovalByFlowCaseAndName(Long id, String fieldName);

	GeneralApprovalVal getGeneralApprovalVal(Long flowCaseId, Long formOriginId, Long formVersion, String fieldName);

	List<GeneralApprovalVal> getGeneralApprovalVal(Long formOriginId, Long formVersion, Long flowCaseId, Long flowNodeId);

	GeneralApprovalVal getGeneralApprovalById(Long id);

}
