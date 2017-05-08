package com.everhomes.techpark.park;

import java.util.Set;

import javax.validation.Valid;

import org.apache.tools.ant.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.techpark.onlinePay.OnlinePayBillCommand;
import com.everhomes.rest.techpark.park.CreateRechargeOrderCommand;
import com.everhomes.rest.techpark.park.ListCardTypeCommand;
import com.everhomes.rest.techpark.park.ListCardTypeResponse;
import com.everhomes.rest.techpark.park.ParkResponseList;
import com.everhomes.rest.techpark.park.ParkResponseListCommand;
import com.everhomes.rest.techpark.park.ParkingPreferentialRuleDTO;
import com.everhomes.rest.techpark.park.PaymentRankingCommand;
import com.everhomes.rest.techpark.park.PlateInfo;
import com.everhomes.rest.techpark.park.PlateNumberCommand;
import com.everhomes.rest.techpark.park.PreferentialRulesDTO;
import com.everhomes.rest.techpark.park.QryPreferentialRuleByCommunityIdCommand;
import com.everhomes.rest.techpark.park.RechargeOrderDTO;
import com.everhomes.rest.techpark.park.RechargeRecordList;
import com.everhomes.rest.techpark.park.RechargeRecordListCommand;
import com.everhomes.rest.techpark.park.RechargeResultSearchCommand;
import com.everhomes.rest.techpark.park.RechargeSuccessResponse;
import com.everhomes.rest.techpark.park.SetParkingPreferentialRuleCommand;
import com.everhomes.rest.techpark.park.SetPreferentialRuleCommand;
import com.everhomes.rest.techpark.park.WaitingLine;



@RestDoc(value = "Park controller", site = "ehcore")
@RestController
@RequestMapping("/techpark/park")
@Deprecated
public class ParkController extends ControllerBase{
	
	@Autowired
	private ParkService parkService;
	
	@Autowired
    private ConfigurationProvider configurationProvider;
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
		
		RechargeSuccessResponse result = parkService.getRechargeStatus(cmd.getBillId());
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * 
	 * <b>URL: /techpark/park/listCardType</b>
	 * @return
	 */
	@RequestMapping("listCardType")
	@RestReturn(value = ListCardTypeResponse.class)
	public RestResponse listCardType(ListCardTypeCommand cmd) {
		
		ListCardTypeResponse result = parkService.listCardType(cmd);
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/park/listRechargeRecord</b>
	 * list logon user's recharge record
	 * @return
	 */
	@RequestMapping("listRechargeRecord")
	@RestReturn(value = RechargeRecordList.class)
	public RestResponse listRechargeRecord(RechargeRecordListCommand cmd){
		
		RechargeRecordList result = parkService.listRechargeRecord(cmd);
		
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/park/listParkingCharge</b>
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
	 * <b>URL: /techpark/park/applyParkingCard</b>
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
	 * <b>URL: /techpark/park/refreshParkingSystem</b>
	 * pay order and return parameters the parking system need
	 * @return
	 */
	@RequestMapping("refreshParkingSystem")
	@RestReturn(value = RechargeSuccessResponse.class)
	public RestResponse refreshParkingSystem(OnlinePayBillCommand cmd) {
		
		RechargeSuccessResponse refresh = parkService.refreshParkingSystem(cmd);
		RestResponse response = new RestResponse(refresh);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/park/updateRechargeOrder</b>
	 * update recharge info while refresh parking system success
	 * @return
	 */
	@RequestMapping("updateRechargeOrder")
	@RestReturn(value = String.class)
	public RestResponse updateRechargeOrder(RechargeResultSearchCommand cmd) {
		
		parkService.updateRechargeOrder(cmd.getBillId());
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/park/getRechargedPlate</b>
	 * @return
	 */
	@RequestMapping("getRechargedPlate")
	@RestReturn(value = String.class)
	public RestResponse getRechargedPlate() {
		
//		Set<String> plates = parkService.getRechargedPlate();
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
		
	}
	
	/**
	 * <b>URL: /techpark/park/verifyRechargedPlate</b>
	 * @return
	 */
	@RequestMapping("verifyRechargedPlate")
	@RestReturn(value = PlateInfo.class)
	public RestResponse verifyRechargedPlate(PlateNumberCommand cmd) {
		
		PlateInfo info = parkService.verifyRechargedPlate(cmd);
		RestResponse response = new RestResponse(info);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
		
	}
	

	/**
	 * <b>URL: /techpark/park/getPaymentRanking</b>
	 * @return payFailed; is in range
	 */
	@RequestMapping("getPaymentRanking")
	@RestReturn(value = String.class)
	public RestResponse getPaymentRanking(PaymentRankingCommand cmd) {
		
		String count = parkService.rechargeTop(cmd);
		RestResponse response = new RestResponse(count);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
		
	}
	
	/**
	 * <b>URL: /techpark/park/setParkingPreferentialRule</b>
	 * 停车缴费活动规则设置
	 * @return
	 */
	@RequestMapping("setParkingPreferentialRule")
	@RestReturn(value = ParkingPreferentialRuleDTO.class)
	public RestResponse setParkingPreferentialRule(SetParkingPreferentialRuleCommand cmd) {
		
        configurationProvider.setValue(ConfigConstants.PARKING_PREFERENTIAL_STARTTIME, cmd.getStartTime());
        configurationProvider.setValue(ConfigConstants.PARKING_PREFERENTIAL_ENDTIME, cmd.getEndTime());
        configurationProvider.setValue(ConfigConstants.PARKING_PREFERENTIAL_RANGE, cmd.getRange());
        
        SetPreferentialRuleCommand command = new SetPreferentialRuleCommand();
        try {
        	command.setStartTime(DateUtils.parseDateFromHeader(cmd.getStartTime()).getTime());
            command.setEndTime(DateUtils.parseDateFromHeader(cmd.getEndTime()).getTime());
            command.setBeforeNember(Long.valueOf(cmd.getRange()));
            parkService.setPreferentialRule(command);
		} catch (Exception e) {
			// TODO: handle exception
		}
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
		
	}
	
	/**
	 * <b>URL: /techpark/park/getParkingPreferentialRule</b>
	 * 获取车缴费活动规则
	 * @return
	 */
	@RequestMapping("getParkingPreferentialRule")
	@RestReturn(value = ParkingPreferentialRuleDTO.class)
	public RestResponse getParkingPreferentialRule(@Valid QryPreferentialRuleByCommunityIdCommand cmd) {
		
		ParkingPreferentialRuleDTO parkingPreferential = new ParkingPreferentialRuleDTO();
		PreferentialRulesDTO dto = parkService.qryPreferentialRuleByCommunityId(cmd);

		if(null != dto){
			 parkingPreferential.setStartTime(DateUtils.format(dto.getStartTime(), "YYYY-MM-DD"));
		     parkingPreferential.setEndTime(DateUtils.format(dto.getEndTime(), "YYYY-MM-DD"));
		     parkingPreferential.setRange(String.valueOf(dto.getBeforeNember()));
		}
       
		RestResponse response = new RestResponse(parkingPreferential);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/park/qryPreferentialRuleByCommunityId</b>
	 * 获取车缴费活动规则
	 * @return
	 */
	@RequestMapping("qryPreferentialRuleByCommunityId")
	@RestReturn(value = PreferentialRulesDTO.class)
	public RestResponse qryPreferentialRuleByCommunityId(@Valid QryPreferentialRuleByCommunityIdCommand cmd) {
		
		PreferentialRulesDTO dto = parkService.qryPreferentialRuleByCommunityId(cmd);
        
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/park/setPreferentialRule</b>
	 * 停车缴费活动规则设置
	 * @return
	 */
	@RequestMapping("setPreferentialRule")
	@RestReturn(value = String.class)
	public RestResponse setPreferentialRule(SetPreferentialRuleCommand cmd) {
		parkService.setPreferentialRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
		
	}
	
}
