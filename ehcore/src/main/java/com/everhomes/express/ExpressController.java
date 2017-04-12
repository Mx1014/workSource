package com.everhomes.express;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.express.ListServiceAddressCommand;
import com.everhomes.rest.express.ListServiceAddressResponse;

@RestDoc(value="express", site="express controller")
@RestController
@RequestMapping("/express")
public class ExpressController extends ControllerBase {
	
	
	@RequestMapping("listServiceAddress")
	@RestReturn(ListServiceAddressResponse.class)
	public RestResponse listServiceAddress(ListServiceAddressCommand cmd) {
		return new RestResponse();
	}
	
}
