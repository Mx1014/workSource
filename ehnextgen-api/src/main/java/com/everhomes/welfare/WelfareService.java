// @formatter:off
package com.everhomes.welfare;

import com.everhomes.rest.welfare.DraftWelfareCommand;
import com.everhomes.rest.welfare.GetUserWelfareCommand;
import com.everhomes.rest.welfare.GetUserWelfareResponse;
import com.everhomes.rest.welfare.ListWelfaresCommand;
import com.everhomes.rest.welfare.ListWelfaresResponse;
import com.everhomes.rest.welfare.SendWelfareCommand;

public interface WelfareService {


	ListWelfaresResponse listWelfares(ListWelfaresCommand cmd);


	void draftWelfare(DraftWelfareCommand cmd);


	void sendWelfare(SendWelfareCommand cmd);


	GetUserWelfareResponse getUserWelfare(GetUserWelfareCommand cmd);

}