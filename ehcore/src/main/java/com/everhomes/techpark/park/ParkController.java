package com.everhomes.techpark.park;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.techpark.onlinePay.OnlinePayBillCommand;
import com.everhomes.techpark.punch.PunchLogsDayList;



@RestDoc(value = "Park controller", site = "ehcore")
@RestController
@RequestMapping("/techpark/park")
public class ParkController extends ControllerBase{
	
	@Autowired
	private ParkService parkService;
	
	/**
	 * <b>URL: /techpark/park/recharge</b>
	 * customer recharge parking card, write a record into record table 
	 * @return
	 */
	
	@RequestMapping("recharge")
	@RestReturn(value = RechargeOrderDTO.class)
	public RestResponse recharge(CreateRechargeOrderCommand cmd){
		
		RechargeOrderDTO order = parkService.createRechargeOrder(cmd);
		RestResponse response = new RestResponse(order);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * 
	 * <b>URL: /techpark/park/rechargeResult</b>
	 * @return
	 */
	@RequestMapping("rechargeResult")
	@RestReturn(value = RechargeSuccessResponse.class)
	public RestResponse rechargeResult(RechargeResultSearchCommand cmd) {
		
		RechargeSuccessResponse result = parkService.getRechargeStatus(cmd);
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/park/listRechargeRecord</b>
	 * list recharge record
	 * @return
	 */
	@RequestMapping("listRechargeRecord")
	@RestReturn(value = RechargeRecordList.class, collection = true)
	public RestResponse listRechargeRecord(RechargeRecordListCommand cmd){
		
		RechargeRecordList result = parkService.listRechargeRecord(cmd);
		
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/listParkingCharge</b>
	 * list all parking charge rule
	 * @return
	 */
	@RequestMapping("listParkingCharge")
	@RestReturn(value = ParkResponseList.class)
	public RestResponse listParkingCharge(ParkResponseListCommand cmd){
		
		ParkResponseList list = parkService.listParkingCharge(cmd);
		RestResponse response = new RestResponse(list);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/applyParkingCard</b>
	 * apply for parking card
	 * @return
	 */
	@RequestMapping("applyParkingCard")
	@RestReturn(value = WaitingLine.class)
	public RestResponse applyParkingCard(PlateNumberCommand cmd) {
		
		WaitingLine waiting = new WaitingLine();
		waiting.setWaitingPeople(parkService.applyParkingCard(cmd));
		RestResponse response = new RestResponse(waiting);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/refreshParkingSystem</b>
	 * pay order and return parameters the parking system need
	 * @return
	 */
	@RequestMapping("refreshParkingSystem")
	@RestReturn(value = RefreshParkingSystemResponse.class)
	public RestResponse refreshParkingSystem(OnlinePayBillCommand cmd) {
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}
