package com.everhomes.flow;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.flow.SearchFlowCaseCommand;

import java.sql.Timestamp;
import java.util.List;

public interface FlowCaseProvider {

	Long createFlowCase(FlowCase obj);

	void updateFlowCase(FlowCase obj);

	void deleteFlowCase(FlowCase obj);

	FlowCase getFlowCaseById(Long id);

	List<FlowCase> queryFlowCases(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	List<FlowCaseDetail> findApplierFlowCases(ListingLocator locator,
			int count, SearchFlowCaseCommand cmd);

	List<FlowCaseDetail> listFlowCasesByModuleId(ListingLocator locator,
											  int count, SearchFlowCaseCommand cmd);

	boolean updateIfValid(Long flowCaseId, Timestamp last, Timestamp now);

	List<FlowCaseDetail> findAdminFlowCases(ListingLocator locator, int count,
			SearchFlowCaseCommand cmd);

	FlowCase findFlowCaseByReferId(Long referId, String referType, Long moduleId);

	Long createFlowCaseHasId(FlowCase obj);

    List<FlowCase> findFlowCaseByNode(Long originalNodeId, Long convergenceNodeId);

    List<FlowCase> listFlowCaseByParentId(Long parentId);
}
