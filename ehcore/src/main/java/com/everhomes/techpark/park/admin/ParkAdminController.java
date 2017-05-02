package com.everhomes.techpark.park.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.techpark.park.ApplyParkCardDTO;
import com.everhomes.rest.techpark.park.ApplyParkCardList;
import com.everhomes.rest.techpark.park.CreateParkingChargeCommand;
import com.everhomes.rest.techpark.park.DeleteParkingChargeCommand;
import com.everhomes.rest.techpark.park.FetchCardCommand;
import com.everhomes.rest.techpark.park.OfferCardCommand;
import com.everhomes.rest.techpark.park.RechargeRecordList;
import com.everhomes.rest.techpark.park.SearchApplyCardCommand;
import com.everhomes.rest.techpark.park.SearchRechargeRecordCommand;
import com.everhomes.rest.techpark.park.SetWaitingDaysCommand;
import com.everhomes.rest.techpark.park.WaitingDaysResponse;
import com.everhomes.techpark.park.ParkService;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.RequireAuthentication;

@RestController
@RequireAuthentication(true)
@RequestMapping("/admin/techpark/park")
@Deprecated
public class ParkAdminController extends ControllerBase{
	
//	@Autowired
	private ParkService parkService;
	
	@Autowired
    private ConfigurationProvider configurationProvider;

	/**
	 * <b>URL: /admin/techpark/park/addParkingCharge</b>
	 * admin set parking charge rule
	 * @return
	 */
	@RequestMapping("addParkingCharge")
	@RestReturn(value = String.class)
	public RestResponse addParkingCharge(CreateParkingChargeCommand cmd){
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
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
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        parkService.deleteCharge(cmd);
        RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /admin/techpark/park/setWaitingDays</b>
	 * admin set parking card valid days to pick up after offering
	 * @return
	 */
	@RequestMapping("setWaitingDays")
	@RestReturn(value = WaitingDaysResponse.class)
	public RestResponse setWaitingDays(SetWaitingDaysCommand cmd) {
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        configurationProvider.setValue(ConfigConstants.PARKING_CARD_VALID_DAYS, cmd.getDays());
        
        WaitingDaysResponse waitingDays = new WaitingDaysResponse();
        
        waitingDays.setWaitingDays(configurationProvider.getValue(ConfigConstants.PARKING_CARD_VALID_DAYS, ""));
        
		RestResponse response = new RestResponse(waitingDays);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
		
	}
	
	/**
	 * <b>URL: /admin/techpark/park/getWaitingDays</b>
	 * admin set parking card valid days to pick up after offering
	 * @return
	 */
	@RequestMapping("getWaitingDays")
	@RestReturn(value = WaitingDaysResponse.class)
	public RestResponse getWaitingDays() {
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        WaitingDaysResponse waitingDays = new WaitingDaysResponse();
        waitingDays.setWaitingDays(configurationProvider.getValue(ConfigConstants.PARKING_CARD_VALID_DAYS, ""));
        
		RestResponse response = new RestResponse(waitingDays);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
		
	}
	
	/**
	 * <b>URL: /admin/techpark/park/searchRechargeRecordList</b>
	 * 
	 * @return
	 */
	@RequestMapping("searchRechargeRecordList")
	@RestReturn(value = RechargeRecordList.class)
	public RestResponse searchRechargeRecordList(SearchRechargeRecordCommand cmd) {
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        RechargeRecordList result = parkService.searchRechargeRecord(cmd);
        RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /admin/techpark/park/searchApplyCardList</b>
	 * 
	 * @return
	 */
	@RequestMapping("searchApplyCardList")
	@RestReturn(value = ApplyParkCardList.class)
	public RestResponse searchApplyCardList(SearchApplyCardCommand cmd) {
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        ApplyParkCardList list = parkService.searchApplyCardList(cmd);
        RestResponse response = new RestResponse(list);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /admin/techpark/park/offerCard</b>
	 * <p>发放月卡，主要是设置一下发放多少张卡，并修改排除队列里人的领卡通知状态及领取期限</p>
	 * @return
	 */
	@RequestMapping("offerCard")
	@RestReturn(value = ApplyParkCardList.class)
	public RestResponse offerCard(OfferCardCommand cmd) {
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        ApplyParkCardList list = parkService.offerCard(cmd);
        RestResponse response = new RestResponse(list);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /admin/techpark/park/fetchCard</b>
	 * <p>在线下发卡后，在后台管理中设置领卡状态</p>
	 * @return
	 */
	@RequestMapping("fetchCard")
	@RestReturn(value = ApplyParkCardDTO.class)
	public RestResponse fetchCard(FetchCardCommand cmd) {
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        ApplyParkCardDTO result = parkService.fetchCard(cmd);
        RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}
