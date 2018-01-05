package com.everhomes.incubator;


import com.everhomes.rest.incubator.*;

import java.util.List;

public interface IncubatorService {
	ListIncubatorApplyResponse listIncubatorApply(ListIncubatorApplyCommand cmd);

	List<IncubatorApplyDTO> listMyTeams();

	ListIncubatorProjectTypeResponse listIncubatorProjectType();

	IncubatorApplyDTO addIncubatorApply(AddIncubatorApplyCommand cmd);

	void cancelIncubatorApply(CancelIncubatorApplyCommand cmd);

	void approveIncubatorApply(ApproveIncubatorApplyCommand cmd);

	IncubatorApplyDTO findIncubatorApply(FindIncubatorApplyCommand cmd);

	void exportIncubatorApply(ExportIncubatorApplyCommand cmd);

	IncubatorApplyDTO findIncubatorAppling();
}
