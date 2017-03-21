package com.everhomes.flow;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.flow.FlowStepType;
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

	List<FlowEventLog> findEventLogsByNodeId(Long nodeId, Long caseId,
			Long stepCount, FlowUserType flowUserType);

	List<FlowEventLog> findStepEventLogs(Long caseId);

	FlowEventLog getStepEvent(Long caseId, Long flowNodeId, Long stepCount);

	FlowEventLog isProcessor(Long userId, FlowCase flowCase);

	List<FlowEventLog> findFiredEventsByLog(FlowEventLog log);

	/**
	 * 找到某一步当中的跳转日志
	 * @param caseId
	 * @param stepCount
	 * @return
	 */
	List<FlowEventLog> findStepEventLogs(Long caseId, Long stepCount);

	/* 获取处理的事件 */
	FlowEventLog getValidEnterStep(Long userId, FlowCase flowCase);

	void updateFlowEventLogs(List<FlowEventLog> updateLogs);

	List<FlowEventLog> findPrefixStepEventLogs(Long caseId, Long stepCount);

}
