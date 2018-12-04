// @formatter:off
package com.everhomes.welfare;

import javax.servlet.http.HttpServletRequest;

import com.everhomes.rest.welfare.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

@RestController
@RequestMapping("/welfare")
public class WelfareController extends ControllerBase {
	
	@Autowired
	private WelfareService welfareService;

	/**
	 * <p>后台查询福利列表</p>
	 * <b>URL: /welfare/listWelfares</b>
	 */
	@RequestMapping("listWelfares")
	@RestReturn(ListWelfaresResponse.class)
	public RestResponse listWelfares(ListWelfaresCommand cmd){
		return new RestResponse(welfareService.listWelfares(cmd));
	}

	/**
	 * <p>查询某个福利</p>
	 * <b>URL: /welfare/getWelfare</b>
	 */
	@RequestMapping("getWelfare")
	@RestReturn(GetWelfareResponse.class)
	public RestResponse getWelfare(GetWelfareCommand cmd){
		return new RestResponse(welfareService.getWelfare(cmd));
	}
	
	/**
	 * <p>保存草稿</p>
	 * <b>URL: /welfare/draftWelfare</b>
	 */
	@RequestMapping("draftWelfare")
	@RestReturn(DraftWelfareResponse.class)
	public RestResponse draftWelfare(DraftWelfareCommand cmd){
		DraftWelfareResponse resp = welfareService.draftWelfare(cmd);
		return new RestResponse(resp);
	}

	/**
	 * <p>发送福利</p>
	 * <b>URL: /welfare/sendWelfare</b>
	 */
	@RequestMapping("sendWelfare")
	@RestReturn(SendWelfaresResponse.class)
	public RestResponse sendWelfare(SendWelfareCommand cmd, HttpServletRequest request){
		return new RestResponse(welfareService.sendWelfare(cmd, request));
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
	/**
	 * <p>删除福利(草稿)</p>
	 * <b>URL: /welfare/deleteWelfare</b>
	 */
	@RequestMapping("deleteWelfare")
	@RestReturn(String.class)
	public RestResponse deleteWelfare(DeleteWelfareCommand cmd){
		welfareService.deleteWelfare(cmd);
		return new RestResponse();
	}

	/**
	 * <p>查询用户自己接受的福利列表</p>
	 * <b>URL: /welfare/listUserWelfares</b>
	 */
	@RequestMapping("listUserWelfares")
	@RestReturn(ListUserWelfaresResponse.class)
	public RestResponse listUserWelfares(ListUserWelfaresCommand cmd){
		return new RestResponse(welfareService.listUserWelfares(cmd));
	}

	/**
	 * <p>营销系统调用: 更新发送福利状态</p>
	 * <b>URL: /welfare/updateWelfareStatus</b>
	 */
	@RequestMapping("updateWelfareStatus")
	@RestReturn(String.class)
	public RestResponse updateWelfareStatus(UpdateWelfareStatusCommand cmd){
		welfareService.updateWelfareStatus(cmd);
		return new RestResponse();
	}
}