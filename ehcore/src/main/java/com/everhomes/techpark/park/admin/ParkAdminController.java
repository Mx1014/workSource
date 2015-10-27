package com.everhomes.techpark.park.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.techpark.park.CreateParkingChargeCommand;
import com.everhomes.techpark.park.DeleteParkingChargeCommand;
import com.everhomes.techpark.park.ParkService;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.RequireAuthentication;

@RestController
@RequireAuthentication(true)
@RequestMapping("/admin/techpark/park")
public class ParkAdminController extends ControllerBase{
	
	@Autowired
	private ParkService parkService;

	/**
	 * <b>URL: /admin/techpark/park/addParkingCharge</b>
	 * admin set parking charge rule
	 * @return
	 */
	@RequestMapping("addParkingCharge")
	@RestReturn(value = String.class)
	public RestResponse addParkingCharge(CreateParkingChargeCommand cmd){
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        parkService.addCharge(cmd);
        RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /admin/techpark/park/deleteParkingCharge</b>
	 * admin delete parking charge rule
	 * @return
	 */
	@RequestMapping("deleteParkingCharge")
	@RestReturn(value = String.class)
	public RestResponse deleteParkingCharge(DeleteParkingChargeCommand cmd){
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        parkService.deleteCharge(cmd);
        RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	
}
