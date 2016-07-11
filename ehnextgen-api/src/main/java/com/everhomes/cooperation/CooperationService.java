package com.everhomes.cooperation;

import com.everhomes.rest.cooperation.NewCooperationCommand;

//import com.everhomes.cooperation.admin.ListCooperationAdminCommand;
//import com.everhomes.cooperation.admin.ListCooperationAdminCommandResponse;
 
public interface CooperationService {
	void newCooperation(NewCooperationCommand cmd);

//	ListCooperationAdminCommandResponse listCooperation(
//			ListCooperationAdminCommand cmd);
}
