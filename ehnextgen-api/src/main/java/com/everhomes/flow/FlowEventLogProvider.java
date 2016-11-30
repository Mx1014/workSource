package com.everhomes.flow;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.flow.SearchFlowCaseCommand;

public interface FlowEventLogProvider {

	Long createFlowEventLog(FlowEventLog obj);

	void updateFlowEventLog(FlowEventLog obj);

	void deleteFlowEventLog(FlowEventLog obj);

	FlowEventLog getFlowEventLogById(Long id);

	List<FlowEventLog> queryFlowEventLogs(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	Long getNextId();

	void createFlowEventLogs(List<FlowEventLog> objs);

	List<FlowCaseDetail> findProcessorFlowCases(ListingLocator locator,
			int count, SearchFlowCaseCommand cmd);

	List<FlowEventLog> findEventLogsByNodeId(Long caseId, Long nodeId,
			FlowUserType flowUserType);

}
