package com.everhomes.cooperation;

import java.util.List;

import com.everhomes.banner.Banner;

public interface CooperationProvider {
	void newCooperation(CooperationRequests cooperationRequests);

	List<CooperationRequests> listCooperation(String keyword, String cooperationType,
			long offset, long pageSize);
}
