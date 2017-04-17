package com.everhomes.ems;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.ems.TrackBillResponse;

@RestController
@RequestMapping("/ems")
public class EmsController extends ControllerBase {
	
	@Autowired
	private EmsService emsService;
	
	@RequestMapping("getBillNo")
	@RestReturn(String.class)
	public RestResponse getBillNo(){
		return new RestResponse(emsService.getBillNo("1"));
	}
	
	@RequestMapping("updatePrintInfo")
	@RestReturn(String.class)
	public RestResponse updatePrintInfo(){
		emsService.updatePrintInfo();
		return new RestResponse();
	}
	
	@RequestMapping("trackBill")
	@RestReturn(TrackBillResponse.class)
	public RestResponse trackBill(){
		return new RestResponse(emsService.trackBill("123456xxxxx"));
	}
}
