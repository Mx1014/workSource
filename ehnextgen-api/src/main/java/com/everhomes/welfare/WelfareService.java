// @formatter:off
package com.everhomes.welfare;

import com.everhomes.rest.welfare.DraftWelfareCommand;
import com.everhomes.rest.welfare.GetUserWelfareCommand;
import com.everhomes.rest.welfare.GetUserWelfareResponse;
import com.everhomes.rest.welfare.ListWelfaresCommand;
import com.everhomes.rest.welfare.ListWelfaresResponse;
import com.everhomes.rest.welfare.SendWelfareCommand;

public interface WelfareService {


	public ListWelfaresResponse listWelfares(ListWelfaresCommand cmd);


	public void draftWelfare(DraftWelfareCommand cmd);


	public void sendWelfare(SendWelfareCommand cmd);


	public GetUserWelfareResponse getUserWelfare(GetUserWelfareCommand cmd);

}