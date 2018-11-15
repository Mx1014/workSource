// @formatter:off
package com.everhomes.welfare;

import javax.servlet.http.HttpServletRequest;

import com.everhomes.rest.welfare.*;

public interface WelfareService {

	ListWelfaresResponse listWelfares(ListWelfaresCommand cmd);

	GetWelfareResponse getWelfare(GetWelfareCommand cmd);

	DraftWelfareResponse draftWelfare(DraftWelfareCommand cmd);

	GetUserWelfareResponse getUserWelfare(GetUserWelfareCommand cmd);

	void deleteWelfare(DeleteWelfareCommand cmd);

	ListUserWelfaresResponse listUserWelfares(ListUserWelfaresCommand cmd);

	SendWelfaresResponse sendWelfare(SendWelfareCommand cmd, HttpServletRequest request);
}