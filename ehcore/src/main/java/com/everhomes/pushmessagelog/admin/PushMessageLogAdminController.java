package com.everhomes.pushmessagelog.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.pushmessagelog.PushMessageLogService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.pushmessagelog.PushMessageListCommand;
import com.everhomes.rest.pushmessagelog.PushMessageLogReturnDTO;

/**
 * configurations management Controller
 * @author huanglm
 *
 */
@RestDoc(value="pushMessageLog Admin controller", site="core")
@RestController
@RequestMapping("/admin/pushMessageLog")
public class PushMessageLogAdminController extends ControllerBase{
	
	@Autowired
    private PushMessageLogService pushMessageLogService;
	

	/**
     * <b>URL: /admin/pushMessageLog/listPushMessageLogByNamespaceIdAndOperator</b>
     * <p>通过域空间、操作者ID来查询推送记录</p>
     */
	@RequestMapping("listPushMessageLogByNamespaceIdAndOperator")
    @RestReturn(value=PushMessageLogReturnDTO.class)
	public RestResponse listPushMessageLogByNamespaceIdAndOperator(PushMessageListCommand cmd) {
		
		PushMessageLogReturnDTO resultDTO = pushMessageLogService.listPushMessageLogByNamespaceIdAndOperator(cmd);
        RestResponse response = new RestResponse(resultDTO);
        setResponseSuccess(response);
        return response;
	}

	
	/**
	 * <p>设置response 成功信息</p>
	 * @param response
	 */
	private void setResponseSuccess(RestResponse response){
		if(response == null ) return ;
		
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
	}
}
