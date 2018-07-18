package com.everhomes.servicehotline;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.rest.servicehotline.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.techpark.servicehotline.HotlineService;
import com.everhomes.util.RequireAuthentication;

@RestController
@RequestMapping("/hotline")
public class ServiceHotlineController extends ControllerBase {


	@Autowired
	private HotlineService hotlineService;

	/**
	 * <b>URL: /hotline/getHotlineTopic</b>
	 * <p>
	 * 获取热线顶部标题
	 * </p>
	 */
	@RequireAuthentication(false)
	@RequestMapping("getHotlineSubject")
	@RestReturn(value = GetHotlineSubjectResponse.class)
	public RestResponse getHotlineSubject(@Valid GetHotlineSubjectCommand cmd) {
		GetHotlineSubjectResponse res = hotlineService.getHotlineSubject(cmd);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /hotline/getHotlineList</b>
	 * <p>
	 * 获取热线列表
	 * </p>
	 */
	@RequireAuthentication(false)
	@RequestMapping("getHotlineList")
	@RestReturn(value = GetHotlineListResponse.class)
	public RestResponse getHotlineList(@Valid GetHotlineListCommand cmd) {
		GetHotlineListResponse res =hotlineService.getHotlineList(cmd);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	
	/**
	 * <b>URL: /hotline/getHotlineListAdmin</b>
	 * <p>
	 * 获取热线列表(后台)
	 * </p>
	 */
	@RequestMapping("getHotlineListAdmin")
	@RestReturn(value = GetHotlineListResponse.class)
	public RestResponse getHotlineListAdmin(@Valid GetHotlineListCommand cmd) {
		GetHotlineListResponse res =hotlineService.getHotlineListAdmin(cmd);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /hotline/addHotline</b>
	 * <p>
	 * 添加热线
	 * </p>
	 */
	@RequestMapping("addHotline")
	@RestReturn(value = String.class)
	public RestResponse addHotline(@Valid AddHotlineCommand cmd) {
		this.hotlineService.addHotline(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /hotline/deleteHotline</b>
	 * <p>
	 * 删除热线
	 * </p>
	 */
	@RequestMapping("deleteHotline")
	@RestReturn(value = String.class)
	public RestResponse deleteHotline(@Valid DeleteHotlineCommand cmd) {
		this.hotlineService.deleteHotline(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /hotline/updateHotline</b>
	 * <p>
	 * 更新热线
	 * </p>
	 */
	@RequestMapping("updateHotline")
	@RestReturn(value = String.class)
	public RestResponse updateHotline(@Valid UpdateHotlineCommand cmd) {
		this.hotlineService.updateHotline(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /hotline/updateHotlines</b>
	 * <p>
	 * 批量更新热线
	 * </p>
	 */
	@RequestMapping("updateHotlines")
	@RestReturn(value = String.class)
	public RestResponse updateHotlines(@Valid UpdateHotlinesCommand cmd) {
		this.hotlineService.updateHotlines(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}  
	

	/**
	 * <b>URL: /hotline/updateHotlineOrder</b>
	 * <p>
	 * 排序
	 * </p>
	 */
	@RequestMapping("updateHotlineOrder")
	@RestReturn(value = String.class)
	public RestResponse updateHotlineOrder(@Valid UpdateHotlinesCommand cmd) {
		this.hotlineService.updateHotlineOrder(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}  
	/**
	 * <b>URL: /hotline/setHotlineSubject</b>
	 * <p>
	 * 功能开关 
	 * </p>
	 */
	@RequestMapping("setHotlineSubject")
	@RestReturn(value = String.class)
	public RestResponse setHotlineSubject(@Valid SetHotlineSubjectCommand cmd) {
		this.hotlineService.setHotlineSubject(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	@RequestMapping("getUserInfoById")
	@RestReturn(value=GetUserInfoByIdResponse.class)
	public RestResponse getUserInfoById(@Valid GetUserInfoByIdCommand cmd) {
		GetUserInfoByIdResponse user = this.hotlineService.getUserInfoById(cmd);
		RestResponse response =  new RestResponse(user);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /hotline/getChatGroupList</b>
     * <p>获取所有会话列表</p>
     * <p>根据条件获取会话列表。 相同的两个人的会话定义为一个会话。</p>
     * <p>例： 客服A与用户A的有100条聊天记录，归为 “客服A-用户A”一个会话</p>
	 * <p>客服A与用户B的有1条聊天记录，归为 “客服A-用户B”一个会话</p>
	 * <p>客服A与用户C的无聊天记录，不属于会话。</p>
	 */
	@RequestMapping("getChatGroupList")
	@RestReturn(value=GetChatGroupListResponse.class)
	public RestResponse getChatGroupList(@Valid GetChatGroupListCommand cmd) {
		GetChatGroupListResponse chatGroup = this.hotlineService.getChatGroupList(cmd);
		RestResponse response =  new RestResponse(chatGroup);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /hotline/getChatRecordList</b>
     * <p>获取客服与用户的聊天记录</p>
	 */
	@RequestMapping("getChatRecordList")
	@RestReturn(value=GetChatRecordListResponse.class)
	public RestResponse getChatRecordList(@Valid GetChatRecordListCommand cmd) {
		GetChatRecordListResponse chatRecordList = this.hotlineService.getChatRecordList(cmd);
		RestResponse response =  new RestResponse(chatRecordList);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	
	/**
	 * <b>URL: /hotline/exportChatRecordList</b>
     * <p>导出客服与用户的聊天记录</p>
	 */
    @RequestMapping("exportChatRecordList")
    @RestReturn(value = String.class)
    public RestResponse exportChatRecordList(GetChatRecordListCommand cmd,  HttpServletResponse httpResponse){
    	hotlineService.exportChatRecordList(cmd, httpResponse);
    	 return new RestResponse();
    }
    
	/**
	 * <b>URL: /hotline/exportMultiChatRecordList</b>
     * <p>导出客服与用户的聊天记录</p>
	 */
    @RequestMapping("exportMultiChatRecordList")
    @RestReturn(value = String.class)
    public RestResponse exportMultiChatRecordList(GetChatGroupListCommand cmd,  HttpServletResponse httpResponse){
    	hotlineService.exportMultiChatRecordList(cmd, httpResponse);
   	 return new RestResponse();
    }
}
