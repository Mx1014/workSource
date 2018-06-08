// @formatter:off
package com.everhomes.welfare;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.welfare.DraftWelfareCommand;
import com.everhomes.rest.welfare.GetUserWelfareCommand;
import com.everhomes.rest.welfare.GetUserWelfareResponse;
import com.everhomes.rest.welfare.ListWelfaresCommand;
import com.everhomes.rest.welfare.ListWelfaresResponse;
import com.everhomes.rest.welfare.SendWelfareCommand;

@RestController
@RequestMapping("/welfare")
public class WelfareController extends ControllerBase {
	
	@Autowired
	private WelfareService welfareService;
	
	/**
	 * <p>查询列表</p>
	 * <b>URL: /welfare/listWelfares</b>
	 */
	@RequestMapping("listWelfares")
	@RestReturn(ListWelfaresResponse.class)
	public RestResponse listWelfares(ListWelfaresCommand cmd){
		return new RestResponse(welfareService.listWelfares(cmd));
	}

	/**
	 * <p>保存草稿</p>
	 * <b>URL: /welfare/draftWelfare</b>
	 */
	@RequestMapping("draftWelfare")
	@RestReturn(String.class)
	public RestResponse draftWelfare(DraftWelfareCommand cmd){
		welfareService.draftWelfare(cmd);
		return new RestResponse();
	}

	/**
	 * <p>发送福利</p>
	 * <b>URL: /welfare/sendWelfare</b>
	 */
	@RequestMapping("sendWelfare")
	@RestReturn(String.class)
	public RestResponse sendWelfare(SendWelfareCommand cmd){
		welfareService.sendWelfare(cmd);
		return new RestResponse();
	}

	/**
	 * <p>查福利详情</p>
	 * <b>URL: /welfare/getUserWelfare</b>
	 */
	@RequestMapping("getUserWelfare")
	@RestReturn(GetUserWelfareResponse.class)
	public RestResponse getUserWelfare(GetUserWelfareCommand cmd){
		return new RestResponse(welfareService.getUserWelfare(cmd));
	}

}