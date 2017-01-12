package com.everhomes.openapi.jindi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.openapi.jindi.JindiFetchDataCommand;
import com.everhomes.util.RequireAuthentication;

/**
 * 
 * <ul>
 * 金地同步数据使用
 * </ul>
 */
@RestController
@RequestMapping("/jindi")
public class JindiOpenController extends ControllerBase {
	
	@Autowired
	private JindiOpenService jindiOpenService;
	
//	@RequireAuthentication(false)
	@RequestMapping("/fetchData")
	@RestReturn(String.class)
	public RestResponse fetchData(JindiFetchDataCommand cmd) {
		return new RestResponse(jindiOpenService.fetchData(cmd));
	}
}
