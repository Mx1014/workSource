package com.everhomes.ems;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

@RestController
@RequestMapping("/ems")
public class EmsController extends ControllerBase {
	
	@Autowired
	private EmsService emsService;
	
	@RequestMapping("getBillNo")
	@RestReturn(String.class)
	public RestResponse getBillNo(){
		return new RestResponse(emsService.getBillNo((byte)1));
	}
}
