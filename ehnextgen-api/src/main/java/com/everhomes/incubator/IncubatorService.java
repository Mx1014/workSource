package com.everhomes.incubator;


import com.everhomes.rest.incubator.*;

public interface IncubatorService {
	ListIncubatorApplyResponse listIncubatorApply(ListIncubatorApplyCommand cmd);

	ListIncubatorProjectTypeResponse listIncubatorProjectType();

	IncubatorApplyDTO addIncubatorApply(AddIncubatorApplyCommand cmd);

	void approveIncubatorApply(ApproveIncubatorApplyCommand cmd);

	IncubatorApplyDTO findIncubatorApply(FindIncubatorApplyCommand cmd);
}
