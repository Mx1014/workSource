package com.everhomes.repeat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.repeat.RepeatExpressionDTO;


@RestDoc(value = "Repeat Controller", site = "core")
@RestController
@RequestMapping("/repeat")
public class RepeatController extends ControllerBase {
	
	@Autowired
	private RepeatService repeatService;
	
	/**
	 * <b>URL: /repeat/testExpressionAnalyze</b>
	 * <p>解析expression</p>
	 */
	@RequestMapping("testExpressionAnalyze")
	@RestReturn(value = RepeatExpressionDTO.class, collection = true)
	public RestResponse testExpressionAnalyze() {
		
		List<RepeatExpressionDTO> expression = repeatService.test();
		
		RestResponse response = new RestResponse(expression);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

}
