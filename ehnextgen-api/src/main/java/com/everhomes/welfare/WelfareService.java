// @formatter:off
package com.everhomes.welfare;

import com.everhomes.rest.welfare.*;

public interface WelfareService {


	ListWelfaresResponse listWelfares(ListWelfaresCommand cmd);


	GetWelfareResponse getWelfare(GetWelfareCommand cmd);

	DraftWelfareResponse draftWelfare(DraftWelfareCommand cmd);


	SendWelfaresResponse sendWelfare(SendWelfareCommand cmd);


	GetUserWelfareResponse getUserWelfare(GetUserWelfareCommand cmd);

	void deleteWelfare(DeleteWelfareCommand cmd);


	ListUserWelfaresResponse listUserWelfares(ListUserWelfaresCommand cmd);
}