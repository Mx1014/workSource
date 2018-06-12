// @formatter:off
package com.everhomes.welfare;

import com.everhomes.rest.welfare.*;

public interface WelfareService {


	ListWelfaresResponse listWelfares(ListWelfaresCommand cmd);


	void draftWelfare(DraftWelfareCommand cmd);


	void sendWelfare(SendWelfareCommand cmd);


	GetUserWelfareResponse getUserWelfare(GetUserWelfareCommand cmd);

	void deleteWelfare(DeleteWelfareCommand cmd);
}