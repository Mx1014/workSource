package com.everhomes.flow;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface FlowSubjectProvider {

	void updateFlowSubject(FlowSubject obj);

	void deleteFlowSubject(FlowSubject obj);

	FlowSubject getFlowSubjectById(Long id);

	List<FlowSubject> queryFlowSubjects(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	Long createFlowSubject(FlowSubject obj);

}
